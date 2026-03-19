package com.msquare.user.service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static com.msquare.user.common.CommonUtil.mapList;

import com.msquare.user.repo.AddressRepo;
import com.msquare.user.repo.ShopRepo;
import com.msquare.user.entity.AddressEntity;
import com.msquare.user.model.AddressDTO;

@Service
public class AddressService {
    private final ModelMapper modelMapper;
    private final AddressRepo addressRepo;
    private final ShopRepo shopRepo;

    @Autowired 
    public AddressService(ModelMapper modelMapper , AddressRepo addressRepo, ShopRepo shopRepo){
        this.modelMapper = modelMapper;
        this.addressRepo = addressRepo;
        this.shopRepo = shopRepo;
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

    public void deleteAddress(UUID addressId) {
        if (addressId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address ID is required");
        }
        if (shopRepo.existsByAddress_AddressId(addressId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Address is linked to a shop and cannot be deleted");
        }
        AddressEntity entity = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));
        addressRepo.delete(entity);
    }
}
