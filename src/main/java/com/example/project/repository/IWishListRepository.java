package com.example.project.repository;


import com.example.project.model.Dish;
import com.example.project.model.User;
import com.example.project.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWishListRepository extends JpaRepository<WishList, Long> {

    List<WishList> findAllByUserOrderByCreatedDateDesc(User user);

    WishList findByUserAndDish(User user, Dish dish);

    void deleteByUserAndDish(User user, Dish dish);

}
