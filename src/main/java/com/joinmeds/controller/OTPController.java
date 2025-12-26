package com.joinmeds.controller;

import com.joinmeds.common.SecureSwaggerController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class OTPController implements SecureSwaggerController {
}
