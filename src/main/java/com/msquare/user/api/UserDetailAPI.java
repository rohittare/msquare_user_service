package com.msquare.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.msquare.user.service.UserDetailService;
import com.msquare.user.model.UserDetailDTO;

import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@Slf4j
public class UserDetailAPI {
    @Autowired
    protected UserDetailService userDetailService;

    @PutMapping("/user/update")
    public UserDetailDTO updateUser(@RequestBody UserDetailDTO userDetailDTO) {
        return userDetailService.updateUser(userDetailDTO.getUserId(), userDetailDTO);
    }

    @GetMapping("/user/{id}")
    public UserDetailDTO getUserById(@PathVariable UUID id) {
        return userDetailService.getUserById(id);
    }
    @GetMapping("/users")
    public List<UserDetailDTO> getAllUsers() {
        return userDetailService.getAllUsers();
    }
    

}
