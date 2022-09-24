package com.example.project.service;

import com.example.project.dto.cart.AddToCartDto;
import com.example.project.dto.cart.CartDto;
import com.example.project.model.User;

public interface ICartService {
    void addToCart(AddToCartDto addToCartDto, User user);

    CartDto getAllCartItems(User user);

    void deleteCartItem(Long cartItemId, User user);

    void deleteCartItemsByUser(User user);
}
