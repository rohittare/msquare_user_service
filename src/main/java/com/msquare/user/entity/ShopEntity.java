package com.msquare.user.entity;

import com.msquare.user.entity.ItemEntity;
import com.msquare.user.entity.AddressEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shopId;

    @Column(nullable = false)
    private String name;

    private Double rating;

    private String tags;

    private boolean isPureVeg;

    private boolean isApproved;

    @Column(columnDefinition = "TEXT")
    private String picture;

    /* Shop Address */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    /* All items of shop */
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<ItemEntity> items;

    /* Today's special items */
    @ManyToMany
    @JoinTable(
        name = "shop_today_special",
        joinColumns = @JoinColumn(name = "shop_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<ItemEntity> todaysSpecialItems;

    /* Weekly menu items */
    @ManyToMany
    @JoinTable(
        name = "shop_weekly_menu",
        joinColumns = @JoinColumn(name = "shop_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<ItemEntity> weeklyMenuItems;
}
