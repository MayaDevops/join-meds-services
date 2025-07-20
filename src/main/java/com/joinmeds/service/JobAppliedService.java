package com.joinmeds.service;
import com.joinmeds.contract.JobAppliedRequest;
import com.joinmeds.contract.JobAppliedResponse;
import com.joinmeds.model.JobApplied;
import com.joinmeds.respository.JobAppliedRepository;
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
        return JobAppliedResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .orgId(entity.getOrgId())
                .jobId(entity.getJobId())
                .status(entity.getStatus())
                .resumeId(entity.getResumeId())
                .submittedAt(entity.getSubmittedAt())
                .build();
    }

    public List<JobAppliedResponse> searchApplications(UUID userId, UUID orgId, UUID id) {
        List<JobApplied> all = repository.findAll();

        return all.stream()
                .filter(app -> userId == null || userId.equals(app.getUserId()))
                .filter(app -> orgId == null || orgId.equals(app.getOrgId()))
                .filter(app -> id == null || id.equals(app.getId()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
