package com.joinmeds.contract;
import lombok.*;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinMedsOrgDetailsRequest {
    private String aboutCompany;
    private String jobHiringFor;
    private String yearExp;
    private String skillRequired;
    private String natureJob;
    private String payFrom;
    private String payTo;
    private String salaryRange;
    private String jobDesc;
}
