package com.example.project.repository;

import com.example.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, String> {

    Category findByCategoryNameEn(String categoryName);

    Category findByCategoryNameUa(String categoryName);

    boolean existsByCategoryNameEn(String categoryName);

    boolean existsByCategoryNameUa(String categoryName);

    Category getById(String id);

}
