package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.table.TableTimeDto;
import com.example.project.dto.table.UpdateTableDto;
import com.example.project.model.Reservation;
import com.example.project.model.Table;
import com.example.project.service.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/table")
public class TableController {

    @Autowired
    ITableService tableService;

    //todo add getTablesByTime


    @GetMapping("/")
    public ResponseEntity<List<TableTimeDto>> getAllTables() {
        List<TableTimeDto> tableTimeDtos =  tableService.getAllTablesDto();
        return new ResponseEntity<>(tableTimeDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTable(@RequestBody Table table) throws Exception {
        tableService.create(table);
        return new ResponseEntity<>(new ApiResponse(true, "Table has been created!"), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateTable(@RequestBody UpdateTableDto updateTableDto) throws Exception {
        tableService.update(updateTableDto);
        return new ResponseEntity<>(new ApiResponse(true, "Table has been created!"), HttpStatus.CREATED);
    }


    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() throws Exception {
        tableService.create(new Table("searchId", 4, false));
        return new ResponseEntity<>(new ApiResponse(true, "Table has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/test1")
    public ResponseEntity<Reservation> test1() {
        List<Table> tables = tableService.getAllTables();
        Reservation reservation = new Reservation();
        reservation.setTables(tables);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @Scheduled(cron = "${cron.check.table.reservation.time1}")
    private void test2() {
        getAllTables();
    }


}
