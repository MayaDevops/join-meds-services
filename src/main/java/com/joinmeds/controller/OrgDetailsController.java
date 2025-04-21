package com.joinmeds.controller;
import com.joinmeds.contract.JoinMedsOrgDetailsRequest;
import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.service.JoinMedsOrgDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/org")
public class OrgDetailsController {

    @Autowired
    private JoinMedsOrgDetailsService service;

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
    public ResponseEntity<List<JoinMedsOrgDetails>> getAll() {
        return ResponseEntity.ok(service.getAllOrgDetails());
    }
}
