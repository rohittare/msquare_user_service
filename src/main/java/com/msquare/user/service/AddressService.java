package com.msquare.user.service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static com.msquare.user.common.CommonUtil.mapList;

import com.msquare.user.repo.AddressRepo;
import com.msquare.user.entity.AddressEntity;
import com.msquare.user.model.AddressDTO;

@Service
public class AddressService {
    private final ModelMapper modelMapper;
    private final AddressRepo addressRepo;

    @Autowired 
    public AddressService(ModelMapper modelMapper , AddressRepo addressRepo){
        this.modelMapper = modelMapper;
        this.addressRepo = addressRepo;
    }

    public List<AddressDTO> getAddressesByUserId(UUID userId){
        List<AddressEntity> entities = addressRepo.findByUserId(userId);
        if(!entities.isEmpty()){
            return mapList(entities, AddressDTO.class) ;
        }
        return Collections.emptyList();
    }

    public AddressDTO getAddressById(UUID addressId){
        AddressEntity entity = addressRepo.findById(addressId).orElse(null);
        if(entity != null){
            return modelMapper.map(entity , AddressDTO.class);
        }
        return null;
    }

    public AddressDTO saveAddress(AddressDTO dto){
        if(dto == null){
            return null;
        }
        return modelMapper.map(addressRepo.save(modelMapper.map(dto, AddressEntity.class)) , AddressDTO.class);
    }

    public AddressDTO updateAddress(UUID addressId , AddressDTO dto){
        AddressEntity entity = addressRepo.findById(addressId).orElseThrow(() -> new RuntimeException("Address not found"));
        if(dto.getArea() != null){
            entity.setArea(dto.getArea());
        }
        if(dto.getCity() != null){
            entity.setCity(dto.getCity());
        }
        if(dto.getFullAddress() != null){
            entity.setFullAddress(dto.getFullAddress());
        }
        if(dto.getPincode() != null){
            entity.setPincode(dto.getPincode());
        }
        AddressEntity updated = addressRepo.save(entity);
        return modelMapper.map(updated , AddressDTO.class); 
    }
}
