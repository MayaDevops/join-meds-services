package com.joinmeds.service;


import com.joinmeds.contract.UserDetailsDTO;

import java.util.List;
import java.util.UUID;

public interface UserDetailsService {
    UserDetailsDTO save(UserDetailsDTO dto);
    UserDetailsDTO update(UUID id, UserDetailsDTO dto);
    List<UserDetailsDTO> fetchAll();
    UserDetailsDTO fetchById(UUID id);
}
