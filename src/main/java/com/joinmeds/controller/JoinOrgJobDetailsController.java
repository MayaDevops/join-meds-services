package com.joinmeds.controller;


import com.joinmeds.contract.JoinOrgJobDetailsDTO;
import com.joinmeds.contract.Response;
import com.joinmeds.service.JoinOrgJobDetailsService;
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
public class JoinOrgJobDetailsController {

    @Autowired
    private final JoinOrgJobDetailsService service;

    @PostMapping("/save")
    public ResponseEntity<?> save(@PathVariable UUID id ,@RequestBody JoinOrgJobDetailsDTO dto) {
        try {
            UUID jobId = service.save(id,dto);
            Response response = new Response("Job application saved successfully", jobId, 200);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/update/{jobId}")
    public ResponseEntity<?> updateUserById(@PathVariable UUID jobId, @RequestBody JoinOrgJobDetailsDTO request) {
        try {
            String message = service.update(jobId, request);
            return ResponseEntity.ok(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<JoinOrgJobDetailsDTO>> fetchByOrgUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.fetchByUserId(userId));
    }
}
