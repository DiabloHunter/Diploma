package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ResponseDto;
import com.example.project.dto.user.SignInDto;
import com.example.project.dto.user.SignInReponseDto;
import com.example.project.dto.user.SignupDto;
import com.example.project.dto.user.UserDto;
import com.example.project.model.Category;
import com.example.project.model.User;
import com.example.project.service.AuthenticationService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;

    // two apis

    // signup

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupDto signupDto) {
        return userService.signUp(signupDto);
    }


    // signin

    @PostMapping("/signin")
    public SignInReponseDto signIn(@RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }



    @GetMapping("/")
    public UserDto getUser(@RequestParam("token") String token){
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        return userService.getUserDto(user);
    }

    @PostMapping("/update/")
    public ResponseEntity<ApiResponse> updateCategory(@RequestParam("token") String token, @RequestBody User changedUser) {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        userService.editUser(user, changedUser);
        return new ResponseEntity<>(new ApiResponse(true, "User has been updated"), HttpStatus.OK);
    }

}
