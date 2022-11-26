package com.example.project.repository;

import com.example.project.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDishRepository extends JpaRepository<Dish, Long> {

    Optional<Dish> findDishBySearchId(String searchId);

}