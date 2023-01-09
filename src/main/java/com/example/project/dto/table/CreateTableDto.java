package com.example.project.dto.table;

public class CreateTableDto {

    private String searchId;
    private Integer numberOfSeats;

    public CreateTableDto() {
    }

    public CreateTableDto(String searchId, Integer numberOfSeats) {
        this.searchId = searchId;
        this.numberOfSeats = numberOfSeats;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

}
