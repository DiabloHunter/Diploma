package com.example.project.service;

import com.example.project.dto.productDto.ProductDto;
import com.example.project.model.User;
import com.example.project.model.WishList;

import java.util.List;

public interface IWishListService {
    void addWishlist(WishList wishList);

    void deleteWishlist(User user, Long productId);

    List<ProductDto> getWishListForUser(User user);
}
