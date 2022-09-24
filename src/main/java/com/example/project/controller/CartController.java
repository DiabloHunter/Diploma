package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.cart.AddToCartDto;
import com.example.project.dto.cart.CartDto;
import com.example.project.model.User;
import com.example.project.service.CartService;
import com.example.project.service.ProductService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDto addToCartDto,
                                                 @RequestParam("userEmail") String userEmail) {

        // find the user

        User user = userService.getUserByEmail(userEmail);

        cartService.addToCart(addToCartDto, user);

        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }


    @GetMapping("/add/{goodCode}&{goodAmount}")
    public ResponseEntity<ApiResponse> addToCart(@PathVariable("goodCode") String code,
                                                 @PathVariable("goodAmount") Integer amount,
                                                 @RequestParam("userEmail") String userEmail) {

        User user = userService.getUserByEmail(userEmail);

        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setProductId(productService.getProductId(code));
        addToCartDto.setQuantity(amount);

        cartService.addToCart(addToCartDto, user);

        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }


    // get all cart items for a user
    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("userEmail") String userEmail) {
        User user = userService.getUserByEmail(userEmail);

        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    // delete a cart item for a user

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") Integer itemId,
                                                      @RequestParam("userEmail") String userEmail) {

        User user = userService.getUserByEmail(userEmail);

        cartService.deleteCartItem(itemId, user);

        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);

    }

}
