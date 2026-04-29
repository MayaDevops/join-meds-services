package com.joinmeds.service;


import com.joinmeds.contract.UserDetailsDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserDetailsService {
    UserDetailsDTO save(UserDetailsDTO dto);
    UserDetailsDTO update(UUID id, UserDetailsDTO dto);
    List<UserDetailsDTO> fetchAll();
    Page<UserDetailsDTO> fetchAll(int page, int size);
    Page<UserDetailsDTO> fetchAll(String keyword, int page, int size);
    UserDetailsDTO fetchById(UUID id);
}
