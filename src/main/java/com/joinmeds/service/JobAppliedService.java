package com.joinmeds.service;
import com.joinmeds.contract.JobAppliedRequest;
import com.joinmeds.contract.JobAppliedResponse;
import com.joinmeds.model.*;
import com.joinmeds.respository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class JobAppliedService {

    private static final Logger log = LoggerFactory.getLogger(JobAppliedService.class);

    private final JobAppliedRepository repository;
    private final JoinMedsOrgDetailsRepository orgDetailsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final JoinMedsOrgJobDetailsRepository joinMedsOrgJobDetailsRepository;
    private final UserLoginRepository userLoginRepository;
    private final MailService mailService;
    private final NotificationService notificationService;

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

        notifyOrganization(saved);

        return toResponse(saved);
    }

    /**
     * After an application is saved, email the corresponding organization and
     * persist an in-app notification for the org's notification bell.
     * Any failure here is logged but never breaks the apply flow.
     */
    private void notifyOrganization(JobApplied saved) {
        try {
            // Candidate name: prefer applicant name on request, fall back to profile full name.
            String candidateName = saved.getApplicantName();
            if (candidateName == null || candidateName.isBlank()) {
                candidateName = userDetailsRepository.findByUserId(saved.getUserId())
                        .map(UserDetails::getFullname)
                        .orElse("A candidate");
            }

            String hiringFor = saved.getJobId() == null ? null
                    : joinMedsOrgJobDetailsRepository.findById(saved.getJobId())
                    .map(JoinMedsOrgJobDetails::getHiringFor)
                    .orElse(null);

            String candidateEmail = userDetailsRepository.findByUserId(saved.getUserId())
                    .map(UserDetails::getEmail)
                    .orElse(null);

            UserLogin org = saved.getOrgId() == null ? null
                    : userLoginRepository.findById(saved.getOrgId()).orElse(null);
            String orgName = org != null ? org.getOrgName() : null;
            String orgEmail = org != null ? org.getOfficialEmail() : null;

            String message = candidateName + " submitted job application"
                    + (hiringFor != null && !hiringFor.isBlank() ? " for " + hiringFor : "");

            // In-app notification for the bell icon.
            notificationService.create(saved.getOrgId(), saved.getUserId(), saved.getJobId(),
                    candidateName, message, "JOB_APPLICATION");

            // Email to the organization.
            Map<String, Object> vars = new HashMap<>();
            vars.put("orgName", orgName != null ? orgName : "Organization");
            vars.put("candidateName", candidateName);
            vars.put("hiringFor", hiringFor);
            vars.put("candidateEmail", candidateEmail);
            vars.put("submittedAt", saved.getSubmittedAt() != null ? saved.getSubmittedAt().toString() : null);

            mailService.sendTemplateMail(
                    orgEmail,
                    "New Job Application" + (hiringFor != null ? " - " + hiringFor : ""),
                    "org-job-application",
                    vars);
        } catch (Exception ex) {
            log.error("Failed to notify organization for application {}: {}", saved.getId(), ex.getMessage(), ex);
        }
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
                .submittedAt(String.valueOf(entity.getSubmittedAt()))
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


public Page<JobAppliedResponse> searchApplications(UUID userId, UUID jobId, UUID orgId, UUID id, String keyword, int page, int size) {
    PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedAt"));

    if (keyword != null && !keyword.isBlank()) {
        Set<UUID> byMobile = userLoginRepository
                .findByEmailMobileContainingIgnoreCase(keyword)
                .stream().map(u -> u.getId()).collect(Collectors.toSet());

        Set<UUID> byNameOrEmail = userDetailsRepository
                .findByFullnameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword)
                .stream().map(u -> u.getUserId()).collect(Collectors.toSet());

        Set<UUID> matchedUserIds = Stream.concat(byMobile.stream(), byNameOrEmail.stream())
                .collect(Collectors.toSet());

        if (matchedUserIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return repository.searchWithUserIds(userId, jobId, orgId, id, matchedUserIds, pageable)
                .map(this::toTableResponse);
    }

    return repository.search(userId, jobId, orgId, id, pageable)
            .map(this::toTableResponse);
}
    private JobAppliedResponse toTableResponse(JobApplied app) {

        JobAppliedResponse res = new JobAppliedResponse();
        res.setId(app.getId());
        res.setUserId(app.getUserId());
        res.setOrgId(app.getOrgId());
        res.setJobId(app.getJobId());
        res.setResumeId(app.getResumeId());
       // res.setFullname(app.getApplicantName());
        res.setStatus(app.getStatus());

        res.setSubmittedAt(app.getSubmittedAt() != null
                ? app.getSubmittedAt().toString()
                : null);


        userLoginRepository.findById(app.getUserId()).ifPresent(user -> {

            res.setEmailMobile(user.getEmailMobile());
            res.setEmail(user.getOfficialEmail());

        });
        List<UserDetails> list = userDetailsRepository.findAllByUserId(app.getUserId());

        if (!list.isEmpty()) {
            UserDetails users = list.get(0); // or pick latest by updatedAt
            res.setFullname(users.getFullname());
        }


        joinMedsOrgJobDetailsRepository.findById(app.getJobId()).ifPresent(job -> {

            res.setPayFrom(job.getPayFrom());
            res.setPayTo(job.getPayTo());
            res.setPayRange(job.getPayRange());
            res.setHiringFor(job.getHiringFor());
            res.setNatureJob(job.getNatureJob());
            orgDetailsRepository.findById(job.getOrgId())
                    .ifPresent(org -> res.setOrgName(org.getAboutCompany()));
        });

        return res;
    }

}
