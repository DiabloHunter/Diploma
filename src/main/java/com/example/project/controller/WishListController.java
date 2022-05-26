package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ProductDto;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.model.WishList;
import com.example.project.service.AuthenticationService;
import com.example.project.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    AuthenticationService authenticationService;

    // save product as wishlist item
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToWishList(@RequestBody Product product,
                                                     @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        // save the item in wishlist
        WishList wishList = new WishList(user, product);
        wishListService.createWishlist(wishList);
        ApiResponse apiResponse = new ApiResponse(true, "Added to wishlist");
        return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }


    // get all wishlist item for a user

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getWishList(@RequestParam("token") String token) {

        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);
        List<ProductDto> productDtos = wishListService.getWishListForUser(user);
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{wishlistItem}")
    public ResponseEntity<ApiResponse> deleteFromWishList(@PathVariable("wishlistItem") Integer itemId,
                                                     @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        // save the item in wishlist
        wishListService.deleteWishlist(user,itemId);
        ApiResponse apiResponse = new ApiResponse(true, "Deleted from wishlist");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }
}
