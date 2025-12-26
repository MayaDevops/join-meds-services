package com.joinmeds.respository;

import com.joinmeds.model.OtpLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpLogRepository extends JpaRepository<OtpLog, UUID> {
    Optional<OtpLog> findTopByMobileOrderByCreatedAtDesc(String mobile);
}