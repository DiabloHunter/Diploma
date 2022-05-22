package com.example.project.repository;

import com.example.project.model.Category;
import com.example.project.model.Order;
import com.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser(User user);

    List<Order> findAllByCreatedDateBetween(Date dateStart, Date dateEnd);

    Order findOrderById(Integer id);

}
