package com.msquare.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.msquare.user.service.UserDetailService;
import com.msquare.user.service.WeeklyBhajiService;
import com.msquare.user.model.BhajiChartDTO;
import com.msquare.user.model.UserDetailDTO;
import com.msquare.user.model.WeeklyBhajiDTO;

import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/shops/{shopId}/bhaji")
public class WeeklyBhajiAPI {
    private final WeeklyBhajiService weeklyBhajiService;

    @Autowired
    public WeeklyBhajiAPI(WeeklyBhajiService weeklyBhajiService) {
        this.weeklyBhajiService = weeklyBhajiService;
    }

    @PostMapping("/init")
    public ResponseEntity<String> initChart(@PathVariable UUID shopId) {
        weeklyBhajiService.initializeBhajiChart(shopId);
        return ResponseEntity.ok("Bhaji chart initialized for shop");
    }

    @GetMapping("/chart")
    public BhajiChartDTO getChart(@PathVariable UUID shopId) {
        return weeklyBhajiService.getBhajiChart(shopId);
    }

    @GetMapping("/today")
    public ResponseEntity<WeeklyBhajiDTO> getToday(@PathVariable UUID shopId) {
        return ResponseEntity.ok(weeklyBhajiService.getTodaysBhaji(shopId));
    }

    @PutMapping("/slot")
    public ResponseEntity<WeeklyBhajiDTO> updateSlot(
            @PathVariable UUID shopId,
            @RequestBody WeeklyBhajiDTO weeklyBhajiDTO) {
        return ResponseEntity.ok(weeklyBhajiService.updateSlot(shopId, weeklyBhajiDTO));
    }

    @PutMapping("/all")
    public ResponseEntity<List<WeeklyBhajiDTO>> updateAll(
            @PathVariable UUID shopId,
            @RequestBody List<WeeklyBhajiDTO> dtos) {
        return ResponseEntity.ok(weeklyBhajiService.updateAllSlots(shopId, dtos));
    }
}
