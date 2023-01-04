package com.example.project.dto.table;

import java.util.ArrayList;
import java.util.List;

public class TableTimeDto {

    private String searchId;
    private List<String> availableTime;

    public TableTimeDto() {
    }

    public TableTimeDto(String searchId) {
        this.searchId = searchId;
        this.availableTime = new ArrayList<>();
    }

    public TableTimeDto(String searchId, List<String> availableTime) {
        this.searchId = searchId;
        this.availableTime = availableTime;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public List<String> getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(List<String> availableTime) {
        this.availableTime = availableTime;
    }
}
