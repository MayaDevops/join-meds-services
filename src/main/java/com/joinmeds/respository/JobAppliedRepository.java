package com.joinmeds.respository;
import com.joinmeds.model.JobApplied;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface JobAppliedRepository extends JpaRepository<JobApplied, UUID> {
    List<JobApplied> findByUserId(UUID userId);
}
