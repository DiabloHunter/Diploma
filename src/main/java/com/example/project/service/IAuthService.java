package com.example.project.service;

import com.example.project.JWT.payload.request.LoginRequest;
import com.example.project.JWT.payload.request.SignupRequest;
import com.example.project.JWT.payload.response.JwtResponse;
import com.example.project.JWT.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

public interface IAuthService {

    @Transactional
    void signUp(SignupRequest signupRequest);

    ResponseEntity<JwtResponse> signIn(LoginRequest loginRequest);

//    SignInResponseDTO signInMob(SignInDTO signInDto);
}
