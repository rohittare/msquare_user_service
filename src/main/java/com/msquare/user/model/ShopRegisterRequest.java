package com.msquare.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String description;
    private String tags;
    private Boolean pureVeg;
}
