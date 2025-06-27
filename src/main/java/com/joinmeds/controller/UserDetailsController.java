package com.joinmeds.controller;
import com.joinmeds.contract.UserDetailsDTO;
import com.joinmeds.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-details")
@RequiredArgsConstructor
public class UserDetailsController {

    private final UserDetailsService service;

    @PostMapping("/save")
    public ResponseEntity<UserDetailsDTO> save(@RequestBody UserDetailsDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDetailsDTO> update(@RequestParam UUID userId, @RequestBody UserDetailsDTO dto) {
        return ResponseEntity.ok(service.update(userId, dto));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDTO> fetchById(@RequestParam UUID userId) {
        return ResponseEntity.ok(service.fetchById(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDetailsDTO>> fetchAll() {
        return ResponseEntity.ok(service.fetchAll());
    }
}
