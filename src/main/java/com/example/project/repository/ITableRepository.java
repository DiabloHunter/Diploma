package com.example.project.repository;

import com.example.project.model.Table;
import org.joda.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITableRepository extends JpaRepository<Table, Long> {

    Boolean existsBySearchId(String searchId);

    Optional<Table> findBySearchId(String searchId);

    List<Table> findBySearchIdIn(List<String> searchIds);

    List<Table> findBySearchIdInAndReservedFromAfterAndReservedToBefore(List<String> searchIds, LocalTime start, LocalTime end);

    @Query(value = "SELECT * FROM TABLES u WHERE u.search_id in :ids AND u.reserved_from >= :from AND u.reserved_to<=:to",
            nativeQuery = true)
    List<Table> getTablesBySearchIdAndTime(@Param("ids") List<String> searchIds, @Param("from") LocalTime start,
                                           @Param("to") LocalTime end);

    void deleteBySearchId(String searchId);

}
