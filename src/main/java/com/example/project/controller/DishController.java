package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.dish.DishDTO;
import com.example.project.dto.filter.FilterDishDTO;
import com.example.project.model.Dish;
import com.example.project.service.IDishService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dish")
public class DishController {

    @Autowired
    IDishService dishService;

    private static final Logger LOG = LogManager.getLogger(DishController.class);

    @GetMapping("/")
    public ResponseEntity<List<DishDTO>> getDishes() {
        List<DishDTO> dishes = dishService.getAllDishes();
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/getBySearchId/")
    public ResponseEntity<DishDTO> getDishBySearchId(@RequestParam String searchId) {
        Dish dish = dishService.getDishBySearchId(searchId);

        if (dish == null) {
            LOG.warn(String.format("Dish with searchId %s was not found!", searchId));
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        DishDTO dishDTO = dishService.getDishDto(dish);
        return new ResponseEntity<>(dishDTO, HttpStatus.OK);
    }

    @GetMapping("/getFilteredDishes/")
    public ResponseEntity<List<Dish>> getFilteredDishes(@RequestBody FilterDishDTO filterDishDTO) {
        List<Dish> dishes = dishService.getFilteredDishes(filterDishDTO);

        if (dishes == null) {
            LOG.warn(String.format("Dishes with given filters %s was not found!", filterDishDTO));
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createDish(@RequestBody DishDTO dishDto) throws NotFoundException {
        dishService.create(dishDto);

        LOG.info(String.format("Dish with searchId %s has been created!", dishDto.getSearchId()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Dish with searchId %s has been created!", dishDto.getSearchId())), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateDish(@RequestBody DishDTO dishDto) throws NotFoundException {
        dishService.update(dishDto);
        LOG.info(String.format("Dish with Id %s has been updated", dishDto.getId()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Dish with Id %s has been updated", dishDto.getId())), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam String id) throws NotFoundException {
        dishService.deleteDishById(id);
        LOG.info(String.format("Dish with Id %s has been deleted", id));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Dish with Id %s has been deleted", id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/checkPrices")
    public ResponseEntity<ApiResponse> checkPrices() {
        LOG.info("Check price process started.");
        dishService.checkPrices();
        LOG.info("Check price has been done.");

        return new ResponseEntity<>(new ApiResponse(true, "Check price has been done."), HttpStatus.OK);
    }

    @Scheduled(cron = "${cron.check.prices}")
    public ResponseEntity<ApiResponse> checkPricesSchedule() {
        LOG.info("Check price process started.");
        dishService.checkPrices();
        LOG.info("Check price has been done.");

        return new ResponseEntity<>(new ApiResponse(true, "Check price has been done."), HttpStatus.OK);
    }
}
