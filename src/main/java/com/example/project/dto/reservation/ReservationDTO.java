package com.example.project.dto.reservation;

import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ReservationDTO {

    private @NotNull String searchId;
    private @NotNull LocalDateTime startTime;
    private @NotNull LocalDateTime endTime;
    private @NotNull Integer amountOfPeople;
    private @NotNull String userEmail;

    private String description;
    private @NotNull List<String> tableIds;

    public ReservationDTO(String searchId, LocalDateTime startTime, LocalDateTime endTime, Integer amountOfPeople,
                          String userEmail, String description, List<String> tableIds) {

        this.searchId = searchId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountOfPeople = amountOfPeople;
        this.userEmail = userEmail;
        this.description = description;
        this.tableIds = tableIds;
    }

    public ReservationDTO() {
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setAmountOfPeople(Integer amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTableIds() {
        return tableIds;
    }

    public void setTableIds(List<String> tableIds) {
        this.tableIds = tableIds;
    }
}
