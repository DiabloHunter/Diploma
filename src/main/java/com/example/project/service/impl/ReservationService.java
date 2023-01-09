package com.example.project.service.impl;

import com.example.project.dto.reservation.ReservationDTO;
import com.example.project.dto.reservation.UpdateReservationDto;
import com.example.project.dto.table.TableTimeDto;
import com.example.project.model.Reservation;
import com.example.project.model.Table;
import com.example.project.model.User;
import com.example.project.repository.IReservationRepository;
import com.example.project.service.IReservationService;
import com.example.project.service.ITableService;
import com.example.project.service.IUserService;
import com.example.project.util.TimeUtil;
import javassist.NotFoundException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    IReservationRepository reservationRepository;

    @Autowired
    ITableService tableService;

    @Autowired
    IUserService userService;

    @Override
    public List<TableTimeDto> getFreeTables(LocalDate date) {
        List<Reservation> activeReservation = reservationRepository.getReservationByTime(
                TimeUtil.formatLocalDateTimeFromLocalDate(date),
                TimeUtil.formatLocalDateTimeFromLocalDate(date.plusDays(1)));

        List<Table> allTables = tableService.getAllTables();

        allTables.forEach(x -> x.setReserved(false));

        List<Table> reservedTables = new ArrayList<>();
        activeReservation.forEach(x -> reservedTables.addAll(x.getTables()));

        List<Table> updatedTables = new ArrayList<>();
        for (Table table : allTables) {
            for (Table reservedTable : reservedTables) {
                if (table.getId() == reservedTable.getId()) {
                    table.setReserved(true);
                    break;
                }
            }
            updatedTables.add(table);

        }

        return tableService.convertTablesToTableDtos(tableService.convertTablesListToMap(updatedTables));
    }

    @Override
    public void createReservation(ReservationDTO reservationDTO) throws NotFoundException {

        List<Table> selectedTables = tableService.getTablesByIds(reservationDTO.getTableIds());
        if(selectedTables.isEmpty()){
            throw new NotFoundException("Tables with given ids were not found!");
        }

        tableService.reserveTables(selectedTables);

        User user = userService.getUserByEmail(reservationDTO.getUserEmail());
        if(user==null){
            throw new NotFoundException(String.format("User with given email %s was not found!", reservationDTO.getUserEmail()));
        }

        Reservation reservation = new Reservation();
        reservation.setActive(true);
        reservation.setDescription(reservationDTO.getDescription());
        reservation.setAmountOfPeople(reservationDTO.getAmountOfPeople());
        reservation.setStartTime(TimeUtil.formatLocalDateTime(reservationDTO.getStartTime()));
        reservation.setEndTime(TimeUtil.formatLocalDateTime(reservationDTO.getEndTime()));
        reservation.setUser(user);
        reservation.setTables(selectedTables);

        reservationRepository.save(reservation);
        tableService.saveAll(selectedTables);
    }

    @Override
    public void updateReservation(UpdateReservationDto updateReservationDto) throws NotFoundException {
        Reservation outdatedReservation = reservationRepository.getById(updateReservationDto.getId());
        if(outdatedReservation==null){
            throw new NotFoundException(String.format("Reservation with id %s was not found!", updateReservationDto.getId()));
        }

        User user = userService.getUserByEmail(updateReservationDto.getUserEmail());
        if(user==null){
            throw new NotFoundException(String.format("User with given email %s was not found!", updateReservationDto.getUserEmail()));
        }

        outdatedReservation.setStartTime(updateReservationDto.getStartTime());
        outdatedReservation.setEndTime(updateReservationDto.getEndTime());
        outdatedReservation.setAmountOfPeople(updateReservationDto.getAmountOfPeople());
        outdatedReservation.setUser(user);
        outdatedReservation.setDescription(updateReservationDto.getDescription());

        if (!updateReservationDto.isActive()) {
            List<Table> outdatedTables = outdatedReservation.getTables();
            outdatedTables.forEach(table -> table.setReserved(false));
            tableService.saveAll(outdatedTables);
        } else if (outdatedReservation.getStartTime().toLocalDate().equals(new LocalDate())) {
            List<Long> newReservedTableIds = updateReservationDto.getTableIds();
            List<Table> outdatedTables = outdatedReservation.getTables();
            tableService.setNotReserveTables(outdatedTables);
            List<Table> tablesForReservation = tableService.getTablesByIds(newReservedTableIds);

            outdatedTables.removeAll(tablesForReservation);
            tableService.reserveTables(tablesForReservation);
            outdatedTables.addAll(tablesForReservation);
            tableService.saveAll(outdatedTables);

            if (outdatedReservation.getStartTime().isBefore(new LocalDateTime().plusMinutes(30))) {
                user.setRating(user.getRating() - 0.1);
                userService.update(user);
            }
        }

        reservationRepository.save(outdatedReservation);
    }

    @Scheduled(cron = "${cron.check.table.reservation.time}")
    private void checkReservationTime() {
        List<Reservation> outdatedReservations = reservationRepository.findByEndTimeIs(TimeUtil.formatLocalDateTime(new LocalDateTime()));
        for (Reservation reservation : outdatedReservations) {
            reservation.setActive(false);
            List<String> tableIds = reservation.getTables().stream().map(Table::getSearchId).collect(Collectors.toList());
            List<Table> tablesToSetInactive = tableService.getTablesBySearchIdsAndTime(tableIds,
                    TimeUtil.formatLocalTimeToSeconds(reservation.getStartTime().toLocalTime()),
                    TimeUtil.formatLocalTimeToSeconds(reservation.getEndTime().toLocalTime()));
            tableService.setNotReserveTables(tablesToSetInactive);
        }
    }

    @Scheduled(cron = "${cron.check.table.reservation.time1}")
    private void checkTableReservationTime1() {

        List<TableTimeDto> map = getFreeTables(new LocalDate().plusDays(1));

//        Reservation reservationes = new Reservation();
//        LocalDateTime localDateTimeStart = TimeUtil.parseDateTime(new LocalDateTime());
//        LocalDateTime localDateTimeEnd = TimeUtil.parseDateTime(new LocalDateTime().plusHours(1));
//
//        reservationes.setActive(true);
//        reservationes.setStartTime(localDateTimeStart);
//        reservationes.setEndTime(localDateTimeEnd);
//        reservationes.setUser(userService.getUserByEmail("haha@gmail.com"));
//        reservationes.setAmountOfPeople(5);
//        reservationes.setDescription("Testing");
//        reservationes.setTables(tableService.getAllTables());
//
//        reservationRepository.save(reservationes);

//        List<Reservation> outdatedReservations = reservationRepository.findByEndTimeIs(TimeUtil.parseDateTime(new LocalDateTime()));
//        for(Reservation reservation:outdatedReservations){
//            reservation.setActive(false);
//            List<String> tableIds = reservation.getTables().stream().map(Table::getSearchId).collect(Collectors.toList());
//            List<Table> tablesToSetInactive = tableService.getTablesBySearchIdsAndTime(tableIds,
//                    TimeUtil.parseTimeToSeconds(reservation.getStartTime().toLocalTime()),
//                    TimeUtil.parseTimeToSeconds(reservation.getEndTime().toLocalTime()));
//            tableService.setNotReserveTables(tablesToSetInactive);
//        }
//        List<Reservation> outdatedReservations = reservationRepository.getReservationByTime(
//                TimeUtil.parseDateTime(new LocalDateTime().minusMonths(1)), new LocalDateTime().plusDays(26));

        //        List<Table> tables1 = tableService.getTablesBySearchIdsAndTime(List.of("searchId"),
//                TimeUtil.parseTimeToSeconds(new LocalTime().withHourOfDay(23).withMinuteOfHour(30)),
//                TimeUtil.parseTimeToSeconds(new LocalTime().withHourOfDay(15).withMinuteOfHour(30)));
    }

}
