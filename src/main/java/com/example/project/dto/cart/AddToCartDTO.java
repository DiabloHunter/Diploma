package com.example.project.dto.cart;

import javax.validation.constraints.NotNull;

public class AddToCartDTO {

    private @NotNull Long dishId;
    private @NotNull Integer quantity;
    private @NotNull String userEmail;

    public AddToCartDTO() {
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
