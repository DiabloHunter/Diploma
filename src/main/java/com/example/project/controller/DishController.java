package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.dish.DishDTO;
import com.example.project.dto.dish.DishIoTDTO;
import com.example.project.model.Category;
import com.example.project.model.Dish;
import com.example.project.service.ICategoryService;
import com.example.project.service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    @Autowired
    IDishService dishService;

    @Autowired
    ICategoryService categoryService;

    private static final String CHECK_PRICES_CRON = "0 58 13 ? * SAT";

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createDish(@RequestBody DishDTO dishDto) {
        Category category = categoryService.getCategoryById(dishDto.getCategoryId());
        if (category == null) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
        }
        try {
            dishService.create(dishDto, category);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "dish has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<DishDTO>> getDishes() {
        List<DishDTO> dishes = dishService.getAllDishes();
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/getBySearchId/")
    public ResponseEntity<DishIoTDTO> getDishBySearchId(@RequestBody DishDTO dishDto) {
        Dish dish = dishService.getDishBySearchId(dishDto.getSearchId());
        DishIoTDTO dishIoTDTO = new DishIoTDTO(dish.getId(), dish.getSearchId(), dish.getName(),
                dish.getPrice(), dish.getDescription());
        return new ResponseEntity<>(dishIoTDTO, HttpStatus.OK);
    }


    @Scheduled(cron = CHECK_PRICES_CRON)
    public ResponseEntity<ApiResponse> checkPricesSchedule() {
        dishService.checkPrices();
        return new ResponseEntity<>(new ApiResponse(true, "Date for all dishes have been changed"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/checkPrices")
    public ResponseEntity<ApiResponse> checkPrices() {
        dishService.checkPrices();
        return new ResponseEntity<>(new ApiResponse(true, "Date for all dishes have been changed"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateDish(@RequestBody DishDTO dishDto) {
        Category category = categoryService.getCategoryById(dishDto.getCategoryId());
        if (category == null) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
        }
        try {
            dishService.update(dishDto);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(true, "dish has been updated"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam Long id) {
        if (dishService.findDishById(id) == null) {
            return new ResponseEntity<>(new ApiResponse(false, "dish does not exists"), HttpStatus.NOT_FOUND);
        }
        dishService.deleteDishById(id);
        return new ResponseEntity<>(new ApiResponse(true, "dish has been deleted"), HttpStatus.OK);
    }

}
