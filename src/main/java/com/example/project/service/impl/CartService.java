package com.example.project.service.impl;

import com.example.project.dto.cart.AddToCartDTO;
import com.example.project.dto.cart.CartDTO;
import com.example.project.dto.cart.CartItemDTO;
import com.example.project.exceptions.CustomException;
import com.example.project.model.Cart;
import com.example.project.model.Dish;
import com.example.project.model.User;
import com.example.project.repository.ICartRepository;
import com.example.project.service.ICartService;
import com.example.project.util.TimeUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    @Autowired
    DishService dishService;

    @Autowired
    ICartRepository ICartRepository;

    @Override
    public void addToCart(AddToCartDTO addToCartDto, User user) {
        Dish dish = dishService.findDishById(addToCartDto.getDishId());

        Cart cart = new Cart();
        cart.setDish(dish);
        cart.setUser(user);
        cart.setQuantity(addToCartDto.getQuantity());
        cart.setCreatedDate(TimeUtil.parseTime(new LocalDateTime()));

        ICartRepository.save(cart);
    }

    @Override
    public CartDTO getAllCartItems(User user) {
        List<Cart> cartList = ICartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDTO> cartItems = new ArrayList<>();
        double totalCost = 0;
        for (Cart cart : cartList) {
            CartItemDTO cartItemDto = new CartItemDTO(cart);
            totalCost += cartItemDto.getQuantity() * cart.getDish().getPrice();
            cartItems.add(cartItemDto);
        }
        CartDTO cartDto = new CartDTO();
        cartDto.setTotalCost(totalCost);
        cartDto.setCartItems(cartItems);
        cartDto.setUserId(user.getId());
        return cartDto;
    }

    @Override
    public void deleteCartItem(Long cartItemId, User user) {
        Optional<Cart> optionalCart = ICartRepository.findById(cartItemId);
        if (optionalCart.isEmpty()) {
            throw new CustomException("cart item id is invalid: " + cartItemId);
        }
        Cart cart = optionalCart.get();
        if (cart.getUser() != user) {
            throw new CustomException("cart item does not belong to user: " + cartItemId);
        }
        ICartRepository.delete(cart);
    }

    @Override
    public void deleteCartItemsByUser(User user) {
        List<Cart> optionalCart = ICartRepository.findAllByUserOrderByCreatedDateDesc(user);
        if (optionalCart.isEmpty()) {
            throw new CustomException("User don't have carts");
        }
        ICartRepository.deleteAll(optionalCart);
    }
}
