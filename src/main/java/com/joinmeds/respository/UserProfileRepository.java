package com.joinmeds.respository;

import com.joinmeds.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserDetails, UUID>  {
    Optional<UserDetails> findByUserId(UUID userId);

}



