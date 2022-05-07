package com.example.project.repository;

import com.example.project.model.Cart;
import com.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    void deleteAllByUser(User user);


}
