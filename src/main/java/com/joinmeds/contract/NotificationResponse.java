package com.joinmeds.contract;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private UUID id;
    private UUID orgId;
    private UUID userId;
    private UUID jobId;
    private String candidateName;
    private String message;
    private String type;
    private Boolean read;
    private String createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NotificationList {
        private long unreadCount;
        private List<NotificationResponse> notifications;
    }
}