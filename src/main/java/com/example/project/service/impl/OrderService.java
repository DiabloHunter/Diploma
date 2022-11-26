package com.example.project.service.impl;

import com.example.project.dto.order.response.OrderDishDTO;
import com.example.project.dto.dish.DishStatisticDTO;
import com.example.project.dto.statistic.StatisticDateDTO;
import com.example.project.dto.order.response.OrderItemDTO;
import com.example.project.dto.checkout.CheckoutItemDTO;
import com.example.project.exceptions.DishNotExistsException;
import com.example.project.model.Dish;
import com.example.project.model.Order;
import com.example.project.model.OrderUnit;
import com.example.project.model.User;
import com.example.project.repository.ICategoryRepository;
import com.example.project.repository.IDishRepository;
import com.example.project.repository.IOrderRepository;
import com.example.project.repository.IOrderUnitRepository;
import com.example.project.repository.IUserRepository;
import com.example.project.service.ICartService;
import com.example.project.service.IOrderService;
import com.example.project.util.TimeUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService implements IOrderService {

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IDishRepository dishRepository;

    @Autowired
    ICategoryRepository categoryRepository;

    @Autowired
    ICartService cartService;

    @Autowired
    IOrderUnitRepository orderUnitRepository;


    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    @Override
    public Session createSession(List<CheckoutItemDTO> checkoutItemDTOList) throws StripeException {
        String successURL = baseURL + "orders";
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

    @Override
    public List<OrderItemDTO> getAllOrders(User user) {
        List<Order> allOrders = orderRepository.findAllByUser(user);
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (var order : allOrders) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setOrderId(order.getId());
            orderItemDTO.setPrice(order.getPrice());
            orderItemDTO.setCreatedDate(order.getCreatedDate());
            orderItemDTO.setUserId(order.getUser().getId());
            List<OrderDishDTO> dishes = new ArrayList<>();
            for (OrderUnit orderUnit : order.getOrderUnits()) {
                Dish dish = orderUnit.getDish();
                OrderDishDTO orderDishDto = new OrderDishDTO(dish.getId(), dish.getName(), dish.getSearchId(),
                        dish.getImageURL(), dish.getPrice(), dish.getDescription(), orderUnit.getQuantity(),
                        dish.getCategory().getId());
                dishes.add(orderDishDto);
            }
            orderItemDTO.setDishes(dishes);
            orderItemDTOS.add(orderItemDTO);
        }
        return orderItemDTOS;
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        return order;
    }

    @Override
    public void addOrder(OrderItemDTO orderItemDTO) {
        Order order = new Order();
        //todo add exception
        User user = userRepository.findById(orderItemDTO.getUserId()).orElseThrow();
        order.setUser(user);
        order.setCreatedDate(TimeUtil.parseTime(new LocalDateTime()));
        order.setPrice(orderItemDTO.getPrice());
        List<OrderUnit> orderUnits = new ArrayList<>();
        for (OrderDishDTO orderDishDto : orderItemDTO.getDishes()) {
            OrderUnit orderUnit = new OrderUnit(dishRepository.findDishBySearchId(
                    orderDishDto.getSearchId()).orElseThrow(() ->
                    //todo decide, what method should i use to search the dish(id or searchId) + decide what kind of exception
                    new DishNotExistsException("Dish with given searchId not exist. SearchId: " + orderDishDto.getSearchId())),
                    orderDishDto.getQuantity());
            orderUnits.add(orderUnit);
            orderUnitRepository.save(orderUnit);
        }
        order.setOrderUnits(orderUnits);

        orderRepository.save(order);
        cartService.deleteCartItemsByUser(user);
    }

    @Override
    public List<DishStatisticDTO> getStatisticByOrders(StatisticDateDTO statisticDateDto) {
        LocalDateTime start = statisticDateDto.getStart();
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
                    dishStatisticDto.setImageURL(x.getKey().getImageURL());
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
