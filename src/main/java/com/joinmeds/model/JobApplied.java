package com.joinmeds.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "join_org_job_applied")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplied {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "org_id")
    private UUID orgId;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "applicant_name")
    private String applicantName;

    @Column(name = "resume_id")
    private String resumeId;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    @Column(name = "status")
    private String status;
}
