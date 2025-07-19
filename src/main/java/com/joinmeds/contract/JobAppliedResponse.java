package com.joinmeds.contract;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAppliedResponse {

    private UUID id;
    private UUID userId;
    private UUID orgId;
    private UUID orgName;
    private String hiringFor;
    private String emailMobile;
    private UUID jobId;
    private String applicantName;
    private String resumeId;
    private LocalDateTime submittedAt;
}

