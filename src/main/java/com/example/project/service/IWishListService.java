package com.example.project.service;

import com.example.project.dto.dish.DishDTO;
import com.example.project.model.User;
import com.example.project.model.WishList;

import java.util.List;

public interface IWishListService {
    void addWishlist(WishList wishList);

    void deleteWishlist(User user, Long dishId);

    List<DishDTO> getWishListForUser(User user);
}
