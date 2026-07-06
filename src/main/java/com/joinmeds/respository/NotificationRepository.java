package com.joinmeds.respository;

import com.joinmeds.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByOrgIdOrderByCreatedAtDesc(UUID orgId);

    long countByOrgIdAndReadFalse(UUID orgId);
}