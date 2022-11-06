package com.example.project.dto.cart;

import javax.validation.constraints.NotNull;

public class AddToCartDTO {

    private Long id;
    private @NotNull Long dishId;
    private @NotNull Integer quantity;

    public AddToCartDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
