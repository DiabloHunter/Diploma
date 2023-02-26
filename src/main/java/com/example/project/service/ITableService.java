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

    void create(CreateTableDto createTableDto);

    void update(UpdateTableDto updateTableDto) throws NotFoundException;

    void deleteTable(String searchId) throws NotFoundException;

    Table getTableBySearchId(String searchId);

    List<Table> getTablesBySearchIds(List<String> searchIds);

    List<Table> getTablesByIds(List<String> ids);

    List<Table> getTablesByNumberOfSeats(Integer numberOfSeats);

    void saveAll(List<Table> tables);

    boolean existBySearchId(String searchId);
}
