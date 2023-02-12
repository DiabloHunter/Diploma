package com.example.project.dto.table;

public class CreateTableDto {

    private String searchId;
    private Integer minNumberOfSeats;
    private Integer maxNumberOfSeats;

    public CreateTableDto() {
    }

    public CreateTableDto(String searchId, Integer minNumberOfSeats, Integer maxNumberOfSeats) {
        this.searchId = searchId;
        this.minNumberOfSeats = minNumberOfSeats;
        this.maxNumberOfSeats = maxNumberOfSeats;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public Integer getMinNumberOfSeats() {
        return minNumberOfSeats;
    }

    public void setMinNumberOfSeats(Integer minNumberOfSeats) {
        this.minNumberOfSeats = minNumberOfSeats;
    }

    public Integer getMaxNumberOfSeats() {
        return maxNumberOfSeats;
    }

    public void setMaxNumberOfSeats(Integer maxNumberOfSeats) {
        this.maxNumberOfSeats = maxNumberOfSeats;
    }
}
