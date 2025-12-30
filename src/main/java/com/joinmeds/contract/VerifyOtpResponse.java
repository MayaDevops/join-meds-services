package com.joinmeds.contract;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyOtpResponse {
    private boolean success;
    private String message;
    private String mobile;
    private UUID userId;
}
