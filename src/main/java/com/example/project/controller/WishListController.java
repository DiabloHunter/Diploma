package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.dish.DishDTO;
import com.example.project.model.Dish;
import com.example.project.model.User;
import com.example.project.model.WishList;
import com.example.project.service.impl.UserService;
import com.example.project.service.impl.WishListService;
import com.example.project.util.TimeUtil;
import org.joda.time.LocalDateTime;
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

    // save dish as wishlist item
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToWishList(@RequestBody Dish dish,
                                                     @RequestParam("userEmail") String userEmail) {

        // find the user
        User user = userService.getUserByEmail(userEmail);

        // save the item in wishlist
        WishList wishList = new WishList(user, TimeUtil.parseDateTime(new LocalDateTime()), dish);
        wishListService.addWishlist(wishList);
        ApiResponse apiResponse = new ApiResponse(true, "Added to wishlist");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }


    // get all wishlist item for a user
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/")
    public ResponseEntity<List<DishDTO>> getWishList(@RequestParam("userEmail") String userEmail) {
        // find the user
        User user = userService.getUserByEmail(userEmail);
        List<DishDTO> dishDTOS = wishListService.getWishListForUser(user);
        return new ResponseEntity<>(dishDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteFromWishList(@RequestParam("wishlistId") Long itemId,
                                                          @RequestParam("userEmail") String userEmail) {
        // find the user
        User user = userService.getUserByEmail(userEmail);

        // save the item in wishlist
        wishListService.deleteWishlist(user, itemId);
        ApiResponse apiResponse = new ApiResponse(true, "Deleted from wishlist");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }
}
