package com.example.project.dto.table;

public class UpdateTableDto {

    private String previousSearchId;
    private String searchId;
    private Integer numberOfSeats;

    public UpdateTableDto() {
    }

    public UpdateTableDto(String previousSearchId, String searchId, Integer numberOfSeats) {
        this.previousSearchId = previousSearchId;
        this.searchId = searchId;
        this.numberOfSeats = numberOfSeats;
    }

    public String getPreviousSearchId() {
        return previousSearchId;
    }

    public void setPreviousSearchId(String previousSearchId) {
        this.previousSearchId = previousSearchId;
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
