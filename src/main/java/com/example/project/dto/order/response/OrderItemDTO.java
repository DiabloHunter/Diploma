package com.example.project.dto.order.response;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderItemDTO {

    private Long orderId;
    private @NotNull Double price;
    private @NotNull List<OrderDishDTO> dishes = new ArrayList<>();
    private @NotNull Long userId;
    private @NotNull Date createdDate;

    public OrderItemDTO() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
