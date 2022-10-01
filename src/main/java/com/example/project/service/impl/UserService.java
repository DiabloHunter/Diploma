package com.example.project.service.impl;


import com.example.project.dto.ResponseDTO;
import com.example.project.dto.user.SignInDTO;
import com.example.project.dto.user.SignInResponseDTO;
import com.example.project.dto.user.SignupDTO;
import com.example.project.dto.user.UserDTO;
import com.example.project.exceptions.AuthenticationFailException;
import com.example.project.exceptions.CustomException;
import com.example.project.model.ERole;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.repository.IUserRepository;
import com.example.project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

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
public class UserService implements IUserService {

    @Autowired
    IUserRepository IUserRepository;

    @Override
    @Transactional
    public ResponseDTO signUp(SignupDTO signupDto) {
        if (Objects.nonNull(IUserRepository.findByEmail(signupDto.getEmail()))) {
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

        IUserRepository.save(user);

        ResponseDTO responseDto = new ResponseDTO("success", "user created succesfully");
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

    @Override
    public SignInResponseDTO signIn(SignInDTO signInDto) {
        String userEmail = signInDto.getEmail();
        User user = IUserRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new CustomException("User Not Found with email: " + userEmail));
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

        return new SignInResponseDTO("success", /*user.getRoles()*/null);
    }


    @Override
    public SignInResponseDTO signInMob(SignInDTO signInDto) {
        String userEmail = signInDto.getEmail();
        User user = IUserRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new CustomException("User Not Found with email: " + userEmail));
        if (Objects.isNull(user)) {
            return new SignInResponseDTO("fail", null);
        }
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                return new SignInResponseDTO("fail", null);
            }
        } catch (NoSuchAlgorithmException e) {
            return new SignInResponseDTO("fail", null);
        }

        return new SignInResponseDTO("success", /*user.getRoles()*/ null);
    }

    @Override
    public UserDTO getUserDto(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return IUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException("User Not Found with email: " + userEmail));
    }

    @Override
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
        IUserRepository.save(updatedUser);
    }

    @Override
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

    @Override
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
