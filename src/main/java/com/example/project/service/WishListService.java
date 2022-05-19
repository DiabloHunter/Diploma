package com.example.project.service;


import com.example.project.dto.ProductDto;
import com.example.project.exceptions.CustomException;
import com.example.project.model.Cart;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.model.WishList;
import com.example.project.repository.ProductRepository;
import com.example.project.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishListService {

    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    public void createWishlist(WishList wishList) {
        wishListRepository.save(wishList);
    }

    public void deleteWishlist(User user, Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new CustomException("product id is invalid: " + productId);
        }
        Product product = productOptional.get();

        WishList wishList = wishListRepository.findByUserAndProduct(user, product);

        if (wishList==null) {
            throw new CustomException("wishList is invalid!");
        }

        wishListRepository.delete(wishList);
    }

    public List<ProductDto> getWishListForUser(User user) {
        final List<WishList> wishLists = wishListRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<ProductDto> productDtos = new ArrayList<>();
        for (WishList wishList: wishLists) {
            productDtos.add(productService.getProductDto(wishList.getProduct()));
        }

        return productDtos;
    }
}
