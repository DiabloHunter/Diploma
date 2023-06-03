package com.example.project.service;

import java.io.IOException;
import java.util.List;

public interface IMySQLService {

    boolean backup()
            throws IOException;

    boolean restore(String backupName)
            throws IOException;

    List<String> getBackupNames() throws IOException;
}
