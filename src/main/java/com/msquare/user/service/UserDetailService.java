package com.msquare.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static com.msquare.user.common.CommonUtil.mapList;

import com.msquare.user.repo.UserDetailRepo;
import com.msquare.user.entity.UserDetailEntity;
import com.msquare.user.model.AddressDTO;
import com.msquare.user.model.UserDetailDTO;

@Service
public class UserDetailService {
    private final ModelMapper modelMapper;
    private final UserDetailRepo userDetailRepo;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailService(
            ModelMapper modelMapper,
            UserDetailRepo userDetailRepo,
            AddressService addressService,
            PasswordEncoder passwordEncoder
    ) {
        this.modelMapper = modelMapper;
        this.userDetailRepo = userDetailRepo;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetailDTO saveUser(UserDetailDTO userDetailDTO) {
        var entity = modelMapper.map(userDetailDTO, UserDetailEntity.class);
        if (entity.getPassword() != null && !entity.getPassword().isBlank()) {
            if (!entity.getPassword().startsWith("$2a$")
                    && !entity.getPassword().startsWith("$2b$")
                    && !entity.getPassword().startsWith("$2y$")) {
                entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            }
        }
        if (entity.getRole() == null || entity.getRole().isBlank()) {
            entity.setRole("USER");
        }
        if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(LocalDateTime.now());
        }
        if (userDetailDTO.getActive() == null) {
            entity.setActive(true);
        }
        var saved = userDetailRepo.save(entity);
        return sanitize(modelMapper.map(saved, UserDetailDTO.class));
    }

    public UserDetailDTO getUserById(UUID id) {
        var entity = userDetailRepo.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        List<AddressDTO> addresses = addressService.getAddressesByUserId(id);
        UserDetailDTO dto = sanitize(modelMapper.map(entity, UserDetailDTO.class));
        dto.setAddresses(addresses);
        return dto;
    }

    public List<UserDetailDTO> getAllUsers() {
        List<UserDetailEntity> entities = userDetailRepo.findAll();
        if (!entities.isEmpty()) {
            List<UserDetailDTO> dtos = mapList(entities, UserDetailDTO.class);
            dtos.forEach(this::sanitize);
            return dtos;
        }
        return Collections.emptyList();

    }

    private UserDetailDTO sanitize(UserDetailDTO dto) {
        if (dto == null) {
            return null;
        }
        dto.setPassword(null);
        return dto;
    }

}
