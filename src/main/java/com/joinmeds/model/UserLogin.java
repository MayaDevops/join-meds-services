package com.joinmeds.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "join_meds_user_login")
@Data
@NoArgsConstructor
public class UserLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;
    private String emailMobile;
    private String userType;
    private String orgName;
    private String incorporationNo;
    private String officialEmail;
    private String officePhone;
    private Instant createdAt;
  }

