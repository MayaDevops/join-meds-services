package com.joinmeds.service;

import com.joinmeds.contract.JoinMedsOrgDetailsRequest;
import com.joinmeds.contract.OrgListResponse;
import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.model.UserLogin;
import com.joinmeds.respository.JoinMedsOrgDetailsRepository;
import com.joinmeds.respository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JoinMedsOrgDetailsService {

    @Autowired
    private JoinMedsOrgDetailsRepository repository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    public JoinMedsOrgDetails saveOrgDetails(JoinMedsOrgDetailsRequest request) {
        JoinMedsOrgDetails details = JoinMedsOrgDetails.builder()
                .id(UUID.randomUUID())
                .aboutCompany(request.getAboutCompany())
                .jobHiringFor(request.getJobHiringFor())
                .yearExp(request.getYearExp())
                .skillRequired(request.getSkillRequired())
                .natureJob(request.getNatureJob())
                .payFrom(request.getPayFrom())
                .payTo(request.getPayTo())
                .salaryRange(request.getSalaryRange())
                .jobDesc(request.getJobDesc())
                .createdAt(LocalDateTime.now().toString())
                .build();
        return repository.save(details);
    }

    public JoinMedsOrgDetails updateOrgDetails(UUID id, JoinMedsOrgDetailsRequest request) {
        Optional<JoinMedsOrgDetails> optional = repository.findById(id);
        if (optional.isPresent()) {
            JoinMedsOrgDetails details = optional.get();
            details.setAboutCompany(request.getAboutCompany());
            details.setJobHiringFor(request.getJobHiringFor());
            details.setYearExp(request.getYearExp());
            details.setSkillRequired(request.getSkillRequired());
            details.setNatureJob(request.getNatureJob());
            details.setPayFrom(request.getPayFrom());
            details.setPayTo(request.getPayTo());
            details.setSalaryRange(request.getSalaryRange());
            details.setJobDesc(request.getJobDesc());
            return repository.save(details);
        }
        throw new RuntimeException("Record not found with ID: " + id);
    }

    public JoinMedsOrgDetails fetchById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
    }
    public List<OrgListResponse> getAllOrgDetails(String keyword) {
        List<JoinMedsOrgDetails> orgs;

        if (keyword != null && !keyword.isBlank()) {
            List<UserLogin> matchedUsers = userLoginRepository
                    .findByUserTypeAndOrgNameContainingIgnoreCaseOrUserTypeAndEmailMobileContainingIgnoreCaseOrUserTypeAndOfficialEmailContainingIgnoreCase(
                            "ORGANIZATION", keyword,
                            "ORGANIZATION", keyword,
                            "ORGANIZATION", keyword
                    );
            List<UUID> matchedUserIds = matchedUsers.stream()
                    .map(UserLogin::getId)
                    .collect(Collectors.toList());

            if (matchedUserIds.isEmpty()) return List.of();

            orgs = repository.findAll().stream()
                    .filter(org -> org.getUserId() != null && matchedUserIds.contains(org.getUserId()))
                    .collect(Collectors.toList());
        } else {
            orgs = repository.findAll();
        }

        return orgs.stream().map(org -> {
            UserLogin user = userLoginRepository.findById(org.getUserId()).orElse(null);
            return OrgListResponse.builder()
                    .orgId(org.getId())
                    .userId(org.getUserId())
                    .orgName(user != null ? user.getOrgName() : null)
                    .officialEmail(user != null ? user.getOfficialEmail() : null)
                    .officePhone(user != null ? user.getOfficePhone() : null)
                    .incorporationNo(user != null ? user.getIncorporationNo() : null)
                    .emailMobile(user != null ? user.getEmailMobile() : null)
                    .createdAt(org.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }


}

