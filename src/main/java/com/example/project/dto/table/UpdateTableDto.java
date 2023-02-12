package com.example.project.dto.table;

public class UpdateTableDto {

    private String previousSearchId;
    private String searchId;
    private Integer minNumberOfSeats;
    private Integer maxNumberOfSeats;

    public UpdateTableDto() {
    }

    public UpdateTableDto(String previousSearchId, String searchId, Integer minNumberOfSeats, Integer maxNumberOfSeats) {
        this.previousSearchId = previousSearchId;
        this.searchId = searchId;
        this.minNumberOfSeats = minNumberOfSeats;
        this.maxNumberOfSeats = maxNumberOfSeats;
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
