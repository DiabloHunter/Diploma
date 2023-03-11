package com.example.project.dto.order.response;

import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderItemDTO {

    private String orderId;
    private @NotNull Double price;
    private @NotNull List<CreateOrderDishDTO> dishes = new ArrayList<>();
    private @NotNull String userEmail;
    private @NotNull LocalDateTime createdDate;

    public CreateOrderItemDTO() {
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

    public List<CreateOrderDishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(List<CreateOrderDishDTO> dishes) {
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
