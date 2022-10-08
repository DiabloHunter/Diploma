package com.example.project.repository;

import com.example.project.model.OrderUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderUnitRepository extends JpaRepository<OrderUnit, Long> {
}
