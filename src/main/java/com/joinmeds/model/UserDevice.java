package com.joinmeds.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A single device registered for push notifications.
 *
 * <p>One row per physical device, keyed by {@code deviceId} — a user may have a phone and a
 * tablet, and the same device may later be used by a different user after logout/login.
 * The FCM token is not stable: it is reissued on app reinstall, data clear or token refresh,
 * so the client re-registers and the existing row is updated rather than duplicated.
 */
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

    /** Owning user (UserLogin id). */
    @Column(name = "user_id")
    private UUID userId;

    /** FCM registration token this device is currently reachable on. */
    @Column(name = "fcm_token", length = 512)
    private String fcmToken;

    /** Stable client-generated device identifier; the natural key for this row. */
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
