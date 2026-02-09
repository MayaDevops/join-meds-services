package com.joinmeds.respository;
import com.joinmeds.model.JobApplied;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JobAppliedRepository extends JpaRepository<JobApplied, UUID> {
    List<JobApplied> findByUserId(UUID userId);

    @Query("""
        SELECT a FROM JobApplied a
        WHERE (:userId IS NULL OR a.userId = :userId)
          AND (:jobId  IS NULL OR a.jobId  = :jobId)
          AND (:orgId  IS NULL OR a.orgId  = :orgId)
          AND (:id     IS NULL OR a.id     = :id)
        ORDER BY a.submittedAt DESC
    """)
    List<JobApplied> search(UUID userId, UUID jobId, UUID orgId, UUID id);
}

