package com.joinmeds.service;

import com.joinmeds.contract.*;
import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.model.UserDetails;
import com.joinmeds.model.UserLogin;
import com.joinmeds.respository.JoinMedsOrgDetailsRepository;
import com.joinmeds.respository.UserDetailsRepository;
import com.joinmeds.respository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserLoginRepository userLoginRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final JoinMedsOrgDetailsRepository joinMedsOrgDetailsRepository;
    private final PasswordEncoder passwordEncoder;   // ✅ FIXED

    // ---------------- SIGNUP ----------------
    public UUID registerUser(SignupRequest request) {

        if (!request.password.equals(request.confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        if (userLoginRepository.findByEmailMobile(request.emailMobile).isPresent()) {
            throw new IllegalArgumentException("User already exists with this email/mobile.");
        }

        UserLogin userLogin = new UserLogin();
        userLogin.setPassword(passwordEncoder.encode(request.password)); // ✅ HASHED
        userLogin.setEmailMobile(request.emailMobile);
        userLogin.setOfficePhone(request.officialPhone);
        userLogin.setOrgName(request.orgName);
        userLogin.setOfficialEmail(request.officialEmail);
        userLogin.setIncorporationNo(request.incorporationNo);
        userLogin.setUserType(request.userType);
        userLogin.setCreatedAt(String.valueOf(Instant.now()));
        userLogin.setUsername(request.emailMobile);

        userLoginRepository.save(userLogin);

        if ("ORGANIZATION".equals(request.userType)) {
            JoinMedsOrgDetails orgDetails = new JoinMedsOrgDetails();
            orgDetails.setUserId(userLogin.getId());
            joinMedsOrgDetailsRepository.save(orgDetails);
        } else {
            UserDetails userDetails = new UserDetails();
            userDetails.setUserId(userLogin.getId());
            userDetailsRepository.save(userDetails);
        }

        return userLogin.getId();
    }

    // ---------------- UPDATE ----------------
    public String updateUserById(UUID id, SignupRequest request) {
        UserLogin userLogin = userLoginRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        if (request.emailMobile != null) userLogin.setEmailMobile(request.emailMobile);
        if (request.password != null)
            userLogin.setPassword(passwordEncoder.encode(request.password)); // ✅ HASHED
        if (request.officialPhone != null) userLogin.setOfficePhone(request.officialPhone);
        if (request.orgName != null) userLogin.setOrgName(request.orgName);
        if (request.officialEmail != null) userLogin.setOfficialEmail(request.officialEmail);
        if (request.incorporationNo != null) userLogin.setIncorporationNo(request.incorporationNo);

        userLoginRepository.save(userLogin);
        return "User updated successfully.";
    }

    // ---------------- FETCH ----------------
    public SignupResponse fetchUserById(UUID id) {
        UserLogin userLogin = userLoginRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        SignupResponse response = new SignupResponse();
        response.setId(userLogin.getId());
        response.setEmailMobile(userLogin.getEmailMobile());
        response.setOfficialPhone(userLogin.getOfficePhone());
        response.setOrgName(userLogin.getOrgName());
        response.setOfficialEmail(userLogin.getOfficialEmail());
        response.setIncorporationNo(userLogin.getIncorporationNo());
        response.setCreatedAt(userLogin.getCreatedAt());
        response.setUserType(userLogin.getUserType());
        response.setUsername(userLogin.getUsername());

        return response;
    }

    // ---------------- RESET PASSWORD ----------------
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        String mobile = request.getMobileNumber() == null ? "" : request.getMobileNumber().trim();
        if (mobile.isEmpty()) {
            return ResetPasswordResponse.builder()
                    .success(false)
                    .message("Mobile number is required")
                    .build();
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResetPasswordResponse.builder()
                    .success(false)
                    .message("New password and confirm password do not match")
                    .build();
        }

        UserLogin user = userLoginRepository.findByEmailMobile(mobile).orElse(null);

        if (user == null) {
            return ResetPasswordResponse.builder()
                    .success(false)
                    .message("User not found for the given mobile number")
                    .build();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword())); // ✅ HASHED
        userLoginRepository.save(user);

        return ResetPasswordResponse.builder()
                .success(true)
                .message("Password reset successfully")
                .build();
    }
}