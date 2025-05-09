package com.joinmeds.contract;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDTO {
    private UUID id;
    private String fullname;
    private String dob;
    private String email;
    private String address;
    private String aadhaarNo;
    private UUID photoId;
    private UUID resumeId;
    private String profession;
    private String academicStatus;
    private String pgStatus;
    private String speciality;
    private String phdStatus;
    private String workExperience;
    private String clinicalNonclinical;
    private String countryPreffered;
    private String foreignTest;
    private String foreignTestDetails;
    private String foreignCountryExp;
    private String languageTest;
    private String languageTestCleared;
    private String passportNumber;
    private String certification;
    private UUID userId;
}
