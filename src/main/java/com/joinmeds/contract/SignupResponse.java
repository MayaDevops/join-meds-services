package com.joinmeds.contract;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
    public UUID id;
    public String orgName;
    public String officialEmail;
    public String officialPhone;
    public String incorporationNo;
    public String emailMobile;
    public Instant createdAt;
    public String userType;
    public String username;
    public String password;

}
