package com.example.project.controller;

import com.example.project.dto.statistic.DishStatisticDTO;
import com.example.project.dto.statistic.StatisticDateDTO;
import com.example.project.dto.statistic.UserStatisticDTO;
import com.example.project.service.IStatisticService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {

    @Autowired
    private IStatisticService statisticService;

    private static final Logger LOG = LogManager.getLogger(StatisticController.class);

    @Async
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/dish")
    public CompletableFuture<ResponseEntity<List<DishStatisticDTO>>> getDishStatistic(@RequestParam Date startDate, @RequestParam Date endDate) {
        List<DishStatisticDTO> statistic = statisticService.getDishStatisticByOrders(
                new StatisticDateDTO(LocalDateTime.fromDateFields(startDate), LocalDateTime.fromDateFields(endDate)));
        return CompletableFuture.completedFuture(ResponseEntity.ok(statistic));
    }

    @Async
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/user")
    public CompletableFuture<ResponseEntity<List<UserStatisticDTO>>> getUserStatistic(@RequestParam Date startDate, @RequestParam Date endDate) {
        List<UserStatisticDTO> statistic = statisticService.getUserStatisticByOrders(new StatisticDateDTO(LocalDateTime.fromDateFields(startDate), LocalDateTime.fromDateFields(endDate)));
        return CompletableFuture.completedFuture(ResponseEntity.ok(statistic));
    }

}
