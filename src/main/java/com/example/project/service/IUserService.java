package com.example.project.service;

import com.example.project.dto.user.UpdateUserDto;
import com.example.project.dto.user.UserDTO;
import com.example.project.model.User;
import javassist.NotFoundException;

import java.io.IOException;

public interface IUserService {

    UserDTO getUserDto(User user);

    User getUserByEmail(String userEmail);

    Boolean existsByEmail(String email);

    void update(String userEmail, UpdateUserDto updateUser) throws NotFoundException;

    void update(User updateUser);

    boolean backup()
            throws IOException, InterruptedException;

    boolean restore()
            throws IOException;

    void create(User user);
}
