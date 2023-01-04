package com.example.project.service;


import com.example.project.dto.table.TableTimeDto;
import com.example.project.dto.table.UpdateTableDto;
import com.example.project.model.Table;
import org.joda.time.LocalTime;

import java.util.List;
import java.util.Map;

public interface ITableService {

    List<Table> getAllTables();

    List<TableTimeDto> getAllTablesDto();

    Map<String, List<Table>> convertTablesListToMap(List<Table> list);

    List<TableTimeDto> convertTablesToTableDtos(Map<String, List<Table>> tables);

    List<Table> getTableBySearchId(String searchId);

    List<Table> getTablesBySearchIds(List<String> searchIds);

    List<Table> getTablesByIds(List<Long> ids);

    List<Table> getTablesBySearchIdsAndTime(List<String> searchIds, LocalTime start, LocalTime end);

    void reserveTables(List<Table> tablesToReserve);

    void setNotReserveTables(List<Table> tablesToReserve);

    void saveAll(List<Table> tables);

    void create(Table table) throws Exception;

    void update(UpdateTableDto updateTableDto) throws Exception;

    void deleteTable(String searchId);
}
