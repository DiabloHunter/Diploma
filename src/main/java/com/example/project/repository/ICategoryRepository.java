package com.example.project.repository;

import com.example.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, String> {

    Category findByNameEn(String categoryEn);

    Category findByNameUa(String categoryUa);

    boolean existsByNameEn(String categoryEn);

    boolean existsByNameUa(String categoryUa);

    Category getById(String id);

}
