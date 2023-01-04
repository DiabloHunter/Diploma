package com.example.project.dto.reservation;

import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateReservationDto {

    private @NotNull Long id;
    private @NotNull Boolean isActive;
    private @NotNull LocalDateTime startTime;
    private @NotNull LocalDateTime endTime;
    private @NotNull Integer amountOfPeople;
    private @NotNull String userEmail;
    private @NotNull List<Long> tableIds;
    private String description;

    public UpdateReservationDto(Boolean isActive, LocalDateTime startTime, LocalDateTime endTime,
                                Integer amountOfPeople, String userEmail, List<Long> tableIds, String description) {
        this.isActive = isActive;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountOfPeople = amountOfPeople;
        this.userEmail = userEmail;
        this.tableIds = tableIds;
        this.description = description;
    }

    public UpdateReservationDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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

    public List<Long> getTableIds() {
        return tableIds;
    }

    public void setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds;
    }

}
