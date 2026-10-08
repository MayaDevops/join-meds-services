package com.joinmeds.respository;

import com.joinmeds.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {

    Optional<UserDevice> findByDeviceId(String deviceId);

    List<UserDevice> findByUserIdAndActiveTrue(UUID userId);

    List<UserDevice> findByUserId(UUID userId);

    Optional<UserDevice> findByFcmToken(String fcmToken);
}
