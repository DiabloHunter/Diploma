package com.example.project.service.impl;

import com.example.project.dto.order.response.OrderProductDTO;
import com.example.project.dto.productDto.ProductStatisticDTO;
import com.example.project.dto.StatisticDateDTO;
import com.example.project.dto.order.response.OrderItemDTO;
import com.example.project.dto.checkout.CheckoutItemDTO;
import com.example.project.exceptions.ProductNotExistsException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
                                .setName(checkoutItemDto.getProductName())
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
            List<OrderProductDTO> products = new ArrayList<>();
            for (OrderUnit orderUnit : order.getOrderUnits()) {
                Product product = orderUnit.getProduct();
                OrderProductDTO orderProductDto = new OrderProductDTO(product.getId(), product.getName(), product.getCode(),
                        product.getImageURL(), product.getPrice(), product.getDescription(), orderUnit.getQuantity(),
                        product.getCategory().getId());
                products.add(orderProductDto);
            }
            orderItemDTO.setProducts(products);
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
        order.setCreatedDate(new Date());
        order.setPrice(orderItemDTO.getPrice());
        List<OrderUnit> orderUnits = new ArrayList<>();
        for (OrderProductDTO orderProductDto : orderItemDTO.getProducts()) {
            OrderUnit orderUnit = new OrderUnit(productRepository.findProductByCode(
                    orderProductDto.getCode()).orElseThrow(() ->
                    //todo decide, what method should i use to search the product(id or code) + decide what kind of exception
                    new ProductNotExistsException("Product with given code not exist. Code: " + orderProductDto.getCode())),
                    orderProductDto.getQuantity());
            orderUnits.add(orderUnit);
            orderUnitRepository.save(orderUnit);
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
    public List<ProductStatisticDTO> getStatisticByOrders(StatisticDateDTO statisticDateDto) {
        Date start = convertToDateViaSqlDate(convertToLocalDateViaInstant(statisticDateDto.getStart()));
        Date end = convertToDateViaSqlDate(convertToLocalDateViaInstant(statisticDateDto.getEnd()).plusDays(1));

        List<Order> allOrders = orderRepository.findAllByCreatedDateBetween(start, end);
        Map<Product, Double> productMap = new HashMap<>();
        List<ProductStatisticDTO> productsStatistic = new ArrayList<>();
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
                    ProductStatisticDTO productStatisticDto = new ProductStatisticDTO();
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
