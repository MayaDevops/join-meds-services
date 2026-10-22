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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

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
     *
     * <p>A notification row carries both {@code orgId} and {@code userId}. In the existing
     * JOB_APPLICATION flow {@code orgId} is the recipient organisation (see
     * {@link Notification#getOrgId()}) while {@code userId} is the candidate who acted, so the
     * org takes precedence here. {@code userId} is treated as the recipient only when there is
     * no org — the shape a future candidate-facing notification ("you have been shortlisted")
     * would take. Worth confirming before any user-facing notification type is added.
     */
    private void pushToRecipient(Notification saved) {
        UUID recipientId = saved.getOrgId() != null ? saved.getOrgId() : saved.getUserId();

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