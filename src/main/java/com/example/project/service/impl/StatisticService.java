package com.example.project.service.impl;

import com.example.project.dto.dish.DishStatisticDTO;
import com.example.project.dto.statistic.StatisticDateDTO;
import com.example.project.model.Dish;
import com.example.project.model.Order;
import com.example.project.model.OrderUnit;
import com.example.project.repository.IOrderRepository;
import com.example.project.service.IDishService;
import com.example.project.service.IOrderService;
import com.example.project.service.IReservationService;
import com.example.project.service.IStatisticService;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticService implements IStatisticService {

    @Autowired
    private IDishService dishService;
    @Autowired
    private IReservationService reservationService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderRepository orderRepository;

//    @Override
//    public void getAllStatistic(StatisticDateDTO statisticDateDto){
//        List<Order> orders = orderService.getStatisticByOrders()
//    }

    @Override
    public List<DishStatisticDTO> getDishStatisticByOrders(StatisticDateDTO statisticDateDto) {
        org.joda.time.LocalDateTime start = statisticDateDto.getStart();
        LocalDateTime end = statisticDateDto.getEnd().plusDays(1);

        List<Order> allOrders = orderRepository.findAllByCreatedDateBetween(start, end);
        Map<Dish, Double> dishMap = new HashMap<>();
        List<DishStatisticDTO> dishesStatistic = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);

        for (Order item : allOrders) {
            for (OrderUnit orderUnit : item.getOrderUnits()) {
                Dish dish = orderUnit.getDish();
                if (dishMap.containsKey(dish)) {
                    dishMap.replace(dish, dishMap.get(dish), dishMap.get(dish) + orderUnit.getQuantity());
                } else {
                    dishMap.put(dish, orderUnit.getQuantity());
                }
            }
        }
        dishMap.entrySet().stream()
                .sorted(Map.Entry.<Dish, Double>comparingByValue().reversed())
                .forEach(x -> {
                    DishStatisticDTO dishStatisticDto = new DishStatisticDTO();
                    dishStatisticDto.setId(x.getKey().getId());
                    dishStatisticDto.setSearchId(x.getKey().getSearchId());
                    dishStatisticDto.setName(x.getKey().getName());
                    dishStatisticDto.setImageData(x.getKey().getImageData());
                    dishStatisticDto.setPrice(x.getKey().getPrice());
                    dishStatisticDto.setDescription(x.getKey().getDescription());
                    dishStatisticDto.setCategoryId(x.getKey().getCategory().getId());
                    dishStatisticDto.setMonthSales(x.getValue());
                    dishStatisticDto.setPlace(count.get());
                    count.getAndIncrement();
                    dishesStatistic.add(dishStatisticDto);
                });
        return dishesStatistic;
    }

}
