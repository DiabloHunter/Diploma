package com.example.project.service.impl;


import com.example.project.dto.productDto.ProductDto;
import com.example.project.exceptions.CustomException;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.model.WishList;
import com.example.project.repository.IProductRepository;
import com.example.project.repository.IWishListRepository;
import com.example.project.service.IWishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListService implements IWishListService {

    @Autowired
    IWishListRepository IWishListRepository;

    @Autowired
    IProductRepository IProductRepository;

    @Autowired
    ProductService productService;

    @Override
    public void addWishlist(WishList wishList) {
        IWishListRepository.save(wishList);
    }

    @Override
    public void deleteWishlist(User user, Long productId) {
        Product product = IProductRepository.findById(productId)
                .orElseThrow(() -> new CustomException("product id is invalid: " + productId));

        WishList wishList = IWishListRepository.findByUserAndProduct(user, product);

        if (wishList==null) {
            throw new CustomException("wishList is invalid!");
        }

        IWishListRepository.delete(wishList);
    }

    @Override
    public List<ProductDto> getWishListForUser(User user) {
        final List<WishList> wishLists = IWishListRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<ProductDto> productDtos = new ArrayList<>();
        for (WishList wishList: wishLists) {
            productDtos.add(productService.getProductDto(wishList.getProduct()));
        }

        return productDtos;
    }
}
