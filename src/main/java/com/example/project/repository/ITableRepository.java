package com.example.project.repository;

import com.example.project.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITableRepository extends JpaRepository<Table, String> {

    Boolean existsBySearchId(String searchId);

    Table findBySearchId(String searchId);

    List<Table> findBySearchIdIn(List<String> searchIds);

    List<Table> getTablesByMinNumberOfSeats(int minNumberOfSeats);

    void deleteBySearchId(String searchId);
}
