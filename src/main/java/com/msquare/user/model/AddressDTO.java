package com.msquare.user.model;

import lombok.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private UUID addressId;
    private String type;       // HOME, WORK
    private String area;
    private String city;
    private String pincode;
    private String fullAddress;
}
