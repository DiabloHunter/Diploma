package com.example.project.service;


import com.example.project.dto.ResponseDto;
import com.example.project.dto.user.SignInDto;
import com.example.project.dto.user.SignInReponseDto;
import com.example.project.dto.user.SignupDto;
import com.example.project.dto.user.UserDto;
import com.example.project.exceptions.AuthenticationFailException;
import com.example.project.exceptions.CustomException;
import com.example.project.model.ERole;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            throw new CustomException("user already present");
        }

        String encryptedpassword = signupDto.getPassword();

        try {
            encryptedpassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = new User(signupDto.getUsername(), signupDto.getEmail(),
                encryptedpassword, Collections.singleton(new Role(ERole.USER)));

        userRepository.save(user);

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
        String userEmail = signInDto.getEmail();
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + userEmail));
        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("user is not valid");
        }
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException("wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return new SignInReponseDto("success", /*user.getRoles()*/null);
    }


    public SignInReponseDto signInMob(SignInDto signInDto) {
        String userEmail = signInDto.getEmail();
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + userEmail));
        if (Objects.isNull(user)) {
            return new SignInReponseDto("fail", null);
        }
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                return new SignInReponseDto("fail", null);
            }
        } catch (NoSuchAlgorithmException e) {
            return new SignInReponseDto("fail", null);
        }

        return new SignInReponseDto("success", /*user.getRoles()*/ null);
    }

    public UserDto getUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public User getUserByEmail(String userEmail){
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + userEmail));
    }

    public void editUser(User updatedUser, User updateUser) {
        updatedUser.setEmail(updateUser.getEmail());
        updatedUser.setUsername(updateUser.getUsername());

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

        String fileName = "DbBackup";

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

        String fileName = "DbBackup";

        String restoreFileName = fileName + "_" + backupDateStr + ".sql";

        String command = "D:\\projects\\CourseWork\\Project\\backup.bat && mysql -u root -proot atarkv2 <" +
                "D:\\projects\\CourseWork\\DBBackup\\" + restoreFileName;
        Runtime.getRuntime().exec(command);
        return true;
    }


}
