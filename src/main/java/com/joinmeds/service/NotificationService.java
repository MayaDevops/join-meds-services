package com.joinmeds.service;

import com.joinmeds.contract.NotificationResponse;
import com.joinmeds.model.Notification;
import com.joinmeds.respository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

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
        return notificationRepository.save(notification);
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