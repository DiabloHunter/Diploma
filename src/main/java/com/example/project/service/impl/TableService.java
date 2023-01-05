package com.example.project.service.impl;

import com.example.project.dto.table.TableTimeDto;
import com.example.project.dto.table.UpdateTableDto;
import com.example.project.model.Table;
import com.example.project.repository.ITableRepository;
import com.example.project.service.ITableService;
import com.example.project.util.TimeUtil;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

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
        dayStartTime = TimeUtil.formatStringToLocalTime(dayStartTimeString);
        timesToCreateTable = (int) (dayWorkingHours / 0.25);
    }

    @Override
    public void create(Table table) throws Exception {
        if (tableRepository.existsBySearchId(table.getSearchId())) {
            throw new Exception("Table with the same searchId already exists!");
        }
        List<Table> tablesToSave = new ArrayList<>();
        LocalTime currentTime = dayStartTime;
        for (int i = 0; i < timesToCreateTable; i++) {
            Table tableToAdd = new Table(table.getSearchId(), table.getNumberOfSeats(), false);
            tableToAdd.setReservedFrom(currentTime);
            currentTime = currentTime.plusMinutes(15);
            tableToAdd.setReservedTo(currentTime);
            tablesToSave.add(tableToAdd);
        }
        tableRepository.saveAll(tablesToSave);
    }

    @Override
    public void update(UpdateTableDto updateTableDto) throws Exception {
        String previousSearchId = updateTableDto.getPreviousSearchId();
        String searchId = updateTableDto.getSearchId();
        Integer numberOfSeats = updateTableDto.getNumberOfSeats();


        if (previousSearchId == null || numberOfSeats == null && searchId == null) {
            throw new Exception("Provide correct data!");
        }
        if (!tableRepository.existsBySearchId(previousSearchId)) {
            throw new Exception("Table with given searchId does not exist!");
        }
        if (searchId != null && tableRepository.existsBySearchId(searchId)) {
            throw new Exception("Table with given new searchId already exists!");
        }
        List<Table> tables = tableRepository.findBySearchId(previousSearchId);
        if (searchId != null) {
            tables.forEach(table -> table.setSearchId(searchId));
        }
        if (numberOfSeats != null) {
            tables.forEach(table -> table.setNumberOfSeats(numberOfSeats));
        }
        tableRepository.saveAll(tables);
    }

    @Override
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public List<TableTimeDto> getAllTablesDto() {
        Map<String, List<Table>> tables = tableRepository.findAll().stream()
                .filter(table -> table.getReservedFrom().isAfter(new LocalTime()))
                .collect(Collectors.groupingBy(
                        Table::getSearchId,
                        Collectors.toList()
                ));

        return convertTablesToTableDtos(tables);
    }

    @Override
    public Map<String, List<Table>> convertTablesListToMap(List<Table> list) {
        return list.stream()
                .collect(Collectors.groupingBy(
                        Table::getSearchId,
                        Collectors.toList()
                ));
    }

    @Override
    public List<TableTimeDto> convertTablesToTableDtos(Map<String, List<Table>> tables) {
        List<TableTimeDto> tableTimeDtos = new ArrayList<>();
        for (Map.Entry<String, List<Table>> entry : tables.entrySet()) {
            tableTimeDtos.add(getTableDto(entry));
        }
        return tableTimeDtos;
    }

    private TableTimeDto getTableDto(Map.Entry<String, List<Table>> tableEntry) {
        TableTimeDto tableTimeDto = new TableTimeDto(tableEntry.getKey(), tableEntry.getValue().get(0).getNumberOfSeats());

        List<String> availableTime = new ArrayList<>();
        Table available = null;

        Iterator<Table> it = tableEntry.getValue().iterator();
        while (it.hasNext()) {
            Table current = it.next();
            if (available == null) {
                if (!current.isReserved()) {
                    available = current;
                }
            } else {
                if (current.isReserved()) {
                    availableTime.add(convertDateToReadableString(available.getReservedFrom(), current.getReservedFrom()));
                    available = null;
                    continue;
                }
            }

            if (available != null && !it.hasNext()) {
                if (available.equals(current)) {
                    availableTime.add(convertDateToReadableString(available.getReservedFrom(), available.getReservedTo()));
                } else {
                    availableTime.add(convertDateToReadableString(available.getReservedFrom(), current.getReservedFrom()));
                }
            }
        }

        tableTimeDto.setAvailableTime(availableTime);
        return tableTimeDto;
    }

    private String convertDateToReadableString(LocalTime available, LocalTime current) {
        return available.toString().substring(0, 5) + " -> " + current.toString().substring(0, 5);
    }


    @Override
    public List<Table> getTableBySearchId(String searchId) {
        return tableRepository.findBySearchId(searchId);
    }

    @Override
    public List<Table> getTablesBySearchIds(List<String> searchIds) {
        return tableRepository.findBySearchIdIn(searchIds);
    }

    @Override
    public List<Table> getTablesByIds(List<Long> ids) {
        return tableRepository.findAllById(ids);
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
