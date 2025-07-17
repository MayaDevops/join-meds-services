package com.joinmeds.service;
import com.joinmeds.model.UserDetails;
import com.joinmeds.respository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public String saveImageForUser(MultipartFile file, UUID userId) throws IOException {
        // Create folder if not exists
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }


        String extension = getFileExtension(file.getOriginalFilename());
        String uniqueFilename = userId.toString() + "." + extension;

        Path filePath = Paths.get(uploadDir, uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        UserDetails user = userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPhotoId(uniqueFilename);
        userDetailsRepository.save(user);

        return uniqueFilename;
    }

    public byte[] loadImage(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        return Files.readAllBytes(filePath);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
