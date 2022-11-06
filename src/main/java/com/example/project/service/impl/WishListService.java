package com.example.project.service.impl;

import com.example.project.dto.dishDto.DishDTO;
import com.example.project.exceptions.CustomException;
import com.example.project.model.Dish;
import com.example.project.model.User;
import com.example.project.model.WishList;
import com.example.project.repository.IDishRepository;
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
    IDishRepository IDishRepository;

    @Autowired
    DishService dishService;

    @Override
    public void addWishlist(WishList wishList) {
        IWishListRepository.save(wishList);
    }

    @Override
    public void deleteWishlist(User user, Long dishId) {
        Dish dish = IDishRepository.findById(dishId)
                .orElseThrow(() -> new CustomException("dish id is invalid: " + dishId));

        WishList wishList = IWishListRepository.findByUserAndDish(user, dish);

        if (wishList == null) {
            throw new CustomException("wishList is invalid!");
        }

        IWishListRepository.delete(wishList);
    }

    @Override
    public List<DishDTO> getWishListForUser(User user) {
        final List<WishList> wishLists = IWishListRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<DishDTO> dishDTOS = new ArrayList<>();
        for (WishList wishList : wishLists) {
            dishDTOS.add(dishService.getDishDto(wishList.getDish()));
        }

        return dishDTOS;
    }
}
