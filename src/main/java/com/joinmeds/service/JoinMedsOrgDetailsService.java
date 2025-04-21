package com.joinmeds.service;

import com.joinmeds.contract.JoinMedsOrgDetailsRequest;
import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.respository.JoinMedsOrgDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JoinMedsOrgDetailsService {

    @Autowired
    private JoinMedsOrgDetailsRepository repository;

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
                .createdAt(LocalDateTime.now())
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
    public List<JoinMedsOrgDetails> getAllOrgDetails() {
        return repository.findAll();
    }


}

