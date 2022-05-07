package com.example.project.repository;

import com.example.project.model.Order;
import com.example.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findProductByCode(String code);

}
