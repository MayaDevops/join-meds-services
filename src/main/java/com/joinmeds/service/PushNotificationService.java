package com.joinmeds.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Delivers a notification to every active device of a user via Firebase Cloud Messaging.
 *
 * <p>Runs asynchronously and swallows every failure, so a dead token or an FCM outage can never
 * break the API call that triggered the notification — the same contract as
 * {@link MailService#sendTemplateMail}.
 */
@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    private final UserDeviceService userDeviceService;

    @Value("${app.fcm.enabled:false}")
    private boolean fcmEnabled;

    /** Android 8+ drops a notification whose channel is unknown to the app. */
    @Value("${app.fcm.android-channel-id:joinmeds_default}")
    private String androidChannelId;

    /**
     * @param recipientId UserLogin id of the recipient — the org for org-facing notifications,
     *                    the candidate for user-facing ones
     * @param data        extra key/value payload the client uses to deep-link; values must be
     *                    Strings, which is an FCM constraint
     */
    @Async
    public void sendToUser(UUID recipientId, String title, String body, Map<String, String> data) {
        if (!fcmEnabled) {
            log.info("FCM disabled; would have pushed to user {}: '{}' / '{}'", recipientId, title, body);
            return;
        }
        if (recipientId == null) {
            log.warn("Skipping push '{}' — no recipient id", title);
            return;
        }
        if (FirebaseApp.getApps().isEmpty()) {
            log.error("Skipping push to {} — Firebase Admin SDK is not initialised", recipientId);
            return;
        }

        List<String> tokens = userDeviceService.activeTokensForUser(recipientId);
        if (tokens.isEmpty()) {
            log.debug("No active devices for user {}; nothing to push", recipientId);
            return;
        }

        try {
            Map<String, String> payload = data == null ? new HashMap<>() : new HashMap<>(data);

            MulticastMessage message = MulticastMessage.builder()
                    // fully qualified: com.joinmeds.model.Notification would otherwise collide
                    .setNotification(com.google.firebase.messaging.Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(payload)
                    .setAndroidConfig(androidConfig())
                    .setApnsConfig(apnsConfig())
                    .addAllTokens(tokens)
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            log.info("Pushed to user {}: {} delivered, {} failed, {} device(s) targeted",
                    recipientId, response.getSuccessCount(), response.getFailureCount(), tokens.size());

            pruneDeadTokens(tokens, response);
        } catch (Exception ex) {
            log.error("Failed to push to user {}: {}", recipientId, ex.getMessage(), ex);
        }
    }

    /**
     * Android delivery options. Both configs are set on the same message: FCM applies only the
     * one matching each token's platform, so a single multicast still serves mixed devices.
     *
     * <p>HIGH priority wakes a device in Doze mode — without it a notification can sit undelivered
     * until the next maintenance window. The channel id must match the channel the app creates;
     * on Android 8+ a notification naming an unknown channel is discarded silently.
     */
    private AndroidConfig androidConfig() {
        return AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setChannelId(androidChannelId)
                        .setSound("default")
                        .build())
                .build();
    }

    /**
     * Apple delivery options. Without an {@code aps} block iOS shows the alert silently — no
     * sound, no badge increment. {@code apns-priority: 10} tells APNs to deliver immediately,
     * which is the correct setting for a user-visible alert.
     */
    private ApnsConfig apnsConfig() {
        return ApnsConfig.builder()
                .putHeader("apns-priority", "10")
                .setAps(Aps.builder()
                        .setSound("default")
                        // lets an app's notification service extension alter the alert before display
                        .setMutableContent(true)
                        .build())
                .build();
    }

    /**
     * A token FCM reports as UNREGISTERED or INVALID_ARGUMENT belongs to an app that was
     * uninstalled or had its data cleared. Deactivating it keeps the device table from filling
     * with tokens that can never be delivered to.
     */
    private void pruneDeadTokens(List<String> tokens, BatchResponse response) {
        List<SendResponse> responses = response.getResponses();
        for (int i = 0; i < responses.size() && i < tokens.size(); i++) {
            SendResponse sendResponse = responses.get(i);
            if (sendResponse.isSuccessful() || sendResponse.getException() == null) {
                continue;
            }
            MessagingErrorCode errorCode = sendResponse.getException().getMessagingErrorCode();
            if (errorCode == MessagingErrorCode.UNREGISTERED
                    || errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                userDeviceService.deactivateByToken(tokens.get(i));
            } else {
                log.warn("Push to one device failed with {} — token kept for retry", errorCode);
            }
        }
    }
}
