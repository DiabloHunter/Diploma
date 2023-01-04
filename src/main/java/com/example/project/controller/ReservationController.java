package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.reservation.ReservationDTO;
import com.example.project.dto.reservation.UpdateReservationDto;
import com.example.project.service.IReservationService;
import com.example.project.util.TimeUtil;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    IReservationService reservationService;

    @GetMapping("/create")
    public ResponseEntity<ApiResponse> createReservation(@RequestBody ReservationDTO reservationDTO) {
        reservationService.createReservation(reservationDTO);
        return new ResponseEntity<>(new ApiResponse(true, "Reservation has been created!"), HttpStatus.CREATED);
    }

    @GetMapping("/update")
    public ResponseEntity<ApiResponse> updateReservation(@RequestBody UpdateReservationDto updateReservationDto) {
        reservationService.updateReservation(updateReservationDto);
        return new ResponseEntity<>(new ApiResponse(true, "Reservation has been updated!"), HttpStatus.OK);
    }

    @GetMapping("/test")
    public void test() {
        reservationService.createReservation(new ReservationDTO(
                TimeUtil.formatLocalDateTime(new LocalDateTime().plusDays(2).minusHours(3)),
                TimeUtil.formatLocalDateTime(new LocalDateTime().plusDays(2).plusHours(5)),
                5, "haha@gmail.com", "test", List.of(1L, 4L, 13L)));
        //return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

}
