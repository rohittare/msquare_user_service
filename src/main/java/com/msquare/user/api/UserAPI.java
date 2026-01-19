package com.msquare.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.msquare.user.model.userDTO;
import com.msquare.user.service.userService;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class UserAPI {
    @Autowired
    protected userService userService;

    @GetMapping("/user/{id}/username")
    public String getUsernameById(@PathVariable Long id) {
        return userService.getUsernameById(id);
    }
    @PostMapping("/user")
    public void saveUser(@RequestBody userDTO userDTO) {
        userService.saveUser(userDTO);
    }
    
}
