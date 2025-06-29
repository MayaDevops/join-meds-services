package com.joinmeds.respository;

import com.joinmeds.model.JoinOrgJobDetails;
import com.joinmeds.model.UserDetails;
import com.joinmeds.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JoinOrgJobDetailsRepository extends JpaRepository<JoinOrgJobDetails, UUID> {
    List<JoinOrgJobDetails> findByUserId(UUID userId);
}
