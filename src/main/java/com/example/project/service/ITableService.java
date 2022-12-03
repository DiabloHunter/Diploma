package com.example.project.service;


import com.example.project.model.Table;
import org.joda.time.LocalTime;

import java.util.List;

public interface ITableService {

    void createTable(Table table) throws Exception;

    List<Table> getAllTables();

    void editCategory(String tableSearchId, Table updateTable) throws Exception;

    Table getTableBySearchId(String searchId);

    List<Table> getTablesBySearchIds(List<String> searchIds);

    List<Table> getTablesBySearchIdsAndTime(List<String> searchIds, LocalTime start, LocalTime end);

    void deleteTable(String searchId);

    void reserveTables(List<Table> tablesToReserve);

    void setNotReserveTables(List<Table> tablesToReserve);

    void saveAll(List<Table> tables);
}
