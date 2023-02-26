package com.example.project.service.impl;

import com.example.project.dto.dish.DishDTO;
import com.example.project.model.Action;
import com.example.project.model.Dish;
import com.example.project.model.User;
import com.example.project.model.Wishlist;
import com.example.project.repository.IWishListRepository;
import com.example.project.service.IWishlistService;
import com.example.project.util.TimeUtil;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService implements IWishlistService {

    @Autowired
    IWishListRepository wishListRepository;

    @Autowired
    DishService dishService;

    @Autowired
    UserService userService;

    private static final Logger LOG = LogManager.getLogger(WishlistService.class);

    @Override
    public List<DishDTO> getWishListForUser(String userEmail) throws NotFoundException {
        User user = userService.getUserByEmail(userEmail);
        if (user == null) {
            throw new NotFoundException(String.format("User with email %s was not found!", userEmail));
        }

        List<Wishlist> wishlists = wishListRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<DishDTO> dishDTOS = new ArrayList<>();
        for (Wishlist wishList : wishlists) {
            DishDTO dishDTO = dishService.getDishDto(wishList.getDish());
            if(dishDTO!=null){
                dishDTOS.add(dishDTO);
            } else{
                LOG.warn(String.format("Dish with id %s was not found!", wishList.getDish().getId()));
            }
            dishDTOS.add(dishService.getDishDto(wishList.getDish()));
        }
        return dishDTOS;
    }

    private void create(Dish dish, User user) {
        Wishlist wishList = new Wishlist(user, TimeUtil.formatLocalDateTime(new LocalDateTime()), dish);
        wishListRepository.save(wishList);
    }

    private void delete(Dish dish, User user) throws NotFoundException {
        Wishlist existedWishlist = wishListRepository.findByUserAndDish(user, dish);
        if (existedWishlist == null) {
            throw new NotFoundException(String.format("User with email %s doesn't have wishlist for dish %s",
                    user.getEmail(), dish.getSearchId()));
        }
        wishListRepository.delete(existedWishlist);
    }

    @Override
    public void process(String dishId, String userEmail, Action action) throws NotFoundException {
        User user = userService.getUserByEmail(userEmail);
        if (user == null) {
            throw new NotFoundException(String.format("User with email %s was not found!", userEmail));
        }

        Dish dish = dishService.findDishById(dishId);
        if (dish == null) {
            throw new NotFoundException(String.format("Dish with id %s was not found!", dishId));
        }

        switch (action) {
            case CREATE:
                create(dish, user);
            case DELETE:
                delete(dish, user);
        }
    }
}
