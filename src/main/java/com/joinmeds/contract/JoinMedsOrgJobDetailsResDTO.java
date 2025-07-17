package com.joinmeds.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinMedsOrgJobDetailsResDTO {
    private UUID id;
    private UUID userId;
    private UUID orgId;
    private  String hiringFor;
    private  String yearExp;
    private  String skills;
    private  String natureJob;
    private  String payFrom;
    private  String payTo;
    private  String payRange;
    private  String jobDesc;
    private String orgName;
    private LocalDateTime createdAt;
}
