package com.example.project.service;

import com.example.project.dto.user.UserDTO;
import com.example.project.model.User;

import java.io.IOException;

public interface IUserService {

    UserDTO getUserDto(User user);

    User getUserByEmail(String userEmail);

    Boolean existsByEmail(String email);

    void update(User updatedUser, User updateUser);

    void update(User updateUser);

    boolean backup()
            throws IOException, InterruptedException;

    boolean restore()
            throws IOException;

    void create(User user);
}
