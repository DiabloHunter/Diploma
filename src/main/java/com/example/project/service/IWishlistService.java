package com.example.project.service;

import com.example.project.dto.dish.DishDTO;
import com.example.project.model.Action;
import javassist.NotFoundException;

import java.util.List;

public interface IWishlistService {

    List<DishDTO> getWishListForUser(String userEmail) throws NotFoundException;

    void process(Long dishId, String userEmail, Action action) throws NotFoundException;
}
