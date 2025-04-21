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

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDetailsDTO> update(@PathVariable UUID id, @RequestBody UserDetailsDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> fetchById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.fetchById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDetailsDTO>> fetchAll() {
        return ResponseEntity.ok(service.fetchAll());
    }
}
