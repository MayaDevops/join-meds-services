package com.joinmeds.controller;

import com.joinmeds.common.SecureSwaggerController;
import com.joinmeds.contract.LoginRequest;
import com.joinmeds.contract.SignupRequest;
import com.joinmeds.model.UserLogin;
import com.joinmeds.respository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")

public class SignupController implements SecureSwaggerController {
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

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Optional<UserLogin> user = userRepo.findByUsernameAndPassword(
                request.getUsername(),
                request.getPassword()
        );

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get()); // or return a DTO
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
