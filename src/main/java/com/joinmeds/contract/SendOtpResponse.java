package com.joinmeds.contract;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendOtpResponse {

        private boolean success;
        private String message;
        private String mobile;
        private String providerMessageId;
        private String providerStatus;
        private Integer httpStatus;
        private Integer charges;
    
}
