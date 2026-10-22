package com.joinmeds.service;

import com.joinmeds.contract.NotificationResponse;
import com.joinmeds.model.Notification;
import com.joinmeds.respository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    /** Types whose recipient is the organisation rather than the user named on the row. */
    private static final Set<String> ORG_FACING_TYPES = Set.of("JOB_APPLICATION");

    private final NotificationRepository notificationRepository;
    private final PushNotificationService pushNotificationService;

    public Notification create(UUID orgId, UUID userId, UUID jobId, String candidateName,
                               String message, String type) {
        Notification notification = Notification.builder()
                .orgId(orgId)
                .userId(userId)
                .jobId(jobId)
                .candidateName(candidateName)
                .message(message)
                .type(type)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
        Notification saved = notificationRepository.save(notification);

        // Every notification in the system is created here, so push delivery is hooked at this
        // single point rather than at each call site.
        try {
            pushToRecipient(saved);
        } catch (Exception ex) {
            log.error("Failed to dispatch push for notification {}: {}", saved.getId(), ex.getMessage(), ex);
        }

        return saved;
    }

    /**
     * Pushes a saved notification to the recipient's registered devices.
     */
    private void pushToRecipient(Notification saved) {
        UUID recipientId = recipientOf(saved);
        if (recipientId == null) {
            log.warn("Skipping push for notification {} of type {} — recipient could not be resolved",
                    saved.getId(), saved.getType());
            return;
        }

        // FCM data values must be Strings.
        Map<String, String> data = new HashMap<>();
        data.put("notificationId", String.valueOf(saved.getId()));
        if (saved.getType() != null) {
            data.put("type", saved.getType());
        }
        if (saved.getJobId() != null) {
            data.put("jobId", saved.getJobId().toString());
        }

        pushNotificationService.sendToUser(recipientId, titleFor(saved.getType()), saved.getMessage(), data);
    }

    /**
     * Resolves which UserLogin a notification should be pushed to.
     *
     * <p>A notification row carries both {@code orgId} and {@code userId}, and which one is the
     * recipient depends on the type. For an org-facing type such as JOB_APPLICATION, {@code orgId}
     * is the recipient organisation and {@code userId} is the candidate who acted — so an
     * unresolvable org must not fall back to {@code userId}, or the applicant is pushed their own
     * application. {@code JobAppliedService.resolveOrgUserLogin} returns null for an org that is
     * not in the database, which makes that case reachable in practice.
     *
     * <p>Any other (candidate-facing) type, such as a future "you have been shortlisted", is
     * addressed to {@code userId}.
     */
    private UUID recipientOf(Notification saved) {
        if (ORG_FACING_TYPES.contains(saved.getType())) {
            return saved.getOrgId();
        }
        return saved.getUserId() != null ? saved.getUserId() : saved.getOrgId();
    }

    private String titleFor(String type) {
        if ("JOB_APPLICATION".equals(type)) {
            return "New job application";
        }
        return "JoinMeds";
    }

    public NotificationResponse.NotificationList getByOrg(UUID orgId) {
        List<NotificationResponse> list = notificationRepository.findByOrgIdOrderByCreatedAtDesc(orgId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        long unread = notificationRepository.countByOrgIdAndReadFalse(orgId);
        return NotificationResponse.NotificationList.builder()
                .unreadCount(unread)
                .notifications(list)
                .build();
    }

    public NotificationResponse.NotificationList getByUser(UUID userId) {
        List<NotificationResponse> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        long unread = notificationRepository.countByUserIdAndReadFalse(userId);
        return NotificationResponse.NotificationList.builder()
                .unreadCount(unread)
                .notifications(list)
                .build();
    }

    public void markAsRead(UUID id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    public void markAllAsRead(UUID orgId) {
        List<Notification> unread = notificationRepository.findByOrgIdOrderByCreatedAtDesc(orgId).stream()
                .filter(n -> !Boolean.TRUE.equals(n.getRead()))
                .collect(Collectors.toList());
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    public void markAllAsReadForUser(UUID userId) {
        List<Notification> unread = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(n -> !Boolean.TRUE.equals(n.getRead()))
                .collect(Collectors.toList());
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .orgId(n.getOrgId())
                .userId(n.getUserId())
                .jobId(n.getJobId())
                .candidateName(n.getCandidateName())
                .message(n.getMessage())
                .type(n.getType())
                .read(n.getRead())
                .createdAt(n.getCreatedAt() != null ? n.getCreatedAt().toString() : null)
                .build();
    }
}