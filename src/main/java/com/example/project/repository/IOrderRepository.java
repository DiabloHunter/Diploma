package com.example.project.repository;

import com.example.project.model.Order;
import com.example.project.model.User;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByUser(User user);

    List<Order> findAllByCreatedDateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);

    @Query(value = "SELECT * FROM ORDERS u WHERE u.user_id = :userId AND u.created_date BETWEEN :start AND :end",
            nativeQuery = true)
    List<Order> findByUserAndCreatedDate(@Param("userId") String userId,
                                         @Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

}
