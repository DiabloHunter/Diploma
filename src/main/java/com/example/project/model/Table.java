package com.example.project.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@javax.persistence.Table(name = "tables")
public class Table {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private @NotNull String searchId;
    private @NotNull Integer minNumberOfSeats;
    private @NotNull Integer maxNumberOfSeats;

    public Table(String searchId, Integer minNumberOfSeats, Integer maxNumberOfSeats) {
        this.searchId = searchId;
        this.minNumberOfSeats = minNumberOfSeats;
        this.maxNumberOfSeats = maxNumberOfSeats;
    }

    public Table() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setMinNumberOfSeats(Integer numberOfSeats) {
        this.minNumberOfSeats = numberOfSeats;
    }

    public Integer getMaxNumberOfSeats() {
        return maxNumberOfSeats;
    }

    public void setMaxNumberOfSeats(Integer maxNumberOfSeats) {
        this.maxNumberOfSeats = maxNumberOfSeats;
    }
}
