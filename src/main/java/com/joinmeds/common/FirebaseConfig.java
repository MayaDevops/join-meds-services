package com.joinmeds.common;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Initialises the Firebase Admin SDK once at startup.
 *
 * <p>The service account JSON is referenced by path and read from outside the repository —
 * it is a private key and must never be committed. Set {@code FCM_CREDENTIALS_PATH} in the
 * environment, exactly as {@code MAIL_USERNAME} / {@code MAIL_PASSWORD} already are.
 *
 * <p>Startup never fails because of FCM: if the SDK cannot be initialised the error is logged
 * and the application continues serving requests without push, matching how
 * {@link com.joinmeds.service.MailService} degrades when mail is unavailable.
 */
@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${app.fcm.enabled:false}")
    private boolean fcmEnabled;

    @Value("${app.fcm.credentials-path:}")
    private String credentialsPath;

    @PostConstruct
    public void init() {
        if (!fcmEnabled) {
            log.info("FCM disabled (app.fcm.enabled=false); push notifications will be logged, not sent");
            return;
        }
        if (credentialsPath == null || credentialsPath.isBlank()) {
            log.error("app.fcm.enabled=true but app.fcm.credentials-path is empty — "
                    + "push notifications are unavailable. Set FCM_CREDENTIALS_PATH.");
            return;
        }
        if (!FirebaseApp.getApps().isEmpty()) {
            log.debug("FirebaseApp already initialised; skipping");
            return;
        }
        try (InputStream serviceAccount = new FileInputStream(credentialsPath)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            log.info("Firebase Admin SDK initialised from {}", credentialsPath);
        } catch (Exception ex) {
            log.error("Failed to initialise Firebase Admin SDK from {}: {}",
                    credentialsPath, ex.getMessage(), ex);
        }
    }
}
