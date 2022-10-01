package com.example.project.dto.cart;

import javax.validation.constraints.NotNull;

public class AddToCartDTO {

    private Long id;
    private @NotNull Long productId;
    private @NotNull Integer quantity;

    public AddToCartDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
