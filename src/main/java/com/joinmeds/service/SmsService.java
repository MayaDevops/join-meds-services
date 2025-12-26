package com.joinmeds.sms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinmeds.contract.SendOtpRequest;
import com.joinmeds.contract.SendOtpResponse;
import com.joinmeds.contract.SmsBuddySendResponse;
import com.joinmeds.model.OtpLog;
import com.joinmeds.respository.OtpLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final OtpLogRepository otpLogRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${smsbuddy.url:https://thesmsbuddy.com/api/sms/send}")
    private String smsBuddyUrl;

    @Value("${smsbuddy.key}")
    private String smsBuddyKey;

    @Value("${smsbuddy.sender:SMSBDY}")
    private String sender;

    @Value("${smsbuddy.type:1}")
    private String type;

    // ✅ backend fixed template id
    @Value("${smsbuddy.templateId}")
    private String templateId;

    @Value("${otp.expiry.minutes:5}")
    private int otpExpiryMinutes;

    private static final SecureRandom RANDOM = new SecureRandom();

    public SendOtpResponse sendOtp(SendOtpRequest request) {

        String mobile = normalizeMobile(request.getMobile());
        if (mobile.isBlank()) {
            return SendOtpResponse.builder()
                    .success(false)
                    .message("Mobile is required")
                    .mobile(mobile)
                    .build();
        }

        String otp = generateOtp(6);

        // ✅ backend fixed message format
        String message = "Your OTP is " + otp + ". Valid for " + otpExpiryMinutes + " minutes.";

        OtpLog log = OtpLog.builder()
                .id(UUID.randomUUID())
                .mobile(mobile)
                .otp(otp)
                .message(message)
                .templateId(templateId)
                .provider("SMSBUDDY")
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusMinutes(otpExpiryMinutes))
                .verified(false)
                .success(false)
                .build();

        otpLogRepository.save(log);

        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("key", smsBuddyKey);
            form.add("type", type);
            form.add("to", mobile);
            form.add("sender", sender);
            form.add("message", message);
            form.add("template_id", templateId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

            ResponseEntity<SmsBuddySendResponse> response =
                    restTemplate.exchange(smsBuddyUrl, HttpMethod.POST, entity, SmsBuddySendResponse.class);

            int httpStatus = response.getStatusCodeValue();
            SmsBuddySendResponse body = response.getBody();

            log.setHttpStatus(httpStatus);
            log.setRawResponse(objectMapper.writeValueAsString(body));

            if (body != null && "200".equals(body.getStatus())) {
                log.setSuccess(true);
                log.setCharges(body.getCharges());

                if (body.getData() != null && !body.getData().isEmpty()) {
                    SmsBuddySendResponse.SmsBuddyData d = body.getData().get(0);
                    log.setProviderMessageId(d.getId());
                    log.setProviderStatus(d.getStatus());
                    log.setUnit(d.getUnit());
                    log.setSmsLength(d.getLength());
                }

                otpLogRepository.save(log);

                return SendOtpResponse.builder()
                        .success(true)
                        .message(body.getMessage())
                        .mobile(mobile)
                        .providerMessageId(log.getProviderMessageId())
                        .providerStatus(log.getProviderStatus())
                        .httpStatus(httpStatus)
                        .charges(log.getCharges())
                        .build();
            }

            String err = (body != null) ? body.getMessage() : "Unknown SMS provider error";
            log.setErrorMessage(err);
            otpLogRepository.save(log);

            return SendOtpResponse.builder()
                    .success(false)
                    .message(err)
                    .mobile(mobile)
                    .httpStatus(httpStatus)
                    .build();

        } catch (Exception ex) {
            log.setErrorMessage(ex.getMessage());
            otpLogRepository.save(log);

            return SendOtpResponse.builder()
                    .success(false)
                    .message("SMS failed: " + ex.getMessage())
                    .mobile(mobile)
                    .build();
        }
    }

    private String generateOtp(int digits) {
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;
        return String.valueOf(RANDOM.nextInt(max - min + 1) + min);
    }

    private String normalizeMobile(String mobile) {
        if (mobile == null) return "";
        String m = mobile.trim().replaceAll("[^0-9]", "");
        return m;
    }
}