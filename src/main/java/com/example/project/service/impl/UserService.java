package com.example.project.service.impl;

import com.example.project.dto.user.UpdateUserDto;
import com.example.project.dto.user.UserDTO;
import com.example.project.model.User;
import com.example.project.repository.IUserRepository;
import com.example.project.service.IUserService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    private static final Logger LOG = LogManager.getLogger(UserService.class);

    @Override
    public UserDTO getUserDto(User user) {
        UserDTO userDto = new UserDTO();
        if (user == null) {
            return null;
        }

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRating(user.getRating());
        return userDto;
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void create(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(String userEmail, UpdateUserDto updateUser) throws NotFoundException {
        if (userEmail == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }

        User updatedUser = userRepository.findByEmail(userEmail);
        if (updatedUser == null) {
            throw new NotFoundException(String.format("User with email %s was not found!", userEmail));
        }

        if (updateUser.getEmail() != null && !userEmail.equals(updateUser.getEmail())) {
            updatedUser.setEmail(updateUser.getEmail());
        }

        if (updateUser.getPassword() != null && !updatedUser.getPassword().equals(updateUser.getPassword())) {
            updatedUser.setPassword(encoder.encode(updateUser.getPassword()));
        }
        userRepository.save(updatedUser);
    }

    @Override
    public void update(User updateUser) {
        if (updateUser != null) {
            userRepository.save(updateUser);
        } else {
            LOG.error("Update user mustn't be null!");
        }
    }
}
