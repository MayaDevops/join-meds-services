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
public class LoginResponse {
    private UUID id;
    private String username;
    private String emailMobile;
    private String userType;
    private String orgName;
    private String incorporationNo;
    private String officialEmail;
    private String officePhone;
    private String createdAt;
    private UUID orgId;
}