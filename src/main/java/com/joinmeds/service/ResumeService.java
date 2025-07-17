package com.joinmeds.service;

import com.joinmeds.contract.UserDetailsDTO;
import com.joinmeds.model.UserDetails;
import com.joinmeds.respository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ResumeService {

    private static final String RESUME_UPLOAD_DIR = "uploads/resumes/";

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public String saveResume(MultipartFile file, UUID userId) throws IOException {
        // Validate PDF file
        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        // Create resume file name
        String filename = userId.toString() + ".pdf";

        // Create directory if it does not exist
        Path uploadPath = Paths.get(RESUME_UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the PDF file
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        // Update resume_id in user details
        UserDetails user = userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setResumeId(filename); // resumeId must be a field in your entity
        userDetailsRepository.save(user);

        return filename;
    }
}

