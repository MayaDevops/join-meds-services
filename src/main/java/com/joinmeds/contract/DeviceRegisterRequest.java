package com.joinmeds.contract;

import lombok.*;

import java.util.UUID;


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
