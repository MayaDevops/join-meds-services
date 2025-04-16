package com.joinmeds.controller;

import com.joinmeds.common.SecureSwaggerController;
import com.joinmeds.contract.SignupRequest;
import com.joinmeds.model.UserLogin;
import com.joinmeds.respository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")

public class UserController implements SecureSwaggerController {
    @Autowired
    private UserLoginRepository userRepo;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        if (!request.password.equals(request.confirmPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }

        if (userRepo.findByEmailMobile(request.emailMobile).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists with this email/mobile.");
        }

        UserLogin user = new UserLogin();
        user.setId(UUID.randomUUID());
        user.setUsername(request.username);
        user.setPassword(request.password); // Consider encoding this!
        user.setEmailMobile(request.emailMobile);
     //   user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepo.save(user);

        return ResponseEntity.ok("User registered successfully.");
    }

    @GetMapping("/login")
    public ResponseEntity<List<UserLogin>> fetchAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }
}
