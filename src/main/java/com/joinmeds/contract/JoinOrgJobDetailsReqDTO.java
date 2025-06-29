package com.joinmeds.contract;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinOrgJobDetailsReqDTO {

    private  String hiringFor;
    private  String yearExp;
    private  String skills;
    private  String natureJob;
    private  String payFrom;
    private  String payTo;
    private  String payRange;
    private  String jobDesc;

}
