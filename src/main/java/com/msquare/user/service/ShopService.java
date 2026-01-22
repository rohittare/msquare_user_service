package com.msquare.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static com.msquare.user.common.CommonUtil.mapList;

import com.msquare.user.repo.ShopRepo;
import com.msquare.user.entity.ShopEntity;
import com.msquare.user.model.ShopDTO;

@Service
public class ShopService {
    private final ModelMapper modelMapper;
    private final ShopRepo shopRepo;

    @Autowired
    public ShopService(ModelMapper modelMapper, ShopRepo shopRepo) {
        this.modelMapper = modelMapper;
        this.shopRepo = shopRepo;
    }

    public ShopDTO createShop(ShopDTO shopDTO) {
        ShopEntity entity = modelMapper.map(shopDTO, ShopEntity.class);
        entity.setApproved(false);
        entity.setRating(0.0);
        ShopEntity saved = shopRepo.save(entity);
        return modelMapper.map(saved, ShopDTO.class);
    }

    public ShopDTO getShopById(UUID shopId) {
        ShopEntity entity = shopRepo.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        return modelMapper.map(entity, ShopDTO.class);
    }

    public ShopDTO updateShop(UUID shopId, ShopDTO shopDTO) {

        ShopEntity entity = shopRepo.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (shopDTO.getName() != null) {
            entity.setName(shopDTO.getName());
        }
        if (shopDTO.getTags() != null) {
            entity.setTags(shopDTO.getTags());
        }
        if (shopDTO.getPicture() != null) {
            entity.setPicture(shopDTO.getPicture());
        }
        if (shopDTO.getAddressId() != null) {
            entity.setAddressId(shopDTO.getAddressId());
        }

        if(shopDTO.getPureVeg() != null) {
            entity.setPureVeg(shopDTO.getPureVeg());
        }
        ShopEntity updated = shopRepo.save(entity);
        return modelMapper.map(updated, ShopDTO.class);
    }

    public void deleteShop(UUID shopId) {
        ShopEntity entity = shopRepo.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        entity.setApproved(false);
        shopRepo.save(entity);
    }

    public List<ShopDTO> getApprovedShops() {
        List<ShopEntity> entities = shopRepo.findByIsApprovedTrue();
        if (!entities.isEmpty()) {
            return mapList(entities, ShopDTO.class);
        }
        return Collections.emptyList();
    }

}
