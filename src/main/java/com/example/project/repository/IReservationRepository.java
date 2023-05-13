package com.example.project.repository;

import com.example.project.model.Reservation;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, String> {

    @Query(value = "SELECT * FROM RESERVATIONS u WHERE u.end_time<=:to",
            nativeQuery = true)
    List<Reservation> findByEndTimeIs(@Param("to") LocalDateTime endTime);

    @Query(value = "SELECT * FROM RESERVATIONS u WHERE u.user_id = :userId AND u.is_active is true",
            nativeQuery = true)
    List<Reservation> findByUserIdAndActive(@Param("userId") String userId);

    @Query(value = "SELECT * FROM RESERVATIONS u WHERE u.start_time < :from AND u.is_active is true",
            nativeQuery = true)
    List<Reservation> findByStartTimeAndActive(@Param("from") LocalDateTime startTime);

    @Query(value = "SELECT * FROM RESERVATIONS u WHERE u.start_time >= :from AND u.end_time<=:to AND u.is_active is true",
            nativeQuery = true)
    List<Reservation> getReservationByTime(@Param("from") LocalDateTime start,
                                           @Param("to") LocalDateTime end);

    Reservation getById(String id);

}
