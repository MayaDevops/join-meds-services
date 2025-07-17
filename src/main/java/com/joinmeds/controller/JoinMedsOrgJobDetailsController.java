package com.joinmeds.controller;


import com.joinmeds.contract.JoinMedsOrgJobDetailsReqDTO;
import com.joinmeds.contract.JoinMedsOrgJobDetailsResDTO;
import com.joinmeds.contract.JoinMedsOrgJobsaveResDTO;
import com.joinmeds.contract.Response;
import com.joinmeds.service.JoinMedsOrgJobDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/org-job")
public class JoinMedsOrgJobDetailsController {

    @Autowired
    private final JoinMedsOrgJobDetailsService service;

    @PostMapping("/save/{userId}")
    public ResponseEntity<?> save(@PathVariable UUID userId ,@RequestBody JoinMedsOrgJobDetailsReqDTO dto) {
        try {
            UUID jobId = service.save(userId,dto);
            JoinMedsOrgJobsaveResDTO response = new JoinMedsOrgJobsaveResDTO("Job application saved successfully", jobId, 200);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/update/{jobId}")
    public ResponseEntity<?> updateUserById(@PathVariable UUID jobId, @RequestBody JoinMedsOrgJobDetailsReqDTO request) {
        try {
            String message = service.update(jobId, request);
            JoinMedsOrgJobsaveResDTO response = new JoinMedsOrgJobsaveResDTO(message, jobId, 200);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping ("/delete/{jobId}")
    public ResponseEntity<?> deleteByJobId(@PathVariable UUID jobId) {
        try {
            String message = service.delete(jobId);
            JoinMedsOrgJobsaveResDTO response = new JoinMedsOrgJobsaveResDTO(message, jobId, 200);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<JoinMedsOrgJobDetailsResDTO>> fetchByOrgUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.fetchByUserId(userId));
    }
    @GetMapping("/list")
    public ResponseEntity<List<JoinMedsOrgJobDetailsResDTO>> fetchAll() {
        return ResponseEntity.ok(service.fetchAll());
    }
}
