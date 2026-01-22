package com.msquare.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.msquare.user.model.ItemDTO;
import com.msquare.user.service.AdminService;
import com.msquare.user.service.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
public class AdminAPI {
    protected AdminService adminService;

    @PatchMapping("/admin/shops/{shopId}/approve")
    public void approveShop(@PathVariable UUID shopId) {
        adminService.approveShop(shopId);
    }

    @PatchMapping("/admin/shops/{shopId}/disable")
public void disableShop(@PathVariable UUID shopId) {
    adminService.disableShop(shopId);
}

}
