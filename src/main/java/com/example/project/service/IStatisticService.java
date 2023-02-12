package com.example.project.service;

import com.example.project.dto.dish.DishStatisticDTO;
import com.example.project.dto.statistic.StatisticDateDTO;

import java.util.List;

public interface IStatisticService {
    List<DishStatisticDTO> getDishStatisticByOrders(StatisticDateDTO statisticDateDto);
}
