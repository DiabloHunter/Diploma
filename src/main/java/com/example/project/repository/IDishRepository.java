package com.example.project.repository;

import com.example.project.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDishRepository extends JpaRepository<Dish, Long> {

    Dish findDishBySearchId(String searchId);

    boolean existsBySearchId(String searchId);

    Dish getById(Long id);

}