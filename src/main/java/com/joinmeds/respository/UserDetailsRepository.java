package com.joinmeds.respository;

import com.joinmeds.contract.UserDetailsDTO;
import com.joinmeds.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
    Optional<UserDetails> findByUserId(UUID userId);
    Optional<UserDetails> findById(UUID userId);


}

