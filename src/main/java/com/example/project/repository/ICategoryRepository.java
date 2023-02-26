package com.example.project.repository;

import com.example.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, String> {

    Category findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);

    Category getById(String id);

}
