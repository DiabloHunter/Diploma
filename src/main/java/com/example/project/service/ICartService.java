package com.example.project.service;

import com.example.project.dto.cart.AddToCartDTO;
import com.example.project.dto.cart.CartDTO;
import com.example.project.model.User;

public interface ICartService {
    void addToCart(AddToCartDTO addToCartDto, User user);

    CartDTO getAllCartItems(User user);

    void deleteCartItem(Long cartItemId, User user);

    void deleteCartItemsByUser(User user);
}
