package com.example.project.controller;

import com.example.project.model.Reservation;
import com.example.project.model.Table;
import com.example.project.service.ITableService;
import com.example.project.util.TimeUtil;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/table")
public class TableController {

    @Autowired
    ITableService tableService;

    @GetMapping("/test")
    public void test() throws Exception {
        tableService.createTable(new Table("searchId", 4, false));
        //return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    @GetMapping("/test1")
    public ResponseEntity<Reservation> test1() {
        List<Table> tables = tableService.getAllTables();
        Reservation reservation = new Reservation();
        reservation.setTables(tables);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }


}
