package com.joinmeds.controller;


import com.joinmeds.common.SecureSwaggerController;
import com.joinmeds.contract.JoinMedsOrgDetailsRequest;
import com.joinmeds.contract.UserDetailsDTO;
import com.joinmeds.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController implements SecureSwaggerController {

    @Autowired
    private final UserProfileService service;

    @GetMapping("/citizen/details/{userId}")
    public ResponseEntity<UserDetailsDTO> fetchByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.fetchUserDetailsById(userId));
    }

    @GetMapping("/org/details/{userId}")
    public ResponseEntity<JoinMedsOrgDetailsRequest> fetchByOrgUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.fetchOrgDetailsById(userId));
    }


}
