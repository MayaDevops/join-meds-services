package com.joinmeds.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "join_meds_work_experience")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class WorkExperience {

    @Id
    private UUID id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "clinical_nonclinical")
    private String clinicalNonclinical;

    @Column(name = "worked_hosp_name")
    private String workedHospName;

    @Column(name = "work_specialisation")
    private String workSpecialisation;

    @Column(name = "from_date")
    private String fromDate;

    @Column(name = "to_date")
    private String toDate;
}

