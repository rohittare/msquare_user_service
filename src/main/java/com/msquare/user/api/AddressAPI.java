package com.msquare.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.msquare.user.service.AddressService;
import com.msquare.user.model.AddressDTO;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin
@Slf4j
public class AddressAPI {
    @Autowired
    protected AddressService addressService;
    
    @GetMapping("/user/address/{userId}")
    public List<AddressDTO> getAddressByUserId(@PathVariable UUID userId) {
        return addressService.getAddressesByUserId(userId);
    }

    @PostMapping("/user/address")
    public AddressDTO saveAddress(@RequestBody AddressDTO addressDTO) {
        return addressService.saveAddress(addressDTO);        
    }
    
    
}
