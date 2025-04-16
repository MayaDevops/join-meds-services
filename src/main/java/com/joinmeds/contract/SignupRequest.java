package com.joinmeds.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignupRequest {
    public String username;
    public String password;
    public String confirmPassword;
    public String emailMobile;
}
