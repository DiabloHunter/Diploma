package com.example.project.service.impl;

import com.example.project.dto.cart.AddToCartDto;
import com.example.project.dto.cart.CartDto;
import com.example.project.dto.cart.CartItemDto;
import com.example.project.exceptions.CustomException;
import com.example.project.model.Cart;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.repository.ICartRepository;
import com.example.project.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    @Autowired
    ProductService productService;

    @Autowired
    ICartRepository ICartRepository;

    @Override
    public void addToCart(AddToCartDto addToCartDto, User user) {
        Product product = productService.findProductById(addToCartDto.getProductId());

        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setUser(user);
        cart.setQuantity(addToCartDto.getQuantity());
        cart.setCreatedDate(new Date());

        ICartRepository.save(cart);
    }

    @Override
    public CartDto getAllCartItems(User user) {
        List<Cart> cartList = ICartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost = 0;
        for (Cart cart : cartList) {
            CartItemDto cartItemDto = new CartItemDto(cart);
            totalCost += cartItemDto.getQuantity() * cart.getProduct().getPrice();
            cartItems.add(cartItemDto);
        }
        CartDto cartDto = new CartDto();
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
