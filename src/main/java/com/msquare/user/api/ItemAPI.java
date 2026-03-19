package com.msquare.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.msquare.user.model.ItemDTO;
import com.msquare.user.service.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
public class ItemAPI {
    @Autowired
    protected ItemService itemService;

    //done
    @PostMapping("/shop/items/{shopId}")
    public ItemDTO addItem(
            @PathVariable UUID shopId,
            @RequestBody ItemDTO itemDTO) {
        return itemService.addItem(shopId, itemDTO);
    }

    //done
    @PutMapping("/items/{itemId}")
    public ItemDTO updateItem(
            @PathVariable UUID itemId,
            @RequestBody ItemDTO itemDTO) {
        return itemService.updateItem(itemId, itemDTO);
    }

    //done
    @DeleteMapping("/items/{itemId}")
    public void deleteItem(@PathVariable UUID itemId) {
        itemService.deleteItem(itemId);
    }

    //done
    @GetMapping("/shop/{shopId}/today-special")
    public List<ItemDTO> getTodaySpecialItems(@PathVariable UUID shopId) {
        return itemService.getTodaySpecialItems(shopId);
    }

    //done
    @PatchMapping("/items/{itemId}/today-special")
    public void toggleTodaySpecial(
            @PathVariable UUID itemId) {
        itemService.toggleTodaySpecial(itemId);
    }

    //done
    // @GetMapping("/shop/{shopId}/weekly-items")
    // public List<ItemDTO> getAllWeeklyItems(@PathVariable UUID shopId) {
    //     return itemService.getAllWeeklyItems(shopId);
    // }


    //done
    // @PostMapping("/shop/{shopId}/weekly-items/{weekNumber}/{itemId}")
    // public void addWeeklyItem(
    //         @PathVariable UUID shopId,
    //         @PathVariable Integer weekNumber,
    //         @PathVariable UUID itemId) {
    //     itemService.addWeeklyItem(shopId, weekNumber, itemId);
    // }

    // //TODO:
    // @DeleteMapping("/shop/{shopId}/weekly-items/{weekNumber}/{itemId}")
    // public void removeWeeklyItem(
    //         @PathVariable UUID shopId,
    //         @PathVariable Integer weekNumber,
    //         @PathVariable UUID itemId) {
    //     itemService.removeWeeklyItem(shopId, weekNumber, itemId);
    // }

    // //done
    // @GetMapping("/shop/{shopId}/weekly-items/{weekNumber}")
    // public List<ItemDTO> getWeeklyItems(
    //         @PathVariable UUID shopId,
    //         @PathVariable Integer weekNumber) {
    //     return itemService.getWeeklyItems(shopId, weekNumber);
    // }

    //done
    @GetMapping("/shop/items/{shopId}")
    public List<ItemDTO> getItemsByShopId(@PathVariable UUID shopId) {
        return itemService.getItemsByShopId(shopId);
    }

}
