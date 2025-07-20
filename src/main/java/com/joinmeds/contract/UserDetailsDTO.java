package com.joinmeds.contract;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private String fullname;
    private String dob;
    private String email;
    private String address;
    private String aadhaarNo;
    private String photoId;
    private String resumeId;
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
    private String currentYear;
    private String university;
    private String academicQualification;
    private String foreignCountryWorked;
    private String foreignCountryWorkExp;
    private String languageTestScore;

    private UUID userId;
}
