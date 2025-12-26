package com.joinmeds.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinmeds.contract.*;
import com.joinmeds.model.OtpLog;
import com.joinmeds.respository.OtpLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JoinMedsOtpService {

    private final OtpLogRepository otpLogRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${smsbuddy.sendUrl:https://thesmsbuddy.com/api/sms/send}")
    private String sendUrl;

    @Value("${smsbuddy.key}")
    private String key;

    @Value("${smsbuddy.sender:SMSBDY}")
    private String sender;

    @Value("${smsbuddy.type:1}")
    private String type;

    @Value("${smsbuddy.templateId:1707176562443508690}")
    private String templateId;

    @Value("${otp.expiry.minutes:5}")
    private int expiryMinutes;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String TEMPLATE =
            "Your JoinMeds login OTP is {#var#} It is valid for 5 minutes. Do not share this code with anyone.";

    // ---------------- SEND OTP ----------------
    public SendOtpResponse sendOtp(SendOtpRequest request) {

        String mobile = normalizeMobile(request.getMobile()); // will store consistent "91xxxxxxxxxx"
        if (mobile.isBlank()) {
            return SendOtpResponse.builder().success(false).message("mobile is required").mobile(mobile).build();
        }

        String otp = generateOtp(6);
        String message = TEMPLATE.replace("{#var#}", otp);

        OtpLog log = OtpLog.builder()
                .id(UUID.randomUUID())
                .mobile(mobile)
                .otp(otp)
                .message(message)
                .templateId(templateId)
                .provider("SMSBUDDY")
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusMinutes(expiryMinutes))
                .verified(false)
                .success(false)
                .build();

        otpLogRepository.save(log);

        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("key", key);
            form.add("type", type);
            form.add("to", mobile);
            form.add("sender", sender);
            form.add("message", message);
            form.add("template_id", templateId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

            ResponseEntity<String> resp = restTemplate.exchange(sendUrl, HttpMethod.POST, entity, String.class);

            log.setHttpStatus(resp.getStatusCodeValue());
            log.setRawResponse(resp.getBody());

            if (resp.getStatusCodeValue() == 200 && resp.getBody() != null) {
                SmsBuddySendResponse body = objectMapper.readValue(resp.getBody(), SmsBuddySendResponse.class);

                if ("200".equals(body.getStatus())) {
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
                            .httpStatus(log.getHttpStatus())
                            .charges(log.getCharges())
                            .build();
                }
            }

            log.setErrorMessage("SMSBuddy returned non-success response");
            otpLogRepository.save(log);

            return SendOtpResponse.builder()
                    .success(false)
                    .message("SMS send failed")
                    .mobile(mobile)
                    .httpStatus(log.getHttpStatus())
                    .build();

        } catch (HttpStatusCodeException ex) {
            log.setHttpStatus(ex.getStatusCode().value());
            log.setRawResponse(ex.getResponseBodyAsString());
            log.setErrorMessage("SMS send failed: " + ex.getStatusCode());
            otpLogRepository.save(log);

            return SendOtpResponse.builder()
                    .success(false)
                    .message("SMS send failed: " + ex.getStatusCode() + " " + ex.getResponseBodyAsString())
                    .mobile(mobile)
                    .httpStatus(ex.getStatusCode().value())
                    .build();

        } catch (Exception ex) {
            log.setErrorMessage(ex.getMessage());
            otpLogRepository.save(log);

            return SendOtpResponse.builder()
                    .success(false)
                    .message("SMS send failed: " + ex.getMessage())
                    .mobile(mobile)
                    .build();
        }
    }

    // ---------------- VERIFY OTP (LOCAL DB ONLY) ----------------
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {

        String mobile = normalizeMobile(request.getMobile());
        String otp = request.getOtp() == null ? "" : request.getOtp().trim();

        if (mobile.isBlank() || otp.isBlank()) {
            return VerifyOtpResponse.builder()
                    .success(false)
                    .message("mobile and otp are required")
                    .mobile(mobile)
                    .build();
        }

        OtpLog latest = otpLogRepository.findTopByMobileOrderByCreatedAtDesc(mobile).orElse(null);
        if (latest == null) {
            return VerifyOtpResponse.builder()
                    .success(false)
                    .message("No OTP found for this mobile")
                    .mobile(mobile)
                    .build();
        }

        if (Boolean.TRUE.equals(latest.getVerified())) {
            return VerifyOtpResponse.builder()
                    .success(true)
                    .message("OTP already verified")
                    .mobile(mobile)
                    .build();
        }

        if (latest.getExpiresAt() != null && OffsetDateTime.now().isAfter(latest.getExpiresAt())) {
            return VerifyOtpResponse.builder()
                    .success(false)
                    .message("OTP expired. Please request a new OTP.")
                    .mobile(mobile)
                    .build();
        }

        if (!otp.equals(latest.getOtp())) {
            return VerifyOtpResponse.builder()
                    .success(false)
                    .message("Invalid OTP")
                    .mobile(mobile)
                    .build();
        }

        latest.setVerified(true);
        latest.setVerifiedAt(OffsetDateTime.now());
        latest.setProviderStatus("VERIFIED_LOCAL");
        otpLogRepository.save(latest);

        return VerifyOtpResponse.builder()
                .success(true)
                .message("OTP Verified Successfully")
                .mobile(mobile)
                .build();
    }

    // ---------------- HELPERS ----------------
    private String generateOtp(int digits) {
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;
        return String.valueOf(RANDOM.nextInt(max - min + 1) + min);
    }

    // Always store as 91xxxxxxxxxx for consistency
    private String normalizeMobile(String mobile) {
        if (mobile == null) return "";
        String m = mobile.trim().replaceAll("[^0-9]", "");
        if (m.length() == 10) m = "91" + m;
        return m;
    }
}