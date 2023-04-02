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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {

    @Autowired
    private IStatisticService statisticService;

    //todo add getTablesByTime
    private static final Logger LOG = LogManager.getLogger(StatisticController.class);

    @GetMapping("/dish")
    public ResponseEntity<List<DishStatisticDTO>> getDishStatistic(@RequestParam Date startDate, @RequestParam Date endDate) {
        List<DishStatisticDTO> statistic = statisticService.getDishStatisticByOrders(
                new StatisticDateDTO(LocalDateTime.fromDateFields(startDate), LocalDateTime.fromDateFields(endDate)));
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserStatisticDTO>> getUserStatistic(@RequestBody StatisticDateDTO statisticDateDTO) {
        List<UserStatisticDTO> statistic = statisticService.getUserStatisticByOrders(statisticDateDTO);
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

}
