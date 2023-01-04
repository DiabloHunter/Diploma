package com.example.project.service;

import com.example.project.dto.statistic.StatisticDateDTO;
import com.example.project.dto.checkout.CheckoutItemDTO;
import com.example.project.dto.order.response.OrderItemDTO;
import com.example.project.dto.dish.DishStatisticDTO;
import com.example.project.model.Order;
import com.example.project.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.util.List;

public interface IOrderService {
    Session createSession(List<CheckoutItemDTO> checkoutItemDTOList) throws StripeException;

    List<OrderItemDTO> getAllOrders(User user);

    Order getOrderById(Long id);

    void createOrder(OrderItemDTO orderItemDTO);

    List<DishStatisticDTO> getStatisticByOrders(StatisticDateDTO statisticDateDto);
}
