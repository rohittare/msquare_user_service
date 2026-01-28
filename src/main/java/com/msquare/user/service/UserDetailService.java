package com.msquare.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static com.msquare.user.common.CommonUtil.mapList;

import com.msquare.user.repo.UserDetailRepo;
import com.msquare.user.repo.AddressRepo;
import com.msquare.user.entity.AddressEntity;
import com.msquare.user.entity.UserDetailEntity;
import com.msquare.user.model.AddressDTO;
import com.msquare.user.model.UserDetailDTO;

@Service
public class UserDetailService {
    private final ModelMapper modelMapper;
    private final UserDetailRepo userDetailRepo;
    private final AddressService addressService;

    @Autowired
    public UserDetailService(ModelMapper modelMapper, UserDetailRepo userDetailRepo , AddressService addressService , AddressRepo addressRepo) {
        this.modelMapper = modelMapper;
        this.userDetailRepo = userDetailRepo;
        this.addressService = addressService;
    }

    public UserDetailDTO saveUser(UserDetailDTO userDetailDTO) {
        var entity = modelMapper.map(userDetailDTO, UserDetailEntity.class);
        var saved = userDetailRepo.save(entity);
        return modelMapper.map(saved, UserDetailDTO.class);
    }

    public UserDetailDTO getUserById(UUID id) {
        var entity = userDetailRepo.findById(id).orElse(null);
        List<AddressDTO> addresses = addressService.getAddressesByUserId(id);
        UserDetailDTO dto = modelMapper.map(entity, UserDetailDTO.class);
        dto.setAddresses(addresses);
        return dto;
    }

    public List<UserDetailDTO> getAllUsers() {
        List<UserDetailEntity> entities = userDetailRepo.findAll();
        if (!entities.isEmpty()) {
            return mapList(entities, UserDetailDTO.class);
        }
        return Collections.emptyList();

    }

}
