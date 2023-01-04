package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.cart.AddToCartDTO;
import com.example.project.dto.cart.CartDTO;
import com.example.project.model.User;
import com.example.project.service.ICartService;
import com.example.project.service.IDishService;
import com.example.project.service.IUserService;
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

    @Autowired
    private IUserService userService;

    @Autowired
    private IDishService dishService;

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDTO addToCartDto,
                                                 @RequestParam("userEmail") String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        cartService.addToCart(addToCartDto, user);

        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/add/{goodSearchId}&{goodAmount}")
    public ResponseEntity<ApiResponse> addToCart(@PathVariable("goodSearchId") String searchId,
                                                 @PathVariable("goodAmount") Integer amount,
                                                 @RequestParam("userEmail") String userEmail) {
        User user = userService.getUserByEmail(userEmail);

        AddToCartDTO addToCartDto = new AddToCartDTO();
        addToCartDto.setDishId(dishService.getDishBySearchId(searchId).getId());
        addToCartDto.setQuantity(amount);

        cartService.addToCart(addToCartDto, user);

        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }


    // get all cart items for a user
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/")
    public ResponseEntity<CartDTO> getCartItems(@RequestParam("userEmail") String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        CartDTO cartDto = cartService.getAllCartItems(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @DeleteMapping("/delete/")
    public ResponseEntity<ApiResponse> deleteCartItem(@RequestParam("cartItemId") Long itemId,
                                                      @RequestParam("userEmail") String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        cartService.deleteCartItem(itemId, user);

        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);

    }
}
