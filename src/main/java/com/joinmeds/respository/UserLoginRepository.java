package com.joinmeds.respository;

import com.joinmeds.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserLoginRepository extends JpaRepository<UserLogin, UUID> {
    Optional<UserLogin> findByEmailMobile(String emailMobile);
    Optional<UserLogin> findByUsernameAndPassword(String username, String password);
    Optional<UserLogin> findByUsername(String username);
    List<UserLogin> findByEmailMobileContainingIgnoreCase(String keyword);
    List<UserLogin> findByUserTypeAndOrgNameContainingIgnoreCaseOrUserTypeAndEmailMobileContainingIgnoreCaseOrUserTypeAndOfficialEmailContainingIgnoreCase(
            String type1, String orgName,
            String type2, String emailMobile,
            String type3, String officialEmail
    );
}
