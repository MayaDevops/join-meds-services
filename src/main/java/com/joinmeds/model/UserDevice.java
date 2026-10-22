package com.joinmeds.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "join_meds_user_device")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "fcm_token", length = 512)
    private String fcmToken;

    @Column(name = "device_id")
    private String deviceId;

    /** ANDROID, IOS or WEB. */
    @Column(name = "platform")
    private String platform;

    /** False once the user logs out or FCM reports the token as no longer registered. */
    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
