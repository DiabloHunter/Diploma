package com.example.project.service.impl;

import com.example.project.dto.dish.DishDTO;
import com.example.project.model.Category;
import com.example.project.model.Order;
import com.example.project.model.Dish;
import com.example.project.repository.IOrderRepository;
import com.example.project.repository.IDishRepository;
import com.example.project.service.ICategoryService;
import com.example.project.service.IDishService;
import com.example.project.util.TimeUtil;
import com.example.project.util.ValidationUtil;
import javassist.NotFoundException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishService implements IDishService {

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IDishRepository dishRepository;

    @Autowired
    IOrderRepository orderRepository;

    @Override
    public void create(DishDTO dishDto) throws NotFoundException {
        Category category = categoryService.getCategoryById(dishDto.getCategoryId());

        if (category == null) {
            throw new NotFoundException(String.format("Category with Id %s was not found!", dishDto.getCategoryId()));
        }
        if (dishRepository.existsBySearchId(dishDto.getSearchId())) {
            throw new IllegalArgumentException(String.format("Dish with searchId %s already exists!", dishDto.getSearchId()));
        }

        ValidationUtil.validateImageUrl(dishDto.getImageURL());

        Dish dish = new Dish();
        dish.setSearchId(dishDto.getSearchId());
        dish.setDescription(dishDto.getDescription());
        dish.setImageURL(dishDto.getImageURL());
        dish.setName(dishDto.getName());
        dish.setCategory(category);
        dish.setPrice(dishDto.getPrice());
        dish.setCheckDate(TimeUtil.formatLocalDateTime(new LocalDateTime()));
        dish.setMinSales(dishDto.getMinSales());
        dish.setMaxSales(dishDto.getMaxSales());

        dishRepository.save(dish);
    }

    @Override
    public DishDTO getDishDto(Dish dish) {
        DishDTO dishDto = new DishDTO();

        dishDto.setId(dish.getId());
        dishDto.setName(dish.getName());
        dishDto.setSearchId(dish.getSearchId());
        dishDto.setDescription(dish.getDescription());
        dishDto.setImageURL(dish.getImageURL());
        dishDto.setCategoryId(dish.getCategory().getId());
        dishDto.setPrice(dish.getPrice());
        dishDto.setMinSales(dish.getMinSales());
        dishDto.setMaxSales(dish.getMaxSales());

        return dishDto;
    }

    @Override
    public Dish getDishBySearchId(String searchId) {
        return dishRepository.findDishBySearchId(searchId);
    }

    @Override
    public List<DishDTO> getAllDishes() {
        List<Dish> allDishes = dishRepository.findAll();
        List<DishDTO> dishDTOS = new ArrayList<>();

        for (Dish dish : allDishes) {
            dishDTOS.add(getDishDto(dish));
        }
        return dishDTOS;
    }


    @Override
    public void update(DishDTO dishDto) throws NotFoundException {
        Dish dish = dishRepository.findDishBySearchId(dishDto.getSearchId());

        if (dish == null) {
            throw new NotFoundException(String.format("Dish with searchId %s was not found!", dishDto.getSearchId()));
        }

        ValidationUtil.validateImageUrl(dishDto.getImageURL());

        dish.setSearchId(dishDto.getSearchId());
        dish.setDescription(dishDto.getDescription());
        dish.setImageURL(dishDto.getImageURL());
        dish.setName(dishDto.getName());
        dish.setPrice(dishDto.getPrice());
        dish.setMinSales(dishDto.getMinSales());
        dish.setMaxSales(dishDto.getMaxSales());
        dish.setCostPrice(dishDto.getCostPrice());

        dishRepository.save(dish);
    }

    @Override
    public Dish findDishById(Long dishId) {
        return dishRepository.getById(dishId);
    }

    @Override
    public void deleteDishById(Long dishId) throws NotFoundException {
        if (!dishRepository.existsById(dishId)) {
            throw new NotFoundException(String.format("Dish with Id %s was not found!", dishId));
        }

        dishRepository.deleteById(dishId);
    }

    @Override
    public void checkPrices() {
        List<Dish> dishes = dishRepository.findAll();

        LocalDateTime todayDate = TimeUtil.formatLocalDateTime(new LocalDateTime());
        for (Dish dish : dishes) {
            List<Order> orders = orderRepository.findAllByCreatedDateBetween(dish.getCheckDate(), todayDate);
            double count = 0;
            for (var order : orders) {
                for (var orderUnit : order.getOrderUnits()) {
                    if (orderUnit.getDish().getId() == dish.getId()) {
                        count += orderUnit.getQuantity();
                    }
                }
            }
            if (count > dish.getMaxSales()) {
                dish.setPrice(Math.ceil(dish.getPrice() * 1.1));
            } else if (count < dish.getMinSales()) {
                Double newPrice = Math.max(Math.ceil(dish.getPrice() / 1.1), dish.getCostPrice() * 1.05);
                dish.setPrice(newPrice);
            }
            dish.setCheckDate(todayDate);
            dishRepository.save(dish);
        }
    }
}
