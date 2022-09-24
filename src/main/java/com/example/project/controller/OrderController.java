package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.productDto.ProductStatisticDto;
import com.example.project.dto.StatisticDateDto;
import com.example.project.dto.order.OrderDto;
import com.example.project.dto.order.OrderDtoItem;
import com.example.project.dto.checkout.CheckoutItemDto;
import com.example.project.dto.checkout.StripeResponse;
import com.example.project.model.User;
import com.example.project.service.IOrderService;
import com.example.project.service.IUserService;
import com.example.project.service.impl.OrderService;
import com.example.project.service.impl.UserService;
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
    public ResponseEntity<OrderDto> getCartItems(@RequestParam("userEmail") String userEmail) {
        // find the user
        User user = userService.getUserByEmail(userEmail);

        // get cart items
        List<OrderDtoItem> orders = orderService.getAllOrders(user);
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderItems(orders);
        double totalSum=0;
        for(var el: orders){
            totalSum+=el.getPrice();
        }
        orderDto.setTotalCost(totalSum);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<OrderDtoItem> getCartItems(@PathVariable("orderId") Long orderId,
                                                     @RequestParam("userEmail") String userEmail) {
        // find the user
        User user = userService.getUserByEmail(userEmail);

        // get cart items

        OrderDtoItem order = orderService.getOrderById(orderId);
        order.setUserId(user.getId());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/getStatistic")
    public ResponseEntity<List<ProductStatisticDto>> getStatistic(@RequestBody StatisticDateDto statisticDateDto) {
        List<ProductStatisticDto> productStatisticDtos = orderService.getStatisticByOrders(statisticDateDto);
        return new ResponseEntity<>(productStatisticDtos, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody OrderDtoItem orderDtoItem,
                                                 @RequestParam("token") String token) {
        orderService.addOrder(orderDtoItem);
        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }


}
