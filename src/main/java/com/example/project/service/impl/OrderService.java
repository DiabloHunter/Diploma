package com.example.project.service.impl;

import com.example.project.dto.order.response.CreateOrderDishDTO;
import com.example.project.dto.order.response.CreateOrderItemDTO;
import com.example.project.dto.order.response.OrderDishDTO;
import com.example.project.dto.order.response.OrderItemDTO;
import com.example.project.dto.checkout.CheckoutItemDTO;
import com.example.project.model.*;
import com.example.project.repository.IDishRepository;
import com.example.project.repository.IOrderRepository;
import com.example.project.repository.IOrderUnitRepository;
import com.example.project.service.ICartService;
import com.example.project.service.IOrderService;
import com.example.project.service.IUserService;
import com.example.project.util.TimeUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import javassist.NotFoundException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDishRepository dishRepository;

    @Autowired
    private ICartService cartService;

    @Autowired
    private IOrderUnitRepository orderUnitRepository;


    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    @Override
    public List<OrderItemDTO> getAllOrders(String userEmail) throws NotFoundException {
        User user = userService.getUserByEmail(userEmail);
        if (user == null) {
            throw new NotFoundException(String.format("User with given email %s was not found!", userEmail));
        }

        List<Order> allOrders = orderRepository.findAllByUser(user);

        return convertOrder(allOrders);
    }

    private List<OrderItemDTO> convertOrder(List<Order> orders) {
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (var order : orders) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();

            orderItemDTO.setOrderId(order.getId());
            orderItemDTO.setPrice(order.getPrice());
            orderItemDTO.setCreatedDate(order.getCreatedDate());
            orderItemDTO.setOrderState(order.getOrderState());
            orderItemDTO.setUserEmail(order.getUser().getEmail());

            List<OrderDishDTO> dishes = new ArrayList<>();
            for (OrderUnit orderUnit : order.getOrderUnits()) {
                Dish dish = orderUnit.getDish();

                OrderDishDTO orderDishDto = new OrderDishDTO(dish.getId(), dish.getNameEn(), dish.getNameUa(), dish.getSearchId(),
                        dish.getImageData(), dish.getPrice(), dish.getDescriptionEn(), dish.getDescriptionUa(),
                        orderUnit.getQuantity(), dish.getCategory().getId());
                dishes.add(orderDishDto);
            }

            orderItemDTO.setDishes(dishes);
            orderItemDTOS.add(orderItemDTO);
        }
        return orderItemDTOS;
    }

    @Override
    public OrderItemDTO getOrderById(String id) {
        return convertOrder(Collections.singletonList(orderRepository.findById(id).orElse(null))).get(0);
    }

    @Override
    public void createOrder(CreateOrderItemDTO orderItemDTO) throws NotFoundException {
        Order order = new Order();
        User user = userService.getUserByEmail(orderItemDTO.getUserEmail());
        if (user == null) {
            throw new NotFoundException(String.format("User with email %s was not found!", orderItemDTO.getUserEmail()));
        }

        order.setUser(user);
        order.setCreatedDate(TimeUtil.formatLocalDateTime(new LocalDateTime()));
        order.setPrice(orderItemDTO.getPrice());
        List<OrderUnit> orderUnits = new ArrayList<>();
        for (CreateOrderDishDTO orderDishDto : orderItemDTO.getDishes()) {
            Dish dish = dishRepository.findDishBySearchId(orderDishDto.getSearchId());
            if (dish == null) {
                throw new NotFoundException(String.format("Dish with searchId %s was not found!", orderDishDto.getSearchId()));
            }
            OrderUnit orderUnit = new OrderUnit(dish, orderDishDto.getQuantity());
            orderUnits.add(orderUnit);
            orderUnitRepository.save(orderUnit);
        }
        order.setOrderUnits(orderUnits);
        order.setOrderState(OrderState.IN_PROGRESS);

        orderRepository.save(order);

        user.setRating(user.getRating() + orderItemDTO.getPrice());
        userService.update(user);
        cartService.deleteCartItemsByUser(user);
    }

    @Override
    public void setOrderState(String id, OrderState state) throws NotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s was not found", id)));
        order.setOrderState(state);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getUserOrdersByTime(User user, LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByUserAndCreatedDate(user.getId(), start, end);
    }

    @Override
    public Session createSession(List<CheckoutItemDTO> checkoutItemDTOList) throws StripeException {
        String successURL = baseURL + "payment/success";
        String failureURL = baseURL + "payment/failed";
        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        for (CheckoutItemDTO checkoutItemDto : checkoutItemDTOList) {
            sessionItemList.add(createSessionLineItem(checkoutItemDto));
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureURL)
                .addAllLineItem(sessionItemList)
                .setSuccessUrl(successURL)
                .build();

        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDTO checkoutItemDto) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDto))
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDTO checkoutItemDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long) (checkoutItemDto.getPrice() * 100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(checkoutItemDto.getDishName())
                                .build()
                ).build();
    }
}
