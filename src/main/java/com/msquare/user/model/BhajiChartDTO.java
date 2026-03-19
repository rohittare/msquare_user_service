package com.msquare.user.model;
import lombok.*;
import java.time.DayOfWeek;
import java.util.Map;
import com.msquare.user.common.MealTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class BhajiChartDTO {
    private Map<DayOfWeek, Map<MealTime, String>> chart;

    public BhajiChartDTO(Map<DayOfWeek, Map<MealTime, String>> chart) {
        this.chart = chart;
    }

}