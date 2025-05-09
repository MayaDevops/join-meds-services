package com.joinmeds.service;

import com.joinmeds.contract.SignupRequest;
import com.joinmeds.contract.SignupResponse;
import com.joinmeds.model.UserDetails;
import com.joinmeds.model.UserLogin;
import com.joinmeds.respository.UserDetailsRepository;
import com.joinmeds.respository.UserLoginRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Data
@NoArgsConstructor
public class SignupService {
    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public String registerUser(SignupRequest request) {
        if (!request.password.equals(request.confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        if (userLoginRepository.findByEmailMobile(request.emailMobile).isPresent()) {
            throw new IllegalArgumentException("User already exists with this email/mobile.");
        }

        UserLogin userLogin = new UserLogin();
        userLogin.setPassword(request.password);
        userLogin.setEmailMobile(request.emailMobile);
        userLogin.setOfficePhone(request.officialPhone);
        userLogin.setOrgName(request.orgName);
        userLogin.setOfficialEmail(request.officialEmail);
        userLogin.setIncorporationNo(request.incorporationNo);
        userLogin.setUserType(request.userType);
        userLogin.setCreatedAt(Instant.now());
        userLogin.setUsername(request.emailMobile);

        userLoginRepository.save(userLogin);



        return "User registered successfully.";
    }

    public String updateUserById(UUID id, SignupRequest request) {
        UserLogin userLogin = userLoginRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        if (request.emailMobile != null) userLogin.setEmailMobile(request.emailMobile);
        if (request.password != null) userLogin.setPassword(request.password);
        if (request.officialPhone != null) userLogin.setOfficePhone(request.officialPhone);
        if (request.orgName != null) userLogin.setOrgName(request.orgName);
        if (request.officialEmail != null) userLogin.setOfficialEmail(request.officialEmail);
        if (request.incorporationNo != null) userLogin.setIncorporationNo(request.incorporationNo);
        userLoginRepository.save(userLogin);
        userLoginRepository.findById(id).ifPresent(details -> {

            userLoginRepository.save(details);
        });

        return "User updated successfully.";
    }



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

        return response;
    }


}
