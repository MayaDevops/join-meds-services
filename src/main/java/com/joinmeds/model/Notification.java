package com.joinmeds.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "join_meds_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Recipient organization (UserLogin id of the org). */
    @Column(name = "org_id")
    private UUID orgId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "candidate_name")
    private String candidateName;

    @Column(name = "message", length = 500)
    private String message;

    @Column(name = "type")
    private String type;

    @Column(name = "is_read")
    private Boolean read;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}