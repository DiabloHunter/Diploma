package com.example.project.service;

import com.example.project.dto.cart.AddToCartDTO;
import com.example.project.dto.cart.CartDTO;
import com.example.project.model.User;
import javassist.NotFoundException;

public interface ICartService {

    void addToCart(AddToCartDTO addToCartDto) throws NotFoundException;

    CartDTO getAllCartItems(String userEmail) throws NotFoundException;

    void deleteCartItem(Long cartItemId, String userEmail) throws NotFoundException;

    void deleteCartItemsByUser(User user);

}
