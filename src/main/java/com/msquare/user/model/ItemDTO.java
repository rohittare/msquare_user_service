package com.msquare.user.model;

import lombok.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private UUID itemId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Boolean veg;
    private Boolean available;
    private Boolean special;
    // private String imageUrl;
}
