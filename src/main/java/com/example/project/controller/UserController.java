package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.user.UpdateUserDto;
import com.example.project.dto.user.UserDTO;
import com.example.project.model.User;
import com.example.project.service.IUserService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    IUserService userService;

    private static final Logger LOG = LogManager.getLogger(UserController.class);

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @GetMapping("/")
    public ResponseEntity<UserDTO> getUser(@RequestParam("userEmail") String email) {
        UserDTO userDTO = userService.getUserDto(userService.getUserByEmail(email));
        if (userDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CASHIER')")
    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateUser(@RequestParam("userEmail") String userEmail,
                                                  @RequestBody UpdateUserDto changedUser) throws NotFoundException {
        User user = userService.getUserByEmail(userEmail);
        if (user == null) {
            LOG.error(String.format("User with email %s was not found!", userEmail));
            return new ResponseEntity<>(new ApiResponse(false,
                    String.format("User with email %s was not found!", userEmail)), HttpStatus.NOT_FOUND);
        }
        userService.update(userEmail, changedUser);

        LOG.info(String.format("User with email %s has been updated", userEmail));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("User with email %s has been updated!", userEmail)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/backup")
    public ResponseEntity<ApiResponse> backupDB() {
        try {
            userService.backup();
        } catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Database has been successfully backuped!"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/restore")
    public ResponseEntity<ApiResponse> restoreDB() {
        try {
            userService.restore();
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Database has been successfully restored!"), HttpStatus.OK);
    }
}
