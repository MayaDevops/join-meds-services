package com.joinmeds.service;

import com.joinmeds.contract.WorkExperienceRequest;
import com.joinmeds.contract.WorkExperienceResponse;

import java.util.List;
import java.util.UUID;

public interface WorkExperienceService {
    WorkExperienceResponse save(WorkExperienceRequest request);
    WorkExperienceResponse update(UUID id, WorkExperienceRequest request);
    List<WorkExperienceResponse> fetchByUserId(UUID userId);
}

