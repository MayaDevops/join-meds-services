package com.joinmeds.contract;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyOtpRequest {

    @NotBlank(message = "mobile is required")
    private String mobile;

    @NotBlank(message = "otp is required")
    private String otp;
}
