package com.example.project.service;

import com.example.project.dto.statistic.DishStatisticDTO;
import com.example.project.dto.statistic.StatisticDateDTO;
import com.example.project.dto.statistic.UserStatisticDTO;

import java.util.List;

public interface IStatisticService {
    List<DishStatisticDTO> getDishStatisticByOrders(StatisticDateDTO statisticDateDto);

    List<UserStatisticDTO> getUserStatisticByOrders(StatisticDateDTO statisticDateDto);
}
