package com.joinmeds.controller;

import com.joinmeds.common.SecureSwaggerController;
import com.joinmeds.contract.*;
import com.joinmeds.model.UserLogin;
import com.joinmeds.respository.JoinMedsOrgDetailsRepository;
import com.joinmeds.respository.UserDetailsRepository;
import com.joinmeds.respository.UserLoginRepository;
import com.joinmeds.service.SignupService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private JoinMedsOrgDetailsRepository orgDetailsRepository;

    @Autowired
    private SignupService signupService;
    private final PasswordEncoder passwordEncoder;

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
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
//
//        Optional<UserLogin> userOpt = userRepo.findByUsername(request.getUsername());
//
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(401).body("Invalid username or password");
//        }
//
//        UserLogin user = userOpt.get();
//
//        // ✅ Compare raw password with stored bcrypt hash
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(401).body("Invalid username or password");
//        }
//
//        return ResponseEntity.ok(user); // better: return DTO
//    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Optional<UserLogin> user = userRepo.findByUsernameAndPassword(
                request.getUsername(),
                request.getPassword()
        );

        if (user.isPresent()) {
            UserLogin u = user.get();

            if (!"ORGANIZATION".equals(u.getUserType()) && !"SUPERADMIN".equals(u.getUserType())) {
                return ResponseEntity.status(401).body("Organization account does not exist");
            }

            UUID orgId = orgDetailsRepository.findByUserId(u.getId())
                    .map(org -> org.getId())
                    .orElse(null);
            LoginResponse response = LoginResponse.builder()
                    .id(u.getId())
                    .username(u.getUsername())
                    .emailMobile(u.getEmailMobile())
                    .userType(u.getUserType())
                    .orgName(u.getOrgName())
                    .incorporationNo(u.getIncorporationNo())
                    .officialEmail(u.getOfficialEmail())
                    .officePhone(u.getOfficePhone())
                    .createdAt(u.getCreatedAt())
                    .orgId(orgId)
                    .build();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ResetPasswordResponse response = signupService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
