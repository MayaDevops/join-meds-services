package com.joinmeds.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinOrgJobDetailsResDTO {
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
}
