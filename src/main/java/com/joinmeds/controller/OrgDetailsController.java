package com.joinmeds.controller;
import com.joinmeds.contract.JoinMedsOrgDetailsRequest;
import com.joinmeds.contract.LoginRequest;
import com.joinmeds.contract.LoginResponse;
import com.joinmeds.contract.OrgListResponse;
import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.model.UserLogin;
import com.joinmeds.respository.JoinMedsOrgDetailsRepository;
import com.joinmeds.respository.UserLoginRepository;
import com.joinmeds.service.JoinMedsOrgDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/org")
public class OrgDetailsController {

    @Autowired
    private JoinMedsOrgDetailsService service;

    @Autowired
    private UserLoginRepository userRepo;

    @Autowired
    private JoinMedsOrgDetailsRepository orgDetailsRepository;

    @PostMapping("/login")
    public ResponseEntity<?> loginOrganization(@RequestBody LoginRequest request) {
        Optional<UserLogin> user = userRepo.findByUsernameAndPassword(
                request.getUsername(),
                request.getPassword()
        );

        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

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
    }

    @PostMapping("/save")
    public ResponseEntity<JoinMedsOrgDetails> save(@RequestBody JoinMedsOrgDetailsRequest request) {
        return ResponseEntity.ok(service.saveOrgDetails(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<JoinMedsOrgDetails> update(@PathVariable UUID id, @RequestBody JoinMedsOrgDetailsRequest request) {
        return ResponseEntity.ok(service.updateOrgDetails(id, request));
    }
    @GetMapping("/fetch/{id}")
    public ResponseEntity<JoinMedsOrgDetails> fetchById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.fetchById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrgListResponse>> getAll(
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(service.getAllOrgDetails(keyword));
    }
}
