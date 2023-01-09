package com.example.project.controller;


import com.example.project.common.ApiResponse;
import com.example.project.dto.dish.DishDTO;
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

    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/getBySearchId/")
    public ResponseEntity<DishDTO> getDishBySearchId(@RequestBody DishDTO dishDto) {
        Dish dish = dishService.getDishBySearchId(dishDto.getSearchId());

        if (dish == null) {
            LOG.warn(String.format("Dish with searchId %s was not found!", dishDto.getSearchId()));
            return new ResponseEntity<>(null,  HttpStatus.OK);
        }

        DishDTO dishDTO = dishService.getDishDto(dish);
        return new ResponseEntity<>(dishDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createDish(@RequestBody DishDTO dishDto) {
        try {
            dishService.create(dishDto);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        LOG.info(String.format("Dish with name %s has been created!", dishDto.getSearchId()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Dish with name %s has been created!", dishDto.getName())), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateDish(@RequestBody DishDTO dishDto) {
        try {
            dishService.update(dishDto);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        LOG.info(String.format("Dish with Id %s has been updated", dishDto.getId()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Dish with Id %s has been updated", dishDto.getId())), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam Long id) {
        try {
            dishService.deleteDishById(id);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }

        LOG.info(String.format("Dish with Id %s has been deleted", id));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Dish with Id %s has been deleted", id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('MANAGER')")
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
