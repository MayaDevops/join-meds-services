package com.joinmeds.controller;

import com.joinmeds.contract.NotificationResponse;
import com.joinmeds.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping("/org/{orgId}")
    public ResponseEntity<NotificationResponse.NotificationList> getByOrg(@PathVariable UUID orgId) {
        return ResponseEntity.ok(service.getByOrg(orgId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        service.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/org/{orgId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID orgId) {
        service.markAllAsRead(orgId);
        return ResponseEntity.ok().build();
    }
}