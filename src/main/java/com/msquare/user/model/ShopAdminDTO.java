package com.msquare.user.model;

import com.msquare.user.model.ItemDTO;
import com.msquare.user.model.AddressDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopAdminDTO {

    private UUID shopId;
    private String name;
    private Double rating;
    private String tags;
    private boolean isPureVeg;
    private boolean isApproved;
    private String picture;

    private AddressDTO address;

    private List<ItemDTO> items;
}
