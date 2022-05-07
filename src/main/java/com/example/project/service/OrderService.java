package com.example.project.service;



import com.example.project.dto.order.OrderDtoItem;
import com.example.project.dto.checkout.CheckoutItemDto;
import com.example.project.dto.order.OrderProductDto;
import com.example.project.model.*;
import com.example.project.repository.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    CartService cartService;

    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {

        // sucess and failure urls

        String successURL = baseURL + "orders";

        String failureURL = baseURL + "payment/failed";

        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();


        for (CheckoutItemDto checkoutItemDto: checkoutItemDtoList) {
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
                .setUnitAmount((long)(checkoutItemDto.getPrice()*100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(checkoutItemDto.getProductName())
                            .build()
                ).build();
    }




    public List<OrderDtoItem> getAllOrders(User user) {
        List<Order> allOrders = orderRepository.findAllByUser(user);
        List<OrderDtoItem> orderDtoItems = new ArrayList<>();
        for(var order: allOrders){
            if(!order.getOrderProducts().isEmpty()) {
                OrderDtoItem orderDtoItem = new OrderDtoItem();
                orderDtoItem.setId(order.getId());
                orderDtoItem.setPrice(order.getPrice());
                for (var orderProduct : order.getOrderProducts()) {
                    OrderProductDto orderProductDto = new OrderProductDto();
                    orderProductDto.setDescription(orderProduct.getProduct().getDescription());
                    orderProductDto.setImageURL(orderProduct.getProduct().getImageURL());
                    orderProductDto.setName(orderProduct.getProduct().getName());
                    orderProductDto.setCategoryId(orderProduct.getProduct().getCategory().getId());
                    orderProductDto.setPrice(orderProduct.getProduct().getPrice());
                    orderProductDto.setId(orderProduct.getProduct().getId());
                    orderProductDto.setQuantity(orderProduct.getQuantity());
                    orderDtoItem.addProduct(orderProductDto);
                }
                orderDtoItem.setCreatedDate(order.getCreatedDate());
                orderDtoItems.add(orderDtoItem);
            }
        }
        return orderDtoItems;
    }

    public OrderDtoItem getOrderById(Integer id) {
        Order order = orderRepository.findOrderById(id);
        OrderDtoItem orderDtoItem = new OrderDtoItem();
        orderDtoItem.setId(order.getId());
        orderDtoItem.setPrice(order.getPrice());
        for(var orderProduct : order.getOrderProducts()){
            OrderProductDto orderProductDto = new OrderProductDto();
            orderProductDto.setDescription(orderProduct.getProduct().getDescription());
            orderProductDto.setImageURL(orderProduct.getProduct().getImageURL());
            orderProductDto.setName(orderProduct.getProduct().getName());
            orderProductDto.setCategoryId(orderProduct.getProduct().getCategory().getId());
            orderProductDto.setPrice(orderProduct.getProduct().getPrice());
            orderProductDto.setId(orderProduct.getProduct().getId());
            orderProductDto.setQuantity(orderProduct.getQuantity());
            orderDtoItem.addProduct(orderProductDto);
        }
        orderDtoItem.setCreatedDate(order.getCreatedDate());
        return orderDtoItem;
    }

    public void addOrder(OrderDtoItem orderDtoItem) {


        Order order = new Order();

        List<Order> lastOrder = orderRepository.findAll();
        if(lastOrder.isEmpty()){
            order.setId(1);
        }
        else{
            order.setId(lastOrder.get(lastOrder.size()-1).getId()+1);
        }
        order.setPrice(orderDtoItem.getPrice());
        User user = userRepository.findById(orderDtoItem.getUserId()).orElseThrow();
        order.setUser(user);
        order.setCreatedDate(new Date());


        List<OrderProduct> orderProducts = new ArrayList<>();
        for(var productDto: orderDtoItem.getProducts()){
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);

            Product product = productRepository.getById(productDto.getId());
            orderProduct.setProduct(product);
            orderProduct.setQuantity(productDto.getQuantity());

            OrderProductId orderProductId = new OrderProductId(order.getId(), product.getId());
            orderProduct.setId(orderProductId);
            orderProducts.add(orderProduct);

        }
        order.setOrderProducts(orderProducts);

        orderRepository.save(order);
        cartService.deleteCartItemsByUser(user);
    }


}
