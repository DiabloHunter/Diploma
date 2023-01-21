package com.example.project.service;


import com.example.project.dto.table.CreateTableDto;
import com.example.project.dto.table.TableTimeDto;
import com.example.project.dto.table.UpdateTableDto;
import com.example.project.model.Table;
import javassist.NotFoundException;
import org.joda.time.LocalTime;

import java.util.List;
import java.util.Map;

public interface ITableService {

    List<Table> getAllTables();

    List<TableTimeDto> getAllTablesDto();

    TableTimeDto getTableDtoBySearchId(String searchId);

    Map<String, List<Table>> convertTablesListToMap(List<Table> list);

    List<TableTimeDto> convertTablesToTableDtos(Map<String, List<Table>> tables);

    TableTimeDto convertTablesToTableDto(List<Table> tables);

    List<Table> getTableBySearchId(String searchId);

    List<Table> getTablesBySearchIds(List<String> searchIds);

    List<Table> getTablesByIds(List<Long> ids);

    List<Table> getTablesBySearchIdsAndTime(List<String> searchIds, LocalTime start, LocalTime end);

    List<Table> getTablesByNumberOfSeatsAndTime(int numberOfSeats, LocalTime start, LocalTime end);

    List<Table> getTablesByNumberOfSeats(int numberOfSeats);

    List<Table> getTablesByTime(LocalTime start, LocalTime end);

    void reserveTables(List<Table> tablesToReserve);

    void setNotReserveTables(List<Table> tablesToReserve);

    void saveAll(List<Table> tables);

    void create(CreateTableDto createTableDto);

    void update(UpdateTableDto updateTableDto) throws NotFoundException;

    void deleteTable(String searchId) throws NotFoundException;

    boolean existBySearchId(String searchId);
}
