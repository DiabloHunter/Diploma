package com.example.project.service.impl;

import com.example.project.model.Table;
import com.example.project.repository.ITableRepository;
import com.example.project.service.ITableService;
import com.example.project.util.TimeUtil;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableService implements ITableService {

    @Autowired
    private ITableRepository tableRepository;

    @Value("${day.start.time}")
    private String dayStartTimeString;

    @Value("${day.working.hours}")
    private Double dayWorkingHours;

    private LocalTime dayStartTime;
    private Integer timesToCreateTable;


    @PostConstruct
    private void parseGivenTime() {
        dayStartTime = TimeUtil.parseTimeFromString(dayStartTimeString);
        timesToCreateTable = (int) (dayWorkingHours / 0.25);
    }

    @Override
    public void createTable(Table table) throws Exception {
        if (tableRepository.existsBySearchId(table.getSearchId())) {
            throw new Exception("Table with the same searchId already exists!");
        }
        List<Table> tablesToSave = new ArrayList<>();
        LocalTime currentTime = dayStartTime;
        for (int i = 0; i < timesToCreateTable; i++) {
            Table tableToAdd = new Table(table.getSearchId(), table.getNumberOfSeats(), table.getReserved());
            tableToAdd.setReservedFrom(currentTime);
            currentTime = currentTime.plusMinutes(15);
            tableToAdd.setReservedTo(currentTime);
            tablesToSave.add(tableToAdd);
        }
        tableRepository.saveAll(tablesToSave);
    }

    @Override
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public void editCategory(String tableSearchId, Table updateTable) throws Exception {
        Table tableInDb = tableRepository.findBySearchId(tableSearchId).orElse(null);
        if (tableInDb == null) {
            throw new Exception("Table with given searchId doesn't exists!");
        }
        tableInDb.setSearchId(updateTable.getSearchId());
        tableInDb.setReserved(updateTable.getReserved());
        tableInDb.setNumberOfSeats(updateTable.getNumberOfSeats());
        tableInDb.setReservedFrom(TimeUtil.parseTime(updateTable.getReservedFrom()));
        tableInDb.setReservedTo(TimeUtil.parseTime(updateTable.getReservedTo()));
        tableRepository.save(tableInDb);
    }

    @Override
    public Table getTableBySearchId(String searchId) {
        return tableRepository.findBySearchId(searchId).orElse(null);
    }

    @Override
    public List<Table> getTablesBySearchIds(List<String> searchIds) {
        return tableRepository.findBySearchIdIn(searchIds);
    }

    @Override
    public List<Table> getTablesBySearchIdsAndTime(List<String> searchIds, LocalTime start, LocalTime end) {
        return tableRepository.getTablesBySearchIdAndTime(searchIds, start, end);
    }

    @Override
    public void deleteTable(String searchId) {
        tableRepository.deleteBySearchId(searchId);
    }

    @Override
    public void reserveTables(List<Table> tablesToReserve) {
        if (tablesToReserve == null) {
            return;
        }
        for (Table table : tablesToReserve) {
            table.setReserved(true);
        }
    }

    @Override
    public void setNotReserveTables(List<Table> tablesToReserve) {
        if (tablesToReserve == null) {
            return;
        }
        for (Table table : tablesToReserve) {
            table.setReserved(false);
        }
    }

    @Override
    public void saveAll(List<Table> tables) {
        tableRepository.saveAll(tables);
    }

}
