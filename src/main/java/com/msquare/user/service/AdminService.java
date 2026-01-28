package com.msquare.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import com.msquare.user.entity.ShopEntity;
import com.msquare.user.repo.ShopRepo;

@Service
public class AdminService {
    private final ShopRepo shopRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminService(ShopRepo shopRepo, ModelMapper modelMapper) {
        this.shopRepo = shopRepo;
        this.modelMapper = modelMapper;
    }

    public void approveShop(UUID shopId) {

        ShopEntity entity = shopRepo.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        entity.setApproved(true);
        shopRepo.save(entity);
    }
    public void disableShop(UUID shopId) {
    ShopEntity entity = shopRepo.findById(shopId)
            .orElseThrow(() -> new RuntimeException("Shop not found"));

    entity.setApproved(false);
    shopRepo.save(entity);
}
}
