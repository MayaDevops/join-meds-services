package com.joinmeds.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;
@Entity
@Table(name = "otp_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpLog {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String mobile;

    @Column(nullable = false, length = 10)
    private String otp;

    @Column(columnDefinition = "text")
    private String message;

    @Column(name = "template_id", length = 30)
    private String templateId;

    @Column(length = 50)
    private String provider;

    @Column(name = "provider_message_id", length = 100)
    private String providerMessageId;

    @Column(name = "provider_status", length = 50)
    private String providerStatus;

    @Column(name = "http_status")
    private Integer httpStatus;

    private Integer charges;
    private Integer unit;

    @Column(name = "sms_length")
    private Integer smsLength;

    @Column(name = "raw_response", columnDefinition = "text")
    private String rawResponse;

    private Boolean success;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    private Boolean verified;

    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;
}