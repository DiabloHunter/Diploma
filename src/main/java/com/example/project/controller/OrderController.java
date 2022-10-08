package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.order.request.GetOrderDTO;
import com.example.project.dto.productDto.ProductStatisticDTO;
import com.example.project.dto.StatisticDateDTO;
import com.example.project.dto.order.response.OrderDTO;
import com.example.project.dto.order.response.OrderItemDTO;
import com.example.project.dto.checkout.CheckoutItemDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDTO> checkoutItemDTOList)
            throws StripeException {
        Session session = orderService.createSession(checkoutItemDTOList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);

    }
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/getOrders/")
    public ResponseEntity<OrderDTO> getOrders(@RequestBody GetOrderDTO getOrderDTO) {
        // find the user
        User user = userService.getUserByEmail(getOrderDTO.getUserEmail());

        // get cart items
        List<OrderItemDTO> orders = orderService.getAllOrders(user);
        OrderDTO orderDto = new OrderDTO();
        orderDto.setOrderItems(orders);
        double totalSum = 0;
        for (var el : orders) {
            totalSum += el.getPrice();
        }
        orderDto.setTotalCost(totalSum);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/getOrder/")
    public ResponseEntity<Order> getOrder(@RequestBody GetOrderDTO getOrderDTO) {
        // find the user
        User user = userService.getUserByEmail(getOrderDTO.getUserEmail());

        // get cart items
        Order order = orderService.getOrderById(getOrderDTO.getOrderId());
        order.setUser(user);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/getStatistic")
    public ResponseEntity<List<ProductStatisticDTO>> getStatistic(@RequestBody StatisticDateDTO statisticDateDto) {
        List<ProductStatisticDTO> productStatisticDTOS = orderService.getStatisticByOrders(statisticDateDto);
        return new ResponseEntity<>(productStatisticDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody OrderItemDTO orderItemDTO) {
        orderService.addOrder(orderItemDTO);
        return new ResponseEntity<>(new ApiResponse(true, "Order created!"), HttpStatus.CREATED);
    }

}
