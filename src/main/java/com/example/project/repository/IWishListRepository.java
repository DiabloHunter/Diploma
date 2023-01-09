package com.example.project.repository;


import com.example.project.model.Dish;
import com.example.project.model.User;
import com.example.project.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWishListRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findAllByUserOrderByCreatedDateDesc(User user);

    Wishlist findByUserAndDish(User user, Dish dish);

    void deleteByUserAndDish(User user, Dish dish);

}
