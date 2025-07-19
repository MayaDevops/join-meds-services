package com.joinmeds.contract;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAppliedRequest {

    private UUID userId;
    private UUID orgId;
    private UUID jobId;
    private String applicantName;
    private String resumeId;
}

