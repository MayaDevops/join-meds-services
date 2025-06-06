package com.joinmeds.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class SignupRequest {
    public String orgName;
    public String officialEmail;
    public String officialPhone;
    public String incorporationNo;
    public String emailMobile;
     public String password;
    public String confirmPassword;
    public Instant createdAt;
    public String userType;

}
