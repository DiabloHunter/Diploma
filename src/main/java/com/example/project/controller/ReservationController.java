package com.example.project.controller;

import com.example.project.dto.reservation.ReservationDTO;
import com.example.project.service.IReservationService;
import com.example.project.util.TimeUtil;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    IReservationService reservationService;

    @GetMapping("/test")
    public void test() {
        reservationService.createReservation(new ReservationDTO("searchId",
                TimeUtil.parseDateTime(new LocalDateTime().minusHours(3)),
                TimeUtil.parseDateTime(new LocalDateTime().plusHours(5)),
                5, "haha@gmail.com", "test", List.of("searchId")));
        //return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

}
