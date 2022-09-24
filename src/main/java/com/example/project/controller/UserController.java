package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ResponseDto;
import com.example.project.dto.user.SignInDto;
import com.example.project.dto.user.SignInReponseDto;
import com.example.project.dto.user.SignupDto;
import com.example.project.dto.user.UserDto;
import com.example.project.model.User;
import com.example.project.service.IUserService;
import com.example.project.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupDto signupDto) {
        return userService.signUp(signupDto);
    }

    @PostMapping("/signin")
    public SignInReponseDto signIn(@RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }

    @GetMapping("/signinMob/{email}&{password}")
    public SignInReponseDto signInMob(@PathVariable("email") String email, @PathVariable("password") String password) {
        SignInDto signInDto = new SignInDto(email, password);
        return userService.signInMob(signInDto);
    }

    @GetMapping("/")
    public UserDto getUser(@RequestParam("userEmail") String userEmail) {
        // find the user
        User user = userService.getUserByEmail(userEmail);

        return userService.getUserDto(user);
    }

    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateCategory(@RequestParam("userEmail") String userEmail, @RequestBody User changedUser) {
        User user = userService.getUserByEmail(userEmail);
        userService.editUser(user, changedUser);
        return new ResponseEntity<>(new ApiResponse(true, "User has been updated"), HttpStatus.OK);
    }

    @PostMapping("/backup")
    public ResponseEntity<ApiResponse> backupDB() {
        try {
            userService.backup();
        } catch (IOException | InterruptedException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Error:" + e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Database has been successfully backuped!"), HttpStatus.OK);
    }

    @PostMapping("/restore")
    public ResponseEntity<ApiResponse> restoreDB() {
        try {
            userService.restore();
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Error:" + e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Database has been successfully restored!"), HttpStatus.OK);
    }


}
