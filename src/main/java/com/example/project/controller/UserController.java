package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ResponseDTO;
import com.example.project.dto.user.SignInDTO;
import com.example.project.dto.user.SignInResponseDTO;
import com.example.project.dto.user.SignupDTO;
import com.example.project.dto.user.UserDTO;
import com.example.project.model.User;
import com.example.project.service.IUserService;
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
    public ResponseDTO signup(@RequestBody SignupDTO signupDto) {
        return userService.signUp(signupDto);
    }

    @PostMapping("/signin")
    public SignInResponseDTO signIn(@RequestBody SignInDTO signInDto) {
        return userService.signIn(signInDto);
    }

//    @GetMapping("/signinMob/{email}&{password}")
//    public SignInReponseDto signInMob(@PathVariable("email") String email, @PathVariable("password") String password) {
//        SignInDto signInDto = new SignInDto(email, password);
//        return userService.signInMob(signInDto);
//    }

    @GetMapping("/")
    public UserDTO getUser(@RequestBody UserDTO userDto) {
        // find the user
        User user = userService.getUserByEmail(userDto.getEmail());

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
