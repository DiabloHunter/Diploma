package com.example.project.dto.order.response;

import javax.validation.constraints.NotNull;

public class CreateOrderDishDTO {

    private @NotNull String searchId;
    private @NotNull Double quantity;

    public CreateOrderDishDTO() {
    }

    public CreateOrderDishDTO(String searchId, Double quantity) {
        this.searchId = searchId;
        this.quantity = quantity;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
