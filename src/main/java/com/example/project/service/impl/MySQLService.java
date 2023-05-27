package com.example.project.service.impl;

import com.example.project.service.IMySQLService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MySQLService implements IMySQLService {

    private static final Logger LOG = LogManager.getLogger(MySQLService.class);

    @Value("${db.bat.file.path}")
    private String batFilePath;
    @Value("${db.backup.path}")
    private String backupPath;
    @Value("${db.backup.query}")
    private String backupQuery;
    @Value("${db.restore.query}")
    private String restoreQuery;

    @Override
    public boolean backup()
            throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
        String backupDateStr = format.format(new Date());

        String fileName = "DbBackup";

        String saveFileName = fileName + "_" + backupDateStr + ".sql";

        String command = String.format("%s %s %s%s", batFilePath, backupQuery, backupPath, saveFileName);
        Process process = Runtime.getRuntime().exec(command);
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            LOG.error("Error during creating database back-up Reason: {}", e.getMessage());
            return false;
        }

        if (exitCode == 0) {
            LOG.info("Back-up with name {} was successfully created", saveFileName);
            return true;
        } else {
            LOG.error("Error during creating database back-up with name: {}", saveFileName);
            return false;
        }
    }

    @Override
    public boolean restore(String backupName)
            throws IOException {

        String command = String.format("%s %s %s%s", batFilePath, restoreQuery, backupPath, backupName);
        Process process = Runtime.getRuntime().exec(command);
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            LOG.error("Error during restoring database back-up Reason: {}", e.getMessage());
            return false;
        }

        if (exitCode == 0) {
            LOG.info("Restoring back-up with name {} was successfully completed", backupName);
            return true;
        } else {
            LOG.error("Error during restoring database back-up with name: {}", backupName);
            return false;
        }
    }

    @Override
    public List<String> getBackupNames() throws IOException {
        String fileExtension = ".sql";
        Path folder = Paths.get(backupPath);

        List<String> backupNames = new ArrayList<>();
        Files.walkFileTree(folder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(fileExtension)) {
                    backupNames.add(file.getFileName().toString());
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return backupNames;
    }

}
