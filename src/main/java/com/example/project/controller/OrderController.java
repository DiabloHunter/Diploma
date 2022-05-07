package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.order.OrderDto;
import com.example.project.dto.order.OrderDtoItem;
import com.example.project.dto.checkout.CheckoutItemDto;
import com.example.project.dto.checkout.StripeResponse;
import com.example.project.model.User;
import com.example.project.service.AuthenticationService;
import com.example.project.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OrderService orderService;


    // stripe session checkout api

    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList)
            throws StripeException {
        Session session = orderService.createSession(checkoutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);

    }
    @GetMapping("/getOrders/")
    public ResponseEntity<OrderDto> getCartItems(@RequestParam("token") String token) {
        // authenticate the token
        authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

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
    public ResponseEntity<OrderDtoItem> getCartItems(@PathVariable("orderId") Integer orderId,
                                                 @RequestParam("token") String token) {
        // authenticate the token
        authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        // get cart items

        OrderDtoItem order = orderService.getOrderById(orderId);
        order.setUserId(user.getId());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    private void authenticate(String token){
        authenticationService.authenticate(token);
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody OrderDtoItem orderDtoItem,
                                                 @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);


        orderService.addOrder(orderDtoItem);

        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }


}
