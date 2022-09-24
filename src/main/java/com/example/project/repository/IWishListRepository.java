package com.example.project.repository;


import com.example.project.model.Product;
import com.example.project.model.User;
import com.example.project.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWishListRepository extends JpaRepository<WishList, Long> {

    List<WishList> findAllByUserOrderByCreatedDateDesc(User user);

    WishList findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

}
