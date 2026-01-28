package com.msquare.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msquare.user.service.ShopService;
import com.msquare.user.model.ShopDTO;

import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@Slf4j
public class ShopAPI {
    @Autowired
    protected ShopService shopService;

    // done
    @PostMapping("/shops")
    public ShopDTO createShop(@RequestBody ShopDTO shopDTO) {
        return shopService.createShop(shopDTO);
    }

    // done
    @GetMapping("/shops/{shopId}")
    public ShopDTO getShopById(@PathVariable UUID shopId) {
        return shopService.getShopById(shopId);
    }

    // done    
    @PutMapping("/shops/{shopId}")
    public ShopDTO updateShop(
            @PathVariable UUID shopId,
            @RequestBody ShopDTO shopDTO) {
        return shopService.updateShop(shopId, shopDTO);
    }

    // done
    @DeleteMapping("/shops/{shopId}")
    public void deleteShop(@PathVariable UUID shopId) {
        shopService.deleteShop(shopId);
    }

    // done
    @GetMapping("/public/shops")
    public List<ShopDTO> getApprovedShops() {
        return shopService.getApprovedShops();
    }

}
