package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.service.IMySQLService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/api/mysql")
@RestController
public class MySQLController {

    private static final Logger LOG = LogManager.getLogger(MySQLController.class);

    @Autowired
    private IMySQLService mySQLService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/backup")
    public ResponseEntity<ApiResponse> backupDB() {
        try {
            boolean result = mySQLService.backup();
            if (result) {
                return new ResponseEntity<>(new ApiResponse(true, "Database back-up has been successfully created!"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, "Error during creating database back-up"), HttpStatus.OK);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/restore")
    public CompletableFuture<ResponseEntity<ApiResponse>> restoreDB() {
        try {
            String backupName = "DbBackup_27-05-2023_01-59-33.sql";
            boolean result = mySQLService.restore(backupName);
            if (result) {
                return CompletableFuture.completedFuture(ResponseEntity.ok(
                        new ApiResponse(true, "Restoring database back-up with name " + backupName +
                                " has been successfully completed!")));
            } else {
                return CompletableFuture.completedFuture(ResponseEntity.ok(
                        new ApiResponse(false, "Error during restoring database back-up with name: " + backupName)));
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.internalServerError().body(new ApiResponse(false, e.getMessage())));
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/getBackupNames")
    public ResponseEntity<List<String>> getAllBackupNames() {
        try {
            List<String> backupNames = mySQLService.getBackupNames();
            return new ResponseEntity<>(backupNames, HttpStatus.OK);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
