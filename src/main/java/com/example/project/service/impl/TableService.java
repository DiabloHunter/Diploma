package com.example.project.service.impl;

import com.example.project.dto.table.CreateTableDto;
import com.example.project.dto.table.UpdateTableDto;
import com.example.project.model.Table;
import com.example.project.repository.ITableRepository;
import com.example.project.service.ITableService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TableService implements ITableService {

    @Autowired
    private ITableRepository tableRepository;

    @Override
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public void create(CreateTableDto createTableDto) {
        if (tableRepository.existsBySearchId(createTableDto.getSearchId())) {
            throw new IllegalArgumentException(String.format("Table with searchId %s already exists!",
                    createTableDto.getSearchId()));
        }

        Table table = new Table(createTableDto.getSearchId(), createTableDto.getMinNumberOfSeats(), createTableDto.getMaxNumberOfSeats());

        tableRepository.save(table);
    }

    @Override
    public void update(UpdateTableDto updateTableDto) throws NotFoundException {
        String previousSearchId = updateTableDto.getPreviousSearchId();
        String searchId = updateTableDto.getSearchId();
        Integer minNumberOfSeats = updateTableDto.getMinNumberOfSeats();
        Integer maxNumberOfSeats = updateTableDto.getMaxNumberOfSeats();


        if (previousSearchId == null || minNumberOfSeats == null && searchId == null && maxNumberOfSeats == null) {
            throw new IllegalArgumentException("Provide correct data!");
        }
        if (!tableRepository.existsBySearchId(previousSearchId)) {
            throw new NotFoundException(String.format("Table with searchId %s was not found!", previousSearchId));
        }
        if (searchId != null && tableRepository.existsBySearchId(searchId)) {
            throw new IllegalArgumentException(String.format("Table with given new searchId %s already exists!", searchId));
        }

        Table table = tableRepository.findBySearchId(previousSearchId);

        if (searchId != null) {
            table.setSearchId(updateTableDto.getSearchId());
        }
        if (minNumberOfSeats != null) {
            table.setMinNumberOfSeats(minNumberOfSeats);
        }
        if (maxNumberOfSeats != null) {
            table.setMaxNumberOfSeats(maxNumberOfSeats);
        }

        tableRepository.save(table);
    }

    @Override
    public void deleteTable(String searchId) throws NotFoundException {
        if (!tableRepository.existsBySearchId(searchId)) {
            throw new NotFoundException(String.format("Table with searchId %s was not found!", searchId));
        }
        tableRepository.deleteBySearchId(searchId);
    }

    @Override
    public Table getTableBySearchId(String searchId) {
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
    public List<Table> getTablesByNumberOfSeats(Integer numberOfSeats) {
        return tableRepository.getTablesByMinNumberOfSeats(numberOfSeats);
    }

    @Override
    public void saveAll(List<Table> tables) {
        tableRepository.saveAll(tables);
    }

    @Override
    public boolean existBySearchId(String searchId) {
        return tableRepository.existsBySearchId(searchId);
    }

}
