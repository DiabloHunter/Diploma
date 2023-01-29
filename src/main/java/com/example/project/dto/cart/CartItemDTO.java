package com.example.project.dto.cart;


import com.example.project.dto.dish.DishDTO;
import com.example.project.model.Cart;
import com.example.project.model.Dish;

public class CartItemDTO {
    private Long id;
    private Integer quantity;
    private Dish dish;

    public CartItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public CartItemDTO(Cart cart) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.setDish(cart.getDish());
    }
}
