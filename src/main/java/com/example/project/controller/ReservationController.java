package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.reservation.ReservationDTO;
import com.example.project.dto.reservation.UpdateReservationDto;
import com.example.project.service.IReservationService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    IReservationService reservationService;

    private static final Logger LOG = LogManager.getLogger(ReservationController.class);

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            reservationService.createReservation(reservationDTO);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Reservation has been created for user %s!", reservationDTO.getUserEmail())), HttpStatus.CREATED);
    }

    @GetMapping("/update")
    public ResponseEntity<ApiResponse> updateReservation(@RequestBody UpdateReservationDto updateReservationDto) {
        try {
            reservationService.updateReservation(updateReservationDto);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Reservation has been updated for user %s!", updateReservationDto.getUserEmail())), HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelReservation(@RequestParam("id") Long id) {
        try {
            reservationService.cancelReservation(id);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Reservation has been canceled!"), HttpStatus.OK);
    }

}
