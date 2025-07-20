package com.joinmeds.service;

import com.joinmeds.contract.WorkExperienceRequest;
import com.joinmeds.contract.WorkExperienceResponse;
import com.joinmeds.model.WorkExperience;
import com.joinmeds.respository.WorkExperienceRepository;
import com.joinmeds.service.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkExperienceServiceImpl implements WorkExperienceService {

    private final WorkExperienceRepository repository;

    @Override
    public WorkExperienceResponse save(WorkExperienceRequest request) {
        // Parse date strings to LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fromDate = LocalDate.parse(request.getFromDate(), formatter);
        LocalDate toDate = LocalDate.parse(request.getToDate(), formatter);
        LocalDate today = LocalDate.now();

        // Validation: fromDate and toDate not in future
        if (fromDate.isAfter(today) || toDate.isAfter(today)) {
            throw new IllegalArgumentException("From date and To date must not be in the future.");
        }

        // Validation: toDate >= fromDate
        if (toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException("To date must not be before From date.");
        }

        WorkExperience entity = WorkExperience.builder()
                .id(UUID.randomUUID())
                .createdAt(Instant.now())
                .userId(request.getUserId())
                .clinicalNonclinical(request.getClinicalNonclinical())
                .workedHospName(request.getWorkedHospName())
                .workSpecialisation(request.getWorkSpecialisation())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .build();

        repository.save(entity);
        return toResponse(entity);
    }


    @Override
    public WorkExperienceResponse update(UUID id, WorkExperienceRequest request) {
        WorkExperience entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        entity.setClinicalNonclinical(request.getClinicalNonclinical());
        entity.setWorkedHospName(request.getWorkedHospName());
        entity.setWorkSpecialisation(request.getWorkSpecialisation());
        entity.setFromDate(request.getFromDate());
        entity.setToDate(request.getToDate());
        repository.save(entity);
        return toResponse(entity);
    }

    @Override
    public List<WorkExperienceResponse> fetchByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private WorkExperienceResponse toResponse(WorkExperience entity) {
        return WorkExperienceResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .clinicalNonclinical(entity.getClinicalNonclinical())
                .workedHospName(entity.getWorkedHospName())
                .workSpecialisation(entity.getWorkSpecialisation())
                .fromDate(entity.getFromDate())
                .toDate(entity.getToDate())
                .build();
    }
}

