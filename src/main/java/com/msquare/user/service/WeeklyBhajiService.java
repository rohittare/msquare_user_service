package com.msquare.user.service;

import com.msquare.user.common.MealTime;
import com.msquare.user.entity.WeeklyBhajiEntity;
import com.msquare.user.model.BhajiChartDTO;
import com.msquare.user.model.WeeklyBhajiDTO;
import com.msquare.user.repo.WeeklyBhajiRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.msquare.user.common.CommonUtil.mapList;

@Service
public class WeeklyBhajiService {

    private final ModelMapper modelMapper;
    private final WeeklyBhajiRepo weeklyBhajiRepo;

    @Autowired
    public WeeklyBhajiService(
            ModelMapper modelMapper,
            WeeklyBhajiRepo weeklyBhajiRepo
    ) {
        this.modelMapper = modelMapper;
        this.weeklyBhajiRepo = weeklyBhajiRepo;
    }

    // Called when shop is created — initializes all 14 slots
    public void initializeBhajiChart(UUID shopId) {
        if (shopId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID is required");
        }
        List<WeeklyBhajiEntity> slots = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            for (MealTime meal : MealTime.values()) {
                WeeklyBhajiEntity slot = new WeeklyBhajiEntity();
                slot.setShopId(shopId);
                slot.setDayOfWeek(day);
                slot.setMealTime(meal);
                slot.setBhajiName("");
                slots.add(slot);
            }
        }
        weeklyBhajiRepo.saveAll(slots);
    }

    // Get full chart — structured map for frontend
    public BhajiChartDTO getBhajiChart(UUID shopId) {
        if (shopId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID is required");
        }
        List<WeeklyBhajiEntity> list = weeklyBhajiRepo.findByShopId(shopId);
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bhaji chart not found for this shop");
        }

        Map<DayOfWeek, Map<MealTime, String>> chart = new LinkedHashMap<>();
        for (WeeklyBhajiEntity b : list) {
            chart
                .computeIfAbsent(b.getDayOfWeek(), k -> new LinkedHashMap<>())
                .put(b.getMealTime(), b.getBhajiName());
        }
        return new BhajiChartDTO(chart);
    }

    // Get today's bhaji based on current time
    public WeeklyBhajiDTO getTodaysBhaji(UUID shopId) {
        if (shopId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID is required");
        }
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        MealTime currentMeal = getCurrentMealTime();

        WeeklyBhajiEntity entity = weeklyBhajiRepo
                .findByShopIdAndDayOfWeekAndMealTime(shopId, today, currentMeal)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Bhaji not set for today"));

        return modelMapper.map(entity, WeeklyBhajiDTO.class);
    }

    // Owner updates a single slot
    public WeeklyBhajiDTO updateSlot(UUID shopId, WeeklyBhajiDTO weeklyBhajiDTO) {
        if (shopId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID is required");
        }
        if (weeklyBhajiDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bhaji data is required");
        }

        WeeklyBhajiEntity entity = weeklyBhajiRepo
                .findByShopIdAndDayOfWeekAndMealTime(
                        shopId,
                        weeklyBhajiDTO.getDayOfWeek(),
                        weeklyBhajiDTO.getMealTime())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Slot not found"));

        if (weeklyBhajiDTO.getBhajiName() != null) {
            entity.setBhajiName(weeklyBhajiDTO.getBhajiName());
        }

        WeeklyBhajiEntity updated = weeklyBhajiRepo.save(entity);
        return modelMapper.map(updated, WeeklyBhajiDTO.class);
    }

    // Owner saves full chart at once from UI
    public List<WeeklyBhajiDTO> updateAllSlots(UUID shopId, List<WeeklyBhajiDTO> dtos) {
        if (shopId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID is required");
        }
        if (dtos == null || dtos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bhaji data is required");
        }

        List<WeeklyBhajiDTO> responses = new ArrayList<>();
        for (WeeklyBhajiDTO dto : dtos) {
            responses.add(updateSlot(shopId, dto));
        }
        return responses;
    }

    private MealTime getCurrentMealTime() {
        int hour = LocalTime.now().getHour();
        return hour < 15 ? MealTime.LUNCH : MealTime.DINNER;
    }
}