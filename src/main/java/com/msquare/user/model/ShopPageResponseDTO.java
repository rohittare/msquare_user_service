package com.msquare.user.model;

import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopPageResponseDTO {
    private List<ItemDTO> todaysSpecial;
    private List<ItemDTO> weeklyMenu;
    private List<ItemDTO> extraItems;
    private List<ItemDTO> regularItems;
}
