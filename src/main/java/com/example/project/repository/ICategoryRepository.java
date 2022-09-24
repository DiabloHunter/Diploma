package com.example.project.repository;

import com.example.project.model.Category;
import com.example.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByCategoryName(String categoryName);
}
