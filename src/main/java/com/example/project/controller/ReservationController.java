package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.reservation.ReservationDTO;
import com.example.project.dto.reservation.UpdateReservationDto;
import com.example.project.service.IReservationService;
import com.example.project.util.TimeUtil;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOG = LogManager.getLogger(ReservationController.class);

    @GetMapping("/create")
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

    @GetMapping("/test")
    public void test() throws NotFoundException {
        LocalDateTime localDateTime = TimeUtil.formatLocalDateTime(new LocalDateTime());
        System.out.println(localDateTime);
        System.out.println(TimeUtil.formatDate(localDateTime.minusDays(1).toDate()));
//        reservationService.createReservation(new ReservationDTO(
//                TimeUtil.formatLocalDateTime(new LocalDateTime().plusDays(2).minusHours(3)),
//                TimeUtil.formatLocalDateTime(new LocalDateTime().plusDays(2).plusHours(5)),
//                5, "haha@gmail.com", "test", List.of(1L, 4L, 13L)));
        //return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

}
