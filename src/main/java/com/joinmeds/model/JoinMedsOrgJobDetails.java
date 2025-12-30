package com.joinmeds.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "join_org_job_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinMedsOrgJobDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "hiring_for")
    private String hiringFor;

    @Column(name = "year_exp")
    private String yearExp;

    @Column(name = "skills")
    private String skills;

    @Column(name = "nature_job")
    private String natureJob;

    @Column(name = "pay_from")
    private String payFrom;

    @Column(name = "pay_to")
    private String payTo;

    @Column(name = "pay_range")
    private String payRange;

    @Column(name = "job_desc", length = 1000)
    private String jobDesc;

//    @Column(name = "created_at")
//
//    private LocalDateTime createdAt;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "org_id")
    private UUID orgId;

    @Column(name = "is_active")
    private Boolean isActive;

}
