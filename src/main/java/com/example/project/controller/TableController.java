package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.table.CreateTableDto;
import com.example.project.dto.table.UpdateTableDto;
import com.example.project.model.Table;
import com.example.project.service.ITableService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/table")
public class TableController {

    @Autowired
    ITableService tableService;

    private static final Logger LOG = LogManager.getLogger(TableController.class);

    @Async
    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<Table>>> getAllTables() {
        List<Table> tables = tableService.getAllTables();
        return CompletableFuture.completedFuture(ResponseEntity.ok(tables));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTable(@RequestBody CreateTableDto createTableDto) {
        tableService.create(createTableDto);

        LOG.info(String.format("Table with searchId %s has been created!", createTableDto.getSearchId()));
        return new ResponseEntity<>(new ApiResponse(true, String.format("Table with searchId %s has been created!",
                createTableDto.getSearchId())), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateTable(@RequestBody UpdateTableDto updateTableDto) throws NotFoundException {
        tableService.update(updateTableDto);

        LOG.info(String.format("Table with searchId %s has been updated!", updateTableDto.getPreviousSearchId()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Table with searchId %s has been updated!", updateTableDto.getPreviousSearchId())), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam String searchId) throws NotFoundException {
        if (!tableService.existBySearchId(searchId)) {
            return new ResponseEntity<>(new ApiResponse(false, "Table does not exists"), HttpStatus.NOT_FOUND);
        }
        tableService.deleteTable(searchId);

        LOG.info(String.format("Table with searchId %s has been deleted!", searchId));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Table with searchId %s has been deleted!", searchId)), HttpStatus.OK);
    }
}
