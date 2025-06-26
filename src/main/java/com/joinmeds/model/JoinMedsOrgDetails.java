package com.joinmeds.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "join_meds_org_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinMedsOrgDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "about_company")
    private String aboutCompany;

    @Column(name = "job_hiring_for")
    private String jobHiringFor;

    @Column(name = "year_exp")
    private String yearExp;

    @Column(name = "skill_required")
    private String skillRequired;

    @Column(name = "nature_job")
    private String natureJob;

    @Column(name = "pay_from")
    private String payFrom;

    @Column(name = "pay_to")
    private String payTo;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "job_desc", length = 1000)
    private String jobDesc;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_id")
    private UUID userId;
}
