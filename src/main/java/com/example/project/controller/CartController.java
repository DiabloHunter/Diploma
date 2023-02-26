package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.cart.AddToCartDTO;
import com.example.project.dto.cart.CartDTO;
import com.example.project.service.ICartService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    private static final Logger LOG = LogManager.getLogger(CartController.class);

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/")
    public ResponseEntity<CartDTO> getCartItems(@RequestParam("userEmail") String userEmail) {
        try {
            CartDTO cartDto = cartService.getAllCartItems(userEmail);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDTO addToCartDto) throws NotFoundException {
        cartService.addToCart(addToCartDto);
        LOG.info(String.format("Dish with id %s was added to %s user's bucket!",
                addToCartDto.getDishId(), addToCartDto.getUserEmail()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Dish with id %s added to cart!", addToCartDto.getDishId())), HttpStatus.CREATED);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCartItem(@RequestParam("cartItemId") String itemId,
                                                      @RequestParam("userEmail") String userEmail) throws NotFoundException {
        cartService.deleteCartItem(itemId, userEmail);

        LOG.info(String.format("Cart item with id %s was deleted from %s user's bucket!",
                itemId, userEmail));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Cart item with id %s was deleted from %s user's bucket!", itemId, userEmail)), HttpStatus.OK);

    }

}
