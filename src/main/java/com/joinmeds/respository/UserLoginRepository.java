package com.joinmeds.respository;

import com.joinmeds.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserLoginRepository extends JpaRepository<UserLogin, UUID> {
    Optional<UserLogin> findByEmailMobile(String emailMobile);
    Optional<UserLogin> findByUsernameAndPassword(String username,String password);
    Optional<UserLogin> findByUsername(String username);

}
