package com.joinmeds.contract;

import lombok.*;

import java.util.UUID;

/**
 * Sent by the mobile/web client after login, and again whenever FCM issues a new token.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceRegisterRequest {

    private UUID userId;

    /** FCM registration token obtained from the Firebase SDK on the client. */
    private String fcmToken;

    /** Stable client-generated device identifier. */
    private String deviceId;

    /** ANDROID, IOS or WEB. */
    private String platform;
}
