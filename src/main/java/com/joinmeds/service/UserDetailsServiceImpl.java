package com.joinmeds.service;

import com.joinmeds.contract.UserDetailsDTO;
import com.joinmeds.model.UserDetails;
import com.joinmeds.respository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        UserDetails entity = repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFullname() != null) entity.setFullname(dto.getFullname());
        if (dto.getAddress() != null) entity.setAddress(dto.getAddress());
        if (dto.getDob() != null) entity.setDob(dto.getDob());
        if (dto.getPassportNumber() != null) entity.setPassportNumber(dto.getPassportNumber());
        if (dto.getPhotoId() != null) entity.setPhotoId(dto.getPhotoId());
        if (dto.getProfession() != null) entity.setProfession(dto.getProfession());
        if (dto.getPhdStatus() != null) entity.setPhdStatus(dto.getPhdStatus());
        if (dto.getPgStatus() != null) entity.setPgStatus(dto.getPgStatus());
        if (dto.getResumeId() != null) entity.setResumeId(dto.getResumeId());
        if (dto.getClinicalNonclinical() != null) entity.setClinicalNonclinical(dto.getClinicalNonclinical());
        if (dto.getAcademicStatus() != null) entity.setAcademicStatus(dto.getAcademicStatus());
        if (dto.getCountryPreffered() != null) entity.setCountryPreffered(dto.getCountryPreffered());
        if (dto.getForeignCountryExp() != null) entity.setForeignCountryExp(dto.getForeignCountryExp());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getForeignTest() != null) entity.setForeignTest(dto.getForeignTest());
        if (dto.getForeignTestDetails() != null) entity.setForeignTestDetails(dto.getForeignTestDetails());
        if (dto.getCertification() != null) entity.setCertification(dto.getCertification());
        if (dto.getAadhaarNo() != null) entity.setAadhaarNo(dto.getAadhaarNo());
        if (dto.getLanguageTestCleared() != null) entity.setLanguageTestCleared(dto.getLanguageTestCleared());

        // Optional: update timestamp
        entity.setCreatedAt(LocalDateTime.now()); // Ensure you have this field in your entity

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

