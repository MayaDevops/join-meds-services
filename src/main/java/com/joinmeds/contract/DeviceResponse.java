package com.joinmeds.contract;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * The FCM token is deliberately not returned — it is a delivery credential, and the client
 * already holds it.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceResponse {

    private UUID id;
    private UUID userId;
    private String deviceId;
    private String platform;
    private Boolean active;
    private String createdAt;
    private String updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeviceList {
        private long activeCount;
        private List<DeviceResponse> devices;
    }
}
