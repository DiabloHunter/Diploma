package com.example.project.service.impl;

import com.example.project.dto.user.UserDTO;
import com.example.project.exceptions.CustomException;
import com.example.project.model.User;
import com.example.project.repository.IUserRepository;
import com.example.project.service.IUserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }

    @Override
    public UserDTO getUserDto(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRating(user.getRating());
        return userDto;
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException("User Not Found with email: " + userEmail));
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
    public void update(User updatedUser, User updateUser) {
        updatedUser.setEmail(updateUser.getEmail());

        try {
            String encryptedPassword = hashPassword(updateUser.getPassword());
            updatedUser.setPassword(encryptedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        userRepository.save(updatedUser);
    }

    @Override
    public void update(User updateUser) {
        userRepository.save(updateUser);
    }

    @Override
    public boolean backup()
            throws IOException {
        DateTime backupDate = new DateTime();
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
        DateTime restoreDate = new DateTime();
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
