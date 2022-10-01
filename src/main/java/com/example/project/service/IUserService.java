package com.example.project.service;

import com.example.project.dto.ResponseDTO;
import com.example.project.dto.user.SignInDTO;
import com.example.project.dto.user.SignInResponseDTO;
import com.example.project.dto.user.SignupDTO;
import com.example.project.dto.user.UserDTO;
import com.example.project.model.User;

import javax.transaction.Transactional;
import java.io.IOException;

public interface IUserService {
    @Transactional
    ResponseDTO signUp(SignupDTO signupDto);

    SignInResponseDTO signIn(SignInDTO signInDto);

    SignInResponseDTO signInMob(SignInDTO signInDto);

    UserDTO getUserDto(User user);

    User getUserByEmail(String userEmail);

    void editUser(User updatedUser, User updateUser);

    boolean backup()
                throws IOException, InterruptedException;

    boolean restore()
                throws IOException;
}
