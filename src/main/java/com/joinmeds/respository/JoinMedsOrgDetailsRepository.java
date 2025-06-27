package com.joinmeds.respository;


import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JoinMedsOrgDetailsRepository extends JpaRepository<JoinMedsOrgDetails, UUID> {
    Optional<JoinMedsOrgDetails> findById(UUID id);
    Optional<JoinMedsOrgDetails> findByUserId(UUID userId);
}