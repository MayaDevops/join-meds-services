package com.joinmeds.contract;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordResponse {
    private boolean success;
    private String message;
}