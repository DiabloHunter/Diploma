package com.example.project.service;

import com.example.project.dto.ResponseDto;
import com.example.project.dto.user.SignInDto;
import com.example.project.dto.user.SignInReponseDto;
import com.example.project.dto.user.SignupDto;
import com.example.project.dto.user.UserDto;
import com.example.project.model.User;

import javax.transaction.Transactional;
import java.io.IOException;

public interface IUserService {
    @Transactional
    ResponseDto signUp(SignupDto signupDto);

    SignInReponseDto signIn(SignInDto signInDto);

    SignInReponseDto signInMob(SignInDto signInDto);

    UserDto getUserDto(User user);

    User getUserByEmail(String userEmail);

    void editUser(User updatedUser, User updateUser);

    boolean backup()
                throws IOException, InterruptedException;

    boolean restore()
                throws IOException;
}
