package com.joinmeds.respository;

import com.joinmeds.model.JoinMedsOrgJobDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JoinMedsOrgJobDetailsRepository extends JpaRepository<JoinMedsOrgJobDetails, UUID> {
    List<JoinMedsOrgJobDetails> findByUserId(UUID userId);
    List<JoinMedsOrgJobDetails> findByHiringFor(String hiringFor);
    List<JoinMedsOrgJobDetails> findByHiringForContainingIgnoreCase(String keyword);

}
