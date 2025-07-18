package com.joinmeds.controller;

import com.joinmeds.common.SecureSwaggerController;
import com.joinmeds.contract.LoginRequest;
import com.joinmeds.contract.Response;
import com.joinmeds.contract.SignupRequest;
import com.joinmeds.contract.SignupResponse;
import com.joinmeds.model.UserLogin;
import com.joinmeds.respository.UserDetailsRepository;
import com.joinmeds.respository.UserLoginRepository;
import com.joinmeds.service.SignupService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class SignupController implements SecureSwaggerController {
    @Autowired
    private UserLoginRepository userRepo;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        try {
            UUID userId = signupService.registerUser(request);
            Response response = new Response("User registered successfully", userId, 200);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/signup-update/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable UUID id, @RequestBody SignupRequest request) {
        try {
            String message = signupService.updateUserById(id, request);
            return ResponseEntity.ok(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/user-fetch/{id}")
        public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        try {
            SignupResponse response = signupService.fetchUserById(id);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
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
