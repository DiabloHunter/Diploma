package com.example.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@javax.persistence.Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private @NotNull LocalDateTime startTime;
    private @NotNull LocalDateTime endTime;
    private @NotNull Integer amountOfPeople;
    private String description;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isCanceled;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "reservation_tables",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id"))
    private List<Table> tables;

    public Reservation(LocalDateTime startTime, LocalDateTime endTime, Integer amountOfPeople, String description, User user, List<Table> tables) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountOfPeople = amountOfPeople;
        this.description = description;
        this.user = user;
        this.tables = tables;
    }

    public Reservation(LocalDateTime startTime, LocalDateTime endTime, Integer amountOfPeople, User user, List<Table> tables) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountOfPeople = amountOfPeople;
        this.user = user;
        this.tables = tables;
    }

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public void setCanceled(Boolean canceled) {
        isCanceled = canceled;
    }
}
