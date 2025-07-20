package com.joinmeds.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "join_meds_user_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
    private LocalDateTime createdAt;
    private UUID userId;
    private String currentYear;
    private String university;
    private String academicQualification;
    private String foreignCountryWorked;
    private String foreignCountryWorkExp;
    private String languageTestScore;

}
