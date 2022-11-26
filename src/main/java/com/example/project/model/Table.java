package com.example.project.model;

import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@javax.persistence.Table(name = "tables")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private @NotNull String searchId;
    private @NotNull Integer numberOfSeats;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isReserved;

    public Table(Long id, String searchId, Integer numberOfSeats, Boolean isReserved) {
        this.id = id;
        this.searchId = searchId;
        this.numberOfSeats = numberOfSeats;
        this.isReserved = isReserved;
    }

    public Table(String searchId, Integer numberOfSeats, Boolean isReserved) {
        this.searchId = searchId;
        this.numberOfSeats = numberOfSeats;
        this.isReserved = isReserved;
    }

    public Table() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getReserved() {
        return isReserved;
    }

    public void setReserved(Boolean reserved) {
        isReserved = reserved;
    }
}
