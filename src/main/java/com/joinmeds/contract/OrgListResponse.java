package com.joinmeds.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgListResponse {
    private UUID orgId;
    private UUID userId;
    private String orgName;
    private String officialEmail;
    private String officePhone;
    private String incorporationNo;
    private String emailMobile;
    private String createdAt;
}