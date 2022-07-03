package com.example.project.service;


import com.example.project.dto.ResponseDto;
import com.example.project.dto.user.SignInDto;
import com.example.project.dto.user.SignInReponseDto;
import com.example.project.dto.user.SignupDto;
import com.example.project.dto.user.UserDto;
import com.example.project.exceptions.AuthenticationFailException;
import com.example.project.exceptions.CustomException;
import com.example.project.model.AuthenticationToken;
import com.example.project.model.User;
import com.example.project.model.UserRoles;
import com.example.project.repository.UserRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(),signupDto.getEmail(),
                encryptedpassword, UserRoles.USER);

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
        return new SignInReponseDto("sucess", token.getToken(), user.getRole());
        // return response
    }


    public SignInReponseDto signInMob(SignInDto signInDto) {
        // find user by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if (Objects.isNull(user)) {
            return new SignInReponseDto("fail", null, null);
        }
        // hash the password
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                return new SignInReponseDto("fail", null, null);
            }
        } catch (NoSuchAlgorithmException e) {
            return new SignInReponseDto("fail", null, null);
        }
        // compare the password in DB
        // if password match
        AuthenticationToken token = authenticationService.getToken(user);
        // retrive the token
        if (Objects.isNull(token)) {
            return new SignInReponseDto("fail", null, null);
        }
        return new SignInReponseDto("sucess", token.getToken(), user.getRole());
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


    public boolean backup()
            throws IOException, InterruptedException {

        Date backupDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String backupDateStr = format.format(backupDate);

        String fileName = "DbBackup"; // default file name

        String saveFileName = fileName + "_" + backupDateStr + ".sql";
        Path sqlFile = Paths.get(saveFileName);
        OutputStream stdOut = new BufferedOutputStream(Files.newOutputStream(sqlFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
        stdOut.close();

        String command = "D:\\projects\\CourseWork\\Project\\backup.bat && mysqldump -u root -proot atarkv2 >" +
                "D:\\projects\\CourseWork\\DBBackup\\" + saveFileName;
        Runtime.getRuntime().exec(command);
        return true;
    }


    public boolean restore()
            throws IOException {


        Date restoreDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String backupDateStr = format.format(restoreDate);

        String fileName = "DbBackup"; // default file name

        String restoreFileName = fileName + "_" + backupDateStr + ".sql";

        String command = "D:\\projects\\CourseWork\\Project\\backup.bat && mysql -u root -proot atarkv2 <" +
                "D:\\projects\\CourseWork\\DBBackup\\" + restoreFileName;
        Runtime.getRuntime().exec(command);
        return true;
    }


}
