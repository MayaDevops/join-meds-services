package com.joinmeds.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinMedsOrgJobsaveResDTO {

        private String message;
        private UUID id;
        private int statusCode;

}
