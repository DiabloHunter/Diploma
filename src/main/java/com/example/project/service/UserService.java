package com.example.project.service;


import com.example.project.dto.ResponseDto;
import com.example.project.dto.user.SignInDto;
import com.example.project.dto.user.SignInReponseDto;
import com.example.project.dto.user.SignupDto;
import com.example.project.dto.user.UserDto;
import com.example.project.exceptions.AuthenticationFailException;
import com.example.project.exceptions.CustomException;
import com.example.project.model.AuthenticationToken;
import com.example.project.model.Category;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {
        // check if user is already present
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // we have an user
            throw new CustomException("user already present");
        }


        // hash the password

        String encryptedpassword = signupDto.getPassword();

        try {
            encryptedpassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(),
                signupDto.getEmail(), encryptedpassword);

        userRepository.save(user);

        // save the user

        // create the token

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);

        authenticationService.saveConfirmationToken(authenticationToken);

        ResponseDto responseDto = new ResponseDto("success", "user created succesfully");
        return responseDto;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SignInReponseDto signIn(SignInDto signInDto) {
        // find user by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("user is not valid");
        }

        // hash the password
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException("wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // compare the password in DB
        // if password match
        AuthenticationToken token = authenticationService.getToken(user);

        // retrive the token

        if (Objects.isNull(token)) {
            throw new CustomException("token is not present");
        }

        return new SignInReponseDto("sucess", token.getToken(), user.getEmail());

        // return response
    }

    public UserDto getUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public void editUser(User updatedUser, User updateUser) {
        updatedUser.setEmail(updateUser.getEmail());
        updatedUser.setFirstName(updateUser.getFirstName());
        updatedUser.setLastName(updateUser.getLastName());

        String encryptedPassword = updateUser.getPassword();

        try {
            encryptedPassword = hashPassword(updateUser.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        updatedUser.setPassword(encryptedPassword);
        userRepository.save(updatedUser);
    }
}
