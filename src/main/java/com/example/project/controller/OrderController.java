package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.order.request.GetOrderDTO;
import com.example.project.dto.productDto.ProductStatisticDto;
import com.example.project.dto.StatisticDateDto;
import com.example.project.dto.order.response.OrderDto;
import com.example.project.dto.order.response.OrderDtoItem;
import com.example.project.dto.checkout.CheckoutItemDto;
import com.example.project.dto.checkout.StripeResponse;
import com.example.project.model.Order;
import com.example.project.model.User;
import com.example.project.service.IOrderService;
import com.example.project.service.IUserService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList)
            throws StripeException {
        Session session = orderService.createSession(checkoutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);

    }

    @GetMapping("/getOrders/")
    public ResponseEntity<OrderDto> getOrders(@RequestBody GetOrderDTO getOrderDTO) {
        // find the user
        User user = userService.getUserByEmail(getOrderDTO.getUserEmail());

        // get cart items
        List<OrderDtoItem> orders = orderService.getAllOrders(user);
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderItems(orders);
        double totalSum = 0;
        for (var el : orders) {
            totalSum += el.getPrice();
        }
        orderDto.setTotalCost(totalSum);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping("/getOrder/")
    public ResponseEntity<Order> getOrder(@RequestBody GetOrderDTO getOrderDTO) {
        // find the user
        User user = userService.getUserByEmail(getOrderDTO.getUserEmail());

        // get cart items
        Order order = orderService.getOrderById(getOrderDTO.getOrderId());
        order.setUser(user);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/getStatistic")
    public ResponseEntity<List<ProductStatisticDto>> getStatistic(@RequestBody StatisticDateDto statisticDateDto) {
        List<ProductStatisticDto> productStatisticDtos = orderService.getStatisticByOrders(statisticDateDto);
        return new ResponseEntity<>(productStatisticDtos, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody OrderDtoItem orderDtoItem) {
        orderService.addOrder(orderDtoItem);
        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }

}
