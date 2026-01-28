package com.msquare.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.msquare.user.service.UserDetailService;
import com.msquare.user.service.AdminService;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@CrossOrigin
@Slf4j
public class AdminAPI {
    @Autowired
    protected AdminService adminService;
    protected UserDetailService userDetailService;

    @PatchMapping("/admin/shops/{shopId}/approve")
    public void approveShop(@PathVariable UUID shopId) {
        adminService.approveShop(shopId);
    }

    @PatchMapping("/admin/shops/{shopId}/disable")
    public void disableShop(@PathVariable UUID shopId) {
        adminService.disableShop(shopId);
    }



}
