package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.dish.DishDTO;
import com.example.project.model.Action;
import com.example.project.service.IWishlistService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    IWishlistService wishlistService;

    private static final Logger LOG = LogManager.getLogger(WishListController.class);

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/")
    public ResponseEntity<List<DishDTO>> getWishList(@RequestParam("userEmail") String userEmail) {
        try {
            List<DishDTO> dishDTOS = wishlistService.getWishListForUser(userEmail);
            return new ResponseEntity<>(dishDTOS, HttpStatus.OK);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> addToWishlist(@RequestParam("dishId") Long dishId,
                                                     @RequestParam("userEmail") String userEmail) {
        try {
            wishlistService.process(dishId, userEmail, Action.CREATE);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }

        LOG.info(String.format("Wishlist with dishId %s was created for user with email %s", dishId, userEmail));
        return new ResponseEntity<>( new ApiResponse(true,
                String.format("Wishlist with dishId %s was created for user with email %s", dishId, userEmail)), HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteFromWishlist(@RequestParam("dishId") Long dishId,
                                                          @RequestParam("userEmail") String userEmail) {
        try {
            wishlistService.process(dishId, userEmail, Action.DELETE);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }

        LOG.info(String.format("Wishlist with dishId %s was deleted for user with email %s", dishId, userEmail));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Wishlist with dishId %s was deleted for user with email %s", dishId, userEmail)), HttpStatus.OK);
    }
}
