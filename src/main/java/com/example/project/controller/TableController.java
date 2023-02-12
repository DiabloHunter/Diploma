package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.table.CreateTableDto;
import com.example.project.dto.table.UpdateTableDto;
import com.example.project.service.ITableService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table")
public class TableController {

    @Autowired
    ITableService tableService;

    //todo add getTablesByTime
    private static final Logger LOG = LogManager.getLogger(TableController.class);

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTable(@RequestBody CreateTableDto createTableDto) {
        try {
            tableService.create(createTableDto);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        LOG.info(String.format("Table with searchId %s has been created!", createTableDto.getSearchId()));
        return new ResponseEntity<>(new ApiResponse(true, String.format("Table with searchId %s has been created!",
                createTableDto.getSearchId())), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateTable(@RequestBody UpdateTableDto updateTableDto) {
        try {
            tableService.update(updateTableDto);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        LOG.info(String.format("Table with searchId %s has been updated!", updateTableDto.getPreviousSearchId()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Table with searchId %s has been updated!", updateTableDto.getPreviousSearchId())), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam String searchId) {
        if (!tableService.existBySearchId(searchId)) {
            return new ResponseEntity<>(new ApiResponse(false, "Table does not exists"), HttpStatus.NOT_FOUND);
        }
        try {
            tableService.deleteTable(searchId);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }

        LOG.info(String.format("Table with searchId %s has been deleted!", searchId));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Table with searchId %s has been deleted!", searchId)), HttpStatus.OK);
    }
}
