package com.joinmeds.contract;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String orgName;
    private String fullname;
    private String hiringFor;
    private String natureJob;
    private String emailMobile;
    private String email;
    private UUID jobId;
    private String resumeId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String submittedAt;
    private String payFrom;
    private String payTo;
    private String payRange;
    private String status;
}

