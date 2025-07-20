package com.joinmeds.controller;
import com.joinmeds.contract.WorkExperienceRequest;
import com.joinmeds.contract.WorkExperienceResponse;
import com.joinmeds.service.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/work-experience")
@RequiredArgsConstructor
public class WorkExperienceController {

    private final WorkExperienceService service;

    @PostMapping("/save")
    public WorkExperienceResponse save(@RequestBody WorkExperienceRequest request) {
        return service.save(request);
    }

    @PutMapping("/update/{id}")
    public WorkExperienceResponse update(@PathVariable UUID id, @RequestBody WorkExperienceRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/fetch/{userId}")
    public List<WorkExperienceResponse> fetch(@PathVariable UUID userId) {
        return service.fetchByUserId(userId);
    }
}
