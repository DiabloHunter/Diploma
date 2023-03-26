package com.example.project.controller;

import com.example.project.dto.statistic.DishStatisticDTO;
import com.example.project.dto.statistic.StatisticDateDTO;
import com.example.project.dto.statistic.UserStatisticDTO;
import com.example.project.service.IStatisticService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {

    @Autowired
    private IStatisticService statisticService;

    //todo add getTablesByTime
    private static final Logger LOG = LogManager.getLogger(StatisticController.class);

    @GetMapping("/dish")
    public ResponseEntity<List<DishStatisticDTO>> getDishStatistic(@RequestBody StatisticDateDTO statisticDateDTO) {
        List<DishStatisticDTO> statistic = statisticService.getDishStatisticByOrders(statisticDateDTO);
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserStatisticDTO>> getUserStatistic(@RequestBody StatisticDateDTO statisticDateDTO) {
        List<UserStatisticDTO> statistic = statisticService.getUserStatisticByOrders(statisticDateDTO);
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

}
