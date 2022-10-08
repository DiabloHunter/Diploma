package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.productDto.ProductDTO;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.model.WishList;
import com.example.project.service.impl.UserService;
import com.example.project.service.impl.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    UserService userService;

    // save product as wishlist item
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToWishList(@RequestBody Product product,
                                                     @RequestParam("userEmail") String userEmail) {

        // find the user
        User user = userService.getUserByEmail(userEmail);

        // save the item in wishlist
        WishList wishList = new WishList(user, product);
        wishListService.addWishlist(wishList);
        ApiResponse apiResponse = new ApiResponse(true, "Added to wishlist");
        return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }


    // get all wishlist item for a user
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getWishList(@RequestParam("userEmail") String userEmail) {
        // find the user
        User user = userService.getUserByEmail(userEmail);
        List<ProductDTO> productDTOS = wishListService.getWishListForUser(user);
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @DeleteMapping("/delete/{wishlistItem}")
    public ResponseEntity<ApiResponse> deleteFromWishList(@PathVariable("wishlistItem") Long itemId,
                                                     @RequestParam("userEmail") String userEmail) {
        // find the user
        User user = userService.getUserByEmail(userEmail);

        // save the item in wishlist
        wishListService.deleteWishlist(user,itemId);
        ApiResponse apiResponse = new ApiResponse(true, "Deleted from wishlist");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }
}
