package com.joinmeds.controller;


import com.joinmeds.contract.JoinOrgJobDetailsReqDTO;
import com.joinmeds.contract.JoinOrgJobDetailsResDTO;
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

    @PostMapping("/save/{userId}")
    public ResponseEntity<?> save(@PathVariable UUID userId ,@RequestBody JoinOrgJobDetailsReqDTO dto) {
        try {
            UUID jobId = service.save(userId,dto);
            Response response = new Response("Job application saved successfully", jobId, 200);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/update/{jobId}")
    public ResponseEntity<?> updateUserById(@PathVariable UUID jobId, @RequestBody JoinOrgJobDetailsReqDTO request) {
        try {
            String message = service.update(jobId, request);
            return ResponseEntity.ok(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<JoinOrgJobDetailsResDTO>> fetchByOrgUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.fetchByUserId(userId));
    }
}
