package com.example.project.service;

import com.example.project.dto.dish.DishDTO;
import com.example.project.exceptions.DishNotExistsException;
import com.example.project.model.Category;
import com.example.project.model.Dish;

import java.util.List;

public interface IDishService {
    void addDish(DishDTO dishDto, Category category) throws Exception;

    DishDTO getDishDto(Dish dish);

    Dish getDishBySearchId(String searchId);

    List<DishDTO> getAllDishes();

    void updateDish(DishDTO dishDto) throws Exception;

    Dish findDishById(Long dishId) throws DishNotExistsException;

    void deleteDishById(Long dishId);

    void checkPrices();
}
