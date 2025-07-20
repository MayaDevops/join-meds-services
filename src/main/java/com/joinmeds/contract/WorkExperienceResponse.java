package com.joinmeds.contract;

import lombok.*;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class WorkExperienceResponse {
    private UUID id;
    private UUID userId;
    private String clinicalNonclinical;
    private String workedHospName;
    private String workSpecialisation;
    private String fromDate;
    private String toDate;
}

