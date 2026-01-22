package com.msquare.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static com.msquare.user.common.CommonUtil.mapList;

import com.msquare.user.repo.ItemRepo;
import com.msquare.user.repo.WeeklyItemRepo;
import com.msquare.user.entity.ItemEntity;
import com.msquare.user.entity.WeeklyItemEntity;
import com.msquare.user.model.ItemDTO;

@Service
public class ItemService {
    private final ModelMapper modelMapper;
    private final ItemRepo itemRepo;
    private final WeeklyItemRepo weeklyItemRepo;

    @Autowired
    public ItemService(ModelMapper modelMapper, ItemRepo itemRepo, WeeklyItemRepo weeklyItemRepo) {
        this.modelMapper = modelMapper;
        this.itemRepo = itemRepo;
        this.weeklyItemRepo = weeklyItemRepo;
    }

    public ItemDTO addItem(UUID shopId, ItemDTO itemDTO) {
        ItemEntity entity = modelMapper.map(itemDTO, ItemEntity.class);
        entity.setShopId(shopId);
        ItemEntity saved = itemRepo.save(entity);
        return modelMapper.map(saved, ItemDTO.class);
    }

    public ItemDTO updateItem(UUID itemId, ItemDTO itemDTO) {
        ItemEntity entity = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if(itemDTO.getName() != null){
            entity.setName(itemDTO.getName());
        }
        if(itemDTO.getDescription() != null){
            entity.setDescription(itemDTO.getDescription());
        }
        if(itemDTO.getPrice() != null){
            entity.setPrice(itemDTO.getPrice());
        }
        if(itemDTO.getCategory() != null){
            entity.setCategory(itemDTO.getCategory());
        }
        if(itemDTO.getVeg() != null){
            entity.setVeg(itemDTO.getVeg());
        }
        if(itemDTO.getAvailable() != null){
            entity.setAvailable(itemDTO.getAvailable());
        }
        if(itemDTO.getSpecial() != null){
            entity.setSpecial(itemDTO.getSpecial());
        }

        ItemEntity updated = itemRepo.save(entity);
        return modelMapper.map(updated, ItemDTO.class);
    }

    public void deleteItem(UUID itemId) {
        ItemEntity entity = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
                itemRepo.delete(entity);
    }

    public List<ItemDTO> getItemsByShopId(UUID shopId) {
        List<ItemEntity> entities = itemRepo.findByShopId(shopId);
        if (!entities.isEmpty()) {
            return mapList(entities, ItemDTO.class);
        }
        return Collections.emptyList();
    }

    public List<ItemDTO> getTodaySpecialItems(UUID shopId) {
        List<ItemEntity> entities = itemRepo.findByShopIdAndSpecialTrue(shopId);
        if (!entities.isEmpty()) {
            return mapList(entities, ItemDTO.class);
        }
        return Collections.emptyList();

    }

    public void toggleTodaySpecial(UUID itemId) {
        ItemEntity entity = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        entity.setSpecial(!entity.isSpecial());
        itemRepo.save(entity);
    }

    public List<ItemDTO> getAllWeeklyItems(UUID shopId) {
        List<ItemEntity> entities = itemRepo.findAllWeeklyItems(shopId);
        if (!entities.isEmpty()) {
            return mapList(entities, ItemDTO.class);
        }
        return Collections.emptyList();
    }

    public List<ItemDTO> getWeeklyItems(UUID shopId, Integer weekNumber) {
        List<ItemEntity> entities = itemRepo.findWeeklyItems(shopId, weekNumber);
        if (!entities.isEmpty()) {
            return mapList(entities, ItemDTO.class);
        }
        return Collections.emptyList();
    }

    public void addWeeklyItem(UUID shopId, Integer weekNumber, UUID itemId) {

        weeklyItemRepo.findByShopIdAndItemIdAndWeekNumber(shopId, itemId, weekNumber)
                .ifPresent(w -> {
                    throw new RuntimeException("Item already added for this week");
                });

        WeeklyItemEntity entity = new WeeklyItemEntity();
        entity.setShopId(shopId);
        entity.setItemId(itemId);
        entity.setWeekNumber(weekNumber);

        weeklyItemRepo.save(entity);
    }

    public void removeWeeklyItem(UUID shopId, Integer weekNumber, UUID itemId) {
        weeklyItemRepo.deleteByShopIdAndItemIdAndWeekNumber(shopId, itemId, weekNumber);
    }

}
