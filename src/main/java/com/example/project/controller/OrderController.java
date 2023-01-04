package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.dish.DishStatisticDTO;
import com.example.project.dto.liqpay.LiqPayResponse;
import com.example.project.dto.liqpay.PayOptions;
import com.example.project.dto.statistic.StatisticDateDTO;
import com.example.project.dto.order.response.OrderDTO;
import com.example.project.dto.order.response.OrderItemDTO;
import com.example.project.dto.checkout.CheckoutItemDTO;
import com.example.project.dto.checkout.StripeResponse;
import com.example.project.model.Order;
import com.example.project.model.User;
import com.example.project.service.ILiqPayService;
import com.example.project.service.IOrderService;
import com.example.project.service.IUserService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ILiqPayService liqPayService;

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDTO> checkoutItemDTOList)
            throws StripeException {
        Session session = orderService.createSession(checkoutItemDTOList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/liqPay")
    public ResponseEntity<LiqPayResponse> liqPay()
            throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException {
        LiqPayResponse liqPayResponse = liqPayService.createSession(new PayOptions("3", "100", "pay", "UAH",
                "Donate for Armed Forces of Ukraine", UUID.randomUUID().toString()));
        return new ResponseEntity<>(liqPayResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/liqPay2")
    public ResponseEntity<LiqPayResponse> liqPay(@RequestBody List<CheckoutItemDTO> checkoutItemDTOList)
            throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException {
        LiqPayResponse liqPayResponse = liqPayService.createSession(checkoutItemDTOList);
        return new ResponseEntity<>(liqPayResponse, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/getOrders/")
    public ResponseEntity<OrderDTO> getOrders(@RequestParam("userEmail") String email) {
        User user = userService.getUserByEmail(email);

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
    public ResponseEntity<Order> getOrder(@RequestParam Long id, @RequestParam String email) {
        User user = userService.getUserByEmail(email);

        Order order = orderService.getOrderById(id);
        order.setUser(user);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/getStatistic")
    public ResponseEntity<List<DishStatisticDTO>> getStatistic(@RequestBody StatisticDateDTO statisticDateDto) {
        List<DishStatisticDTO> dishStatisticDTOS = orderService.getStatisticByOrders(statisticDateDto);
        return new ResponseEntity<>(dishStatisticDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderItemDTO orderItemDTO) {
        orderService.createOrder(orderItemDTO);
        return new ResponseEntity<>(new ApiResponse(true, "Order created!"), HttpStatus.CREATED);
    }

}
