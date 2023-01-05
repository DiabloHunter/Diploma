package com.example.project.dto.table;

import java.util.ArrayList;
import java.util.List;

public class TableTimeDto {

    private String searchId;
    private int numberOfSeats;
    private List<String> availableTime;

    public TableTimeDto() {
    }

    public TableTimeDto(String searchId, int numberOfSeats) {
        this.searchId = searchId;
        this.numberOfSeats = numberOfSeats;
        this.availableTime = new ArrayList<>();
    }

    public TableTimeDto(String searchId, int numberOfSeats, List<String> availableTime) {
        this.searchId = searchId;
        this.numberOfSeats = numberOfSeats;
        this.availableTime = availableTime;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public List<String> getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(List<String> availableTime) {
        this.availableTime = availableTime;
    }
}
