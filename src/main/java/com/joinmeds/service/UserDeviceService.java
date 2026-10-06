package com.joinmeds.service;

import com.joinmeds.contract.DeviceRegisterRequest;
import com.joinmeds.contract.DeviceResponse;
import com.joinmeds.model.UserDevice;
import com.joinmeds.respository.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDeviceService {

    private static final Logger log = LoggerFactory.getLogger(UserDeviceService.class);

    private final UserDeviceRepository userDeviceRepository;


    public DeviceResponse register(DeviceRegisterRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId is required.");
        }
        if (request.getFcmToken() == null || request.getFcmToken().isBlank()) {
            throw new IllegalArgumentException("fcmToken is required.");
        }
        if (request.getDeviceId() == null || request.getDeviceId().isBlank()) {
            throw new IllegalArgumentException("deviceId is required.");
        }

        LocalDateTime now = LocalDateTime.now();

        UserDevice device = userDeviceRepository.findByDeviceId(request.getDeviceId())
                .orElseGet(() -> UserDevice.builder()
                        .deviceId(request.getDeviceId())
                        .createdAt(now)
                        .build());

        device.setUserId(request.getUserId());
        device.setFcmToken(request.getFcmToken());
        device.setPlatform(request.getPlatform());
        device.setActive(true);
        device.setUpdatedAt(now);

        UserDevice saved = userDeviceRepository.save(device);
        log.debug("Registered device {} for user {}", saved.getDeviceId(), saved.getUserId());
        return toResponse(saved);
    }


    public void deactivate(String deviceId) {
        userDeviceRepository.findByDeviceId(deviceId).ifPresent(device -> {
            device.setActive(false);
            device.setUpdatedAt(LocalDateTime.now());
            userDeviceRepository.save(device);
            log.debug("Deactivated device {}", deviceId);
        });
    }

    /** Marks a token FCM has rejected as unreachable, so it is not retried. */
    public void deactivateByToken(String fcmToken) {
        userDeviceRepository.findByFcmToken(fcmToken).ifPresent(device -> {
            device.setActive(false);
            device.setUpdatedAt(LocalDateTime.now());
            userDeviceRepository.save(device);
            log.info("Deactivated device {} — FCM reported its token as no longer registered",
                    device.getDeviceId());
        });
    }

    public List<String> activeTokensForUser(UUID userId) {
        return userDeviceRepository.findByUserIdAndActiveTrue(userId).stream()
                .map(UserDevice::getFcmToken)
                .filter(token -> token != null && !token.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

    public DeviceResponse.DeviceList fetchByUser(UUID userId) {
        List<DeviceResponse> devices = userDeviceRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        long activeCount = devices.stream().filter(d -> Boolean.TRUE.equals(d.getActive())).count();
        return DeviceResponse.DeviceList.builder()
                .activeCount(activeCount)
                .devices(devices)
                .build();
    }

    private DeviceResponse toResponse(UserDevice device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .userId(device.getUserId())
                .deviceId(device.getDeviceId())
                .platform(device.getPlatform())
                .active(device.getActive())
                .createdAt(device.getCreatedAt() != null ? device.getCreatedAt().toString() : null)
                .updatedAt(device.getUpdatedAt() != null ? device.getUpdatedAt().toString() : null)
                .build();
    }
}
