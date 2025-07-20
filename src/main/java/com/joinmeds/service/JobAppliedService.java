package com.joinmeds.service;
import com.joinmeds.contract.JobAppliedRequest;
import com.joinmeds.contract.JobAppliedResponse;
import com.joinmeds.model.*;
import com.joinmeds.respository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobAppliedService {

    private final JobAppliedRepository repository;
    private final JoinMedsOrgDetailsRepository orgDetailsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final JoinMedsOrgJobDetailsRepository joinMedsOrgJobDetailsRepository;
    private final UserLoginRepository userLoginRepository;

    public JobAppliedResponse saveApplication(JobAppliedRequest request) {
        JobApplied job = JobApplied.builder()
                .userId(request.getUserId())
                .orgId(request.getOrgId())
                .jobId(request.getJobId())
                .applicantName(request.getApplicantName())
                .resumeId(request.getResumeId())
                .submittedAt(LocalDateTime.now())
                .status("APPLIED")
                .build();

        JobApplied saved = repository.save(job);

        return toResponse(saved);
    }

    public List<JobAppliedResponse> getById(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    private JobAppliedResponse toResponse(JobApplied entity) {
        // Fetch org name using orgId
        String hiringFor = joinMedsOrgJobDetailsRepository.findById(entity.getJobId())
                .map(JoinMedsOrgJobDetails::getHiringFor)
                .orElse(null);
        String orgName = userLoginRepository.findById(entity.getOrgId())
                .map(UserLogin::getOrgName)
                .orElse(null);
        String emailMobile = userLoginRepository.findById(entity.getUserId())
                .map(UserLogin::getEmailMobile)
                .orElse(null);

        String natureJob = joinMedsOrgJobDetailsRepository.findById(entity.getJobId())
                .map(JoinMedsOrgJobDetails::getNatureJob)
                .orElse(null);
        String payFrom = joinMedsOrgJobDetailsRepository.findById(entity.getJobId())
                .map(JoinMedsOrgJobDetails::getPayFrom)
                .orElse(null);
        String payTo = joinMedsOrgJobDetailsRepository.findById(entity.getJobId())
                .map(JoinMedsOrgJobDetails::getPayTo)
                .orElse(null);
        String payRange = joinMedsOrgJobDetailsRepository.findById(entity.getJobId())
                .map(JoinMedsOrgJobDetails::getPayRange)
                .orElse(null);

        // Fetch user full name using userId
        String fullName = userDetailsRepository.findByUserId(entity.getUserId())
                .map(UserDetails::getFullname)
                .orElse(null);
        String email = userDetailsRepository.findByUserId(entity.getUserId())
                .map(UserDetails::getEmail)
                .orElse(null);

        return JobAppliedResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .orgId(entity.getOrgId())
                .jobId(entity.getJobId())
                .resumeId(entity.getResumeId())
                .submittedAt(entity.getSubmittedAt())
                .status(entity.getStatus())
                .hiringFor(hiringFor)
                .orgName(orgName)
                .emailMobile(emailMobile)
                .natureJob(natureJob)
                .payFrom(payFrom)
                .email(email)
                .payTo(payTo)
                .payRange(payRange)
                .fullname(fullName)
                .build();

    }

    public List<JobAppliedResponse> searchApplications(UUID userId, UUID jobId,UUID orgId, UUID id) {
        List<JobApplied> all = repository.findAll();

        return all.stream()
                .filter(app -> userId == null || userId.equals(app.getUserId()))
                .filter(app -> orgId == null || orgId.equals(app.getOrgId()))
                .filter(app -> jobId == null || jobId.equals(app.getJobId()))
                .filter(app -> id == null || id.equals(app.getId()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
