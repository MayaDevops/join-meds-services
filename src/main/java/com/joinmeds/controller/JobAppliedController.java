package com.joinmeds.controller;
import com.joinmeds.contract.JobAppliedRequest;
import com.joinmeds.contract.JobAppliedResponse;
import com.joinmeds.service.JobAppliedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-applied")
@RequiredArgsConstructor
public class JobAppliedController {

    private final JobAppliedService service;

    @PostMapping("/save")
    public ResponseEntity<JobAppliedResponse> saveApplication(@RequestBody JobAppliedRequest request) {
        return ResponseEntity.ok(service.saveApplication(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobAppliedResponse>> getApplicationsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getById(userId));
    }
    @GetMapping("/search")
    public ResponseEntity<List<JobAppliedResponse>> getApplications(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID orgId,
            @RequestParam(required = false) UUID id
    ) {
        return ResponseEntity.ok(service.searchApplications(userId, orgId, id));
    }

}

