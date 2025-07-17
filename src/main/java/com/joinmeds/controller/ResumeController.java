package com.joinmeds.controller;

import com.joinmeds.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file") MultipartFile file,
            @PathVariable UUID userId
    ) {
        try {
            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body("Only PDF files are allowed.");
            }

            String filename = resumeService.saveResume(file, userId);
            return ResponseEntity.ok("Resume uploaded successfully: " + filename);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading resume: " + e.getMessage());
        }
    }
}
