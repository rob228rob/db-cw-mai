package com.k_plus.internship.StatisticsPackage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stats")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/get")
    public ResponseEntity<StatResponseDto> getUserRankByCourse(@RequestParam UUID courseId, @RequestParam UUID userId) {
        return ResponseEntity
                .ok(statisticsService.findStatsRankByCourse( courseId, userId));
    }
}
