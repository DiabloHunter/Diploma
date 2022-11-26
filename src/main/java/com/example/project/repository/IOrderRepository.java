package com.example.project.repository;

import com.example.project.model.Order;
import com.example.project.model.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);

    List<Order> findAllByCreatedDateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);

}
