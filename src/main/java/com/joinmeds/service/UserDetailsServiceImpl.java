package com.joinmeds.service;

import com.joinmeds.contract.UserDetailsDTO;
import com.joinmeds.model.UserDetails;
import com.joinmeds.respository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository repo;

    @Override
    public UserDetailsDTO save(UserDetailsDTO dto) {
        UserDetails entity = new UserDetails();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreatedAt(LocalDateTime.now());
        return mapToDTO(repo.save(entity));
    }

    @Override
    public UserDetailsDTO update(UUID userId, UserDetailsDTO dto) {
        UserDetails entity = repo.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        BeanUtils.copyProperties(dto, entity, "userId", "createdAt");
        return mapToDTO(repo.save(entity));
    }

    public UserDetailsDTO fetchById(UUID userId) {
        UserDetails entity = repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return mapToDTO(entity);
    }

    @Override
    public List<UserDetailsDTO> fetchAll() {
        return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private UserDetailsDTO mapToDTO(UserDetails entity) {
        UserDetailsDTO dto = new UserDetailsDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

}

