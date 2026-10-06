package com.joinmeds.controller;

import com.joinmeds.contract.DeviceRegisterRequest;
import com.joinmeds.contract.DeviceResponse;
import com.joinmeds.service.UserDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class UserDeviceController {

    private final UserDeviceService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DeviceRegisterRequest request) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/unregister/{deviceId}")
    public ResponseEntity<Void> unregister(@PathVariable String deviceId) {
        service.deactivate(deviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fetch/{userId}")
    public ResponseEntity<DeviceResponse.DeviceList> fetch(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.fetchByUser(userId));
    }
}
