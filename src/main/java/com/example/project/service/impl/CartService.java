package com.example.project.service.impl;

import com.example.project.dto.cart.AddToCartDTO;
import com.example.project.dto.cart.CartDTO;
import com.example.project.dto.cart.CartItemDTO;
import com.example.project.model.Cart;
import com.example.project.model.Dish;
import com.example.project.model.User;
import com.example.project.repository.ICartRepository;
import com.example.project.service.ICartService;
import com.example.project.service.IUserService;
import com.example.project.util.TimeUtil;
import javassist.NotFoundException;
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
    ICartRepository cartRepository;

    @Autowired
    IUserService userService;

    @Override
    public void addToCart(AddToCartDTO addToCartDto) throws NotFoundException {
        Dish dish = dishService.findDishById(addToCartDto.getDishId());
        if (dish == null) {
            throw new NotFoundException(String.format("Dish with id %s was not found!", addToCartDto.getDishId()));
        }
        User user = userService.getUserByEmail(addToCartDto.getUserEmail());
        if (user == null) {
            throw new NotFoundException(String.format("User with email %s was not found!", addToCartDto.getUserEmail()));
        }

        Cart cart = new Cart();
        cart.setDish(dish);
        cart.setUser(user);
        cart.setQuantity(addToCartDto.getQuantity());
        cart.setCreatedDate(TimeUtil.formatLocalDateTime(new LocalDateTime()));

        cartRepository.save(cart);
    }

    @Override
    public CartDTO getAllCartItems(String userEmail) throws NotFoundException {
        User user = userService.getUserByEmail(userEmail);

        if (user == null) {
            throw new NotFoundException(String.format("User with email %s was not found!", userEmail));
        }

        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDTO> cartItems = new ArrayList<>();
        double totalCost = 0;
        for (Cart cart : cartList) {
            CartItemDTO cartItemDto = new CartItemDTO(cart);
            totalCost += cartItemDto.getQuantity() * cart.getDish().getPrice();
            cartItems.add(cartItemDto);
        }

        double discount = getDiscount(user.getRating());

        CartDTO cartDto = new CartDTO();
        cartDto.setTotalCost(totalCost);
        cartDto.setCartItems(cartItems);
        cartDto.setUserEmail(user.getEmail());
        cartDto.setDiscount(discount);
        return cartDto;
    }

    private double getDiscount(double rating) {
        if (rating > 1500 && rating < 3000) {
            return 0.97;
        }
        if (rating > 3000 && rating < 5000) {
            return 0.95;
        }
        if (rating > 5000) {
            return 0.93;
        }
        return 1;
    }

    @Override
    public void deleteCartItem(String cartItemId, String userEmail) throws NotFoundException {
        User user = userService.getUserByEmail(userEmail);

        if (user == null) {
            throw new NotFoundException(String.format("User with email %s was not found!", userEmail));
        }

        Optional<Cart> optionalCart = cartRepository.findById(cartItemId);
        if (optionalCart.isEmpty()) {
            throw new NotFoundException(String.format("Cart item with id %s was not found!", cartItemId));
        }
        Cart cart = optionalCart.get();
        if (!cart.getUser().equals(user)) {
            throw new IllegalArgumentException(String.format("Cart item with id %s does not belong to user with email %s",
                    cartItemId, userEmail));
        }
        cartRepository.delete(cart);
    }

    @Override
    public void deleteCartItemsByUser(User user) throws NotFoundException {
        List<Cart> optionalCart = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        if (optionalCart.isEmpty()) {
            throw new NotFoundException("User's cart is empty!");
        }
        cartRepository.deleteAll(optionalCart);
    }
}
