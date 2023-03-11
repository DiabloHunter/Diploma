package com.example.project.service.impl;

import com.example.project.dto.statistic.DishStatisticDTO;
import com.example.project.dto.statistic.StatisticDateDTO;
import com.example.project.dto.statistic.UserStatisticDTO;
import com.example.project.model.Dish;
import com.example.project.model.Order;
import com.example.project.model.OrderUnit;
import com.example.project.model.User;
import com.example.project.repository.IOrderRepository;
import com.example.project.service.IDishService;
import com.example.project.service.IOrderService;
import com.example.project.service.IReservationService;
import com.example.project.service.IStatisticService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
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

        for (Order order : allOrders) {
            for (OrderUnit orderUnit : order.getOrderUnits()) {
                Dish dish = orderUnit.getDish();
                if (dishMap.containsKey(dish)) {
                    dishMap.replace(dish, dishMap.get(dish) + orderUnit.getQuantity());
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
                    dishStatisticDto.setNameEn(x.getKey().getNameEn());
                    dishStatisticDto.setNameUa(x.getKey().getNameUa());
                    dishStatisticDto.setImageData(x.getKey().getImageData());
                    dishStatisticDto.setPrice(x.getKey().getPrice());
                    dishStatisticDto.setDescriptionEn(x.getKey().getDescriptionEn());
                    dishStatisticDto.setDescriptionUa(x.getKey().getDescriptionUa());
                    dishStatisticDto.setCategoryId(x.getKey().getCategory().getId());
                    dishStatisticDto.setMonthSales(x.getValue());
                    dishStatisticDto.setPlace(count.get());
                    count.getAndIncrement();
                    dishesStatistic.add(dishStatisticDto);
                });
        return dishesStatistic;
    }

    @Override
    public List<UserStatisticDTO> getUserStatisticByOrders(StatisticDateDTO statisticDateDto) {
        LocalDateTime start = statisticDateDto.getStart();
        LocalDateTime end = statisticDateDto.getEnd().plusDays(1);

        List<Order> allOrders = orderRepository.findAllByCreatedDateBetween(start, end);
        Map<String, UserStatisticDTO> userStatisticMap = new HashMap<>();
        AtomicInteger count = new AtomicInteger(1);

        for (Order order : allOrders) {
            User user = order.getUser();
            Double price = order.getPrice();

            String userEmail = user.getEmail();
            UserStatisticDTO userStatisticDTO;
            if (userStatisticMap.containsKey(userEmail)) {
                userStatisticDTO = userStatisticMap.get(userEmail);
                userStatisticDTO.setOrdersCount(userStatisticDTO.getOrdersCount() + 1);
                userStatisticDTO.setMoneyCount(userStatisticDTO.getMoneyCount() + price);

                userStatisticMap.replace(userEmail, userStatisticDTO);
            } else {
                userStatisticDTO = new UserStatisticDTO();
                userStatisticDTO.setEmail(user.getEmail());
                userStatisticDTO.setName(user.getName());
                userStatisticDTO.setRating(user.getRating());
                userStatisticDTO.setOrdersCount(1d);
                userStatisticDTO.setMoneyCount(price);

                userStatisticMap.put(userEmail, userStatisticDTO);
            }

            count.getAndIncrement();
            userStatisticDTO.setPlace(count.get());
        }

        List<UserStatisticDTO> result = new ArrayList<>(userStatisticMap.values());
        result.sort(Comparator.comparingDouble(UserStatisticDTO::getMoneyCount));

        return result;
    }
}
