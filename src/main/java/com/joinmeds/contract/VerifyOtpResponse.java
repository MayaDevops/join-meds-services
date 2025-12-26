package com.joinmeds.contract;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyOtpResponse {
    private boolean success;
    private String message;
    private String mobile;
}
