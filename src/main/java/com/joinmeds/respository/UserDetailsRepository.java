package com.joinmeds.respository;

import com.joinmeds.contract.UserDetailsDTO;
import com.joinmeds.model.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
    Optional<UserDetails> findByUserId(UUID userId);
    Optional<UserDetails> findById(UUID userId);
    List<UserDetails> findAllByUserId(UUID userId);
    List<UserDetails> findByFullnameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

    @Query(value = """
        SELECT u FROM UserDetails u
        WHERE LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR u.userId IN :userIds
    """, countQuery = """
        SELECT COUNT(u) FROM UserDetails u
        WHERE LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR u.userId IN :userIds
    """)
    Page<UserDetails> searchByKeyword(
            @Param("keyword") String keyword,
            @Param("userIds") Collection<UUID> userIds,
            Pageable pageable
    );
}

