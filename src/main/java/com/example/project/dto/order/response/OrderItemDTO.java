package com.example.project.dto.order.response;

import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDTO {

    private String orderId;
    private @NotNull Double price;
    private @NotNull List<OrderDishDTO> dishes = new ArrayList<>();
    private @NotNull String userEmail;
    private @NotNull LocalDateTime createdDate;

    public OrderItemDTO() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<OrderDishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(List<OrderDishDTO> dishes) {
        this.dishes = dishes;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
