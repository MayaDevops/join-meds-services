package com.joinmeds.service;

import com.joinmeds.contract.JoinMedsOrgDetailsRequest;
import com.joinmeds.contract.UserDetailsDTO;
import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.model.UserDetails;
import com.joinmeds.respository.JoinMedsOrgDetailsRepository;
import com.joinmeds.respository.UserDetailsRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@Data
@RequiredArgsConstructor
public class UserProfileService {

    @Autowired
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    private  final JoinMedsOrgDetailsRepository joinMedsOrgDetailsRepository;


    public UserDetailsDTO fetchUserDetailsById(UUID id) {
        UserDetails userDetails = userDetailsRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        UserDetailsDTO response = new UserDetailsDTO();

        response.setUserId(userDetails.getUserId());
        response.setFullname(userDetails.getFullname());
        response.setDob(userDetails.getDob());
        response.setEmail(userDetails.getEmail());
        response.setAddress(userDetails.getAddress());
        response.setAadhaarNo(userDetails.getAadhaarNo());
        response.setPhotoId(userDetails.getPhotoId());
        response.setResumeId(userDetails.getResumeId());
        response.setProfession(userDetails.getProfession());
        response.setAcademicStatus(userDetails.getAcademicStatus());
        response.setPgStatus(userDetails.getPgStatus());
        response.setSpeciality(userDetails.getSpeciality());
        response.setPhdStatus(userDetails.getPhdStatus());
        response.setClinicalNonclinical(userDetails.getClinicalNonclinical());
        response.setCountryPreffered(userDetails.getCountryPreffered());
        response.setForeignTest(userDetails.getForeignTest());
        response.setForeignTestDetails(userDetails.getForeignTestDetails());
        response.setLanguageTest(userDetails.getLanguageTest());
        response.setForeignCountryExp(userDetails.getForeignCountryExp());
        response.setLanguageTestCleared(userDetails.getLanguageTestCleared());
        response.setPassportNumber(userDetails.getPassportNumber());
        response.setCertification(userDetails.getCertification());

        return response;
    }

    public JoinMedsOrgDetailsRequest fetchOrgDetailsById(UUID id) {
        JoinMedsOrgDetails joinMedsOrgDetails = joinMedsOrgDetailsRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        JoinMedsOrgDetailsRequest response = new JoinMedsOrgDetailsRequest();

        response.setAboutCompany(joinMedsOrgDetails.getAboutCompany());
        response.setJobHiringFor(joinMedsOrgDetails.getJobHiringFor());
        response.setYearExp(joinMedsOrgDetails.getYearExp());
        response.setSkillRequired(joinMedsOrgDetails.getSkillRequired());
        response.setNatureJob(joinMedsOrgDetails.getNatureJob());
        response.setPayFrom(joinMedsOrgDetails.getPayFrom());
        response.setPayTo(joinMedsOrgDetails.getPayTo());
        response.setSalaryRange(joinMedsOrgDetails.getSalaryRange());
        response.setJobDesc(joinMedsOrgDetails.getJobDesc());

        return response;
    }
}



