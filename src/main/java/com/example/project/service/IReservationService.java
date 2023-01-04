package com.example.project.service;

import com.example.project.dto.reservation.ReservationDTO;
import com.example.project.dto.reservation.UpdateReservationDto;
import com.example.project.dto.table.TableTimeDto;
import org.joda.time.LocalDate;

import java.util.List;

public interface IReservationService {

    void createReservation(ReservationDTO reservationDTO);

    List<TableTimeDto> getFreeTables(LocalDate date);

    void updateReservation(UpdateReservationDto updateReservationDto);
}
