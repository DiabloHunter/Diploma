package com.example.project.service;

import com.example.project.dto.StatisticDateDto;
import com.example.project.dto.checkout.CheckoutItemDto;
import com.example.project.dto.order.OrderDtoItem;
import com.example.project.dto.productDto.ProductStatisticDto;
import com.example.project.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.util.List;

public interface IOrderService {
    Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException;

    List<OrderDtoItem> getAllOrders(User user);

    OrderDtoItem getOrderById(Long id);

    void addOrder(OrderDtoItem orderDtoItem);

    List<ProductStatisticDto> getStatisticByOrders(StatisticDateDto statisticDateDto);
}
