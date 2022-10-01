package com.example.project.service.impl;

import com.example.project.dto.order.response.OrderProductDto;
import com.example.project.dto.productDto.ProductStatisticDto;
import com.example.project.dto.StatisticDateDto;
import com.example.project.dto.order.response.OrderDtoItem;
import com.example.project.dto.checkout.CheckoutItemDto;
import com.example.project.model.*;
import com.example.project.repository.*;
import com.example.project.service.ICartService;
import com.example.project.service.IOrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService implements IOrderService {

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IProductRepository productRepository;

    @Autowired
    ICategoryRepository categoryRepository;

    @Autowired
    ICartService cartService;

    @Autowired
    IOrderUnit orderUnitService;


    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    @Override
    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
        String successURL = baseURL + "orders";
        String failureURL = baseURL + "payment/failed";
        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
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

    private SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDto))
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long) (checkoutItemDto.getPrice() * 100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(checkoutItemDto.getProductName())
                                .build()
                ).build();
    }

    @Override
    public List<OrderDtoItem> getAllOrders(User user) {
        List<Order> allOrders = orderRepository.findAllByUser(user);
        List<OrderDtoItem> orderDtoItems = new ArrayList<>();
        for (var order : allOrders) {
            OrderDtoItem orderDtoItem = new OrderDtoItem();
            orderDtoItem.setOrderId(order.getId());
            orderDtoItem.setPrice(order.getPrice());
            orderDtoItem.setCreatedDate(order.getCreatedDate());
            orderDtoItem.setUserId(order.getUser().getId());
            List<OrderProductDto> products = new ArrayList<>();
            for (OrderUnit orderUnit : order.getOrderUnits()) {
                Product product = orderUnit.getProduct();
                OrderProductDto orderProductDto = new OrderProductDto(product.getId(), product.getName(), product.getCode(),
                        product.getImageURL(), product.getPrice(), product.getDescription(), orderUnit.getQuantity(),
                        product.getCategory().getId());
                products.add(orderProductDto);
            }
            orderDtoItem.setProducts(products);
            orderDtoItems.add(orderDtoItem);
        }
        return orderDtoItems;
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        return order;
    }

    @Override
    public void addOrder(OrderDtoItem orderDtoItem) {
        Order order = new Order();
        //todo add exception
        User user = userRepository.findById(orderDtoItem.getUserId()).orElseThrow();
        order.setUser(user);
        order.setCreatedDate(new Date());
        order.setPrice(orderDtoItem.getPrice());
        List<OrderUnit> orderUnits = new ArrayList<>();
        for (OrderProductDto orderProductDto : orderDtoItem.getProducts()) {
            OrderUnit orderUnit = new OrderUnit(productRepository.getById(orderProductDto.getProductId()),
                    orderProductDto.getQuantity());
            orderUnits.add(orderUnit);
            orderUnitService.save(orderUnit);
        }
        order.setOrderUnits(orderUnits);

        orderRepository.save(order);
        cartService.deleteCartItemsByUser(user);
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    @Override
    public List<ProductStatisticDto> getStatisticByOrders(StatisticDateDto statisticDateDto) {
        Date start = convertToDateViaSqlDate(convertToLocalDateViaInstant(statisticDateDto.getStart()));
        Date end = convertToDateViaSqlDate(convertToLocalDateViaInstant(statisticDateDto.getEnd()).plusDays(1));

        List<Order> allOrders = orderRepository.findAllByCreatedDateBetween(start, end);
        Map<Product, Double> productMap = new HashMap<>();
        List<ProductStatisticDto> productsStatistic = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);

        for (Order item : allOrders) {
            for(OrderUnit orderUnit:item.getOrderUnits()){
                Product product = orderUnit.getProduct();
                if(productMap.containsKey(product)){
                    productMap.replace(product, productMap.get(product),productMap.get(product) + orderUnit.getQuantity());
                }
                else{
                    productMap.put(product, orderUnit.getQuantity());
                }
            }
        }
        productMap.entrySet().stream()
                .sorted(Map.Entry.<Product, Double>comparingByValue().reversed())
                .forEach(x -> {
                    ProductStatisticDto productStatisticDto = new ProductStatisticDto();
                    productStatisticDto.setId(x.getKey().getId());
                    productStatisticDto.setCode(x.getKey().getCode());
                    productStatisticDto.setName(x.getKey().getName());
                    productStatisticDto.setImageURL(x.getKey().getImageURL());
                    productStatisticDto.setPrice(x.getKey().getPrice());
                    productStatisticDto.setDescription(x.getKey().getDescription());
                    productStatisticDto.setCategoryId(x.getKey().getCategory().getId());
                    productStatisticDto.setMonthSales(x.getValue());
                    productStatisticDto.setPlace(count.get());
                    count.getAndIncrement();
                    productsStatistic.add(productStatisticDto);
                });
        return productsStatistic;
    }
}
