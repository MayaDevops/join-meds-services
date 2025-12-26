package com.joinmeds.controller;

import com.joinmeds.common.SecureSwaggerController;
import com.joinmeds.contract.SendOtpRequest;
import com.joinmeds.contract.SendOtpResponse;
import com.joinmeds.contract.VerifyOtpRequest;
import com.joinmeds.contract.VerifyOtpResponse;
import com.joinmeds.service.JoinMedsOtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class JoinMedsOtpController implements SecureSwaggerController {

    private final JoinMedsOtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<SendOtpResponse> send(@Valid @RequestBody SendOtpRequest request) {
        return ResponseEntity.ok(otpService.sendOtp(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyOtpResponse> verify(@Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(otpService.verifyOtp(request));
    }
}
