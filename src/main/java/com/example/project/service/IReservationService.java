package com.example.project.service;

import com.example.project.dto.reservation.ReservationDTO;
import com.example.project.model.Table;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Map;

public interface IReservationService {
    void createReservation(ReservationDTO reservationDTO);

    Map<String, List<Table>> getFreeTables(LocalDateTime start, LocalDateTime end);
}
