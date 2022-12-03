package com.example.project.repository;

import com.example.project.model.Reservation;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByEndTimeIs(LocalDateTime endTime);

    @Query(value = "SELECT * FROM RESERVATIONS u WHERE u.start_time >= :from AND u.end_time<=:to AND u.is_active is true",
            nativeQuery = true)
    List<Reservation> getReservationByTime(@Param("from") LocalDateTime start,
                                           @Param("to") LocalDateTime end);

}
