package com.example.project.service.impl;

import com.example.project.dto.reservation.ReservationDTO;
import com.example.project.model.Reservation;
import com.example.project.model.Table;
import com.example.project.model.User;
import com.example.project.repository.IReservationRepository;
import com.example.project.service.IReservationService;
import com.example.project.service.ITableService;
import com.example.project.service.IUserService;
import com.example.project.util.TimeUtil;
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
    public void createReservation(ReservationDTO reservationDTO) {
        List<Table> selectedTables = tableService.getTablesBySearchIds(reservationDTO.getTableIds());
        tableService.reserveTables(selectedTables);

        User user = userService.getUserByEmail(reservationDTO.getUserEmail());

        Reservation reservation = new Reservation();
        reservation.setActive(true);
        reservation.setDescription(reservationDTO.getDescription());
        reservation.setAmountOfPeople(reservationDTO.getAmountOfPeople());
        reservation.setStartTime(TimeUtil.parseDateTime(reservationDTO.getStartTime()));
        reservation.setEndTime(TimeUtil.parseDateTime(reservationDTO.getEndTime()));
        reservation.setUser(user);
        reservation.setTables(selectedTables);

        reservationRepository.save(reservation);
        tableService.saveAll(selectedTables);
    }

    @Override
    public Map<String, List<Table>> getFreeTables(LocalDateTime start, LocalDateTime end) {
        List<Reservation> activeReservation = reservationRepository.getReservationByTime(
                TimeUtil.parseDateTime(start),
                TimeUtil.parseDateTime(end));
        Map<String, List<Table>> allTablesMap = convertToMap(tableService.getAllTables());
        List<Table> reservedTables = new ArrayList<>();
        activeReservation.forEach(x -> reservedTables.addAll(x.getTables()));
        Map<String, List<Table>> reservedTablesMap = convertToMap(reservedTables);
        for (Map.Entry<String, List<Table>> entry : reservedTablesMap.entrySet()) {
            allTablesMap.remove(entry.getKey());
        }
        return allTablesMap;
    }

    private Map<String, List<Table>> convertToMap(List<Table> tables) {
        Map<String, List<Table>> tableMap = new HashMap<>();
        for (Table table : tables) {
            String searchId = table.getSearchId();
            if (tableMap.containsKey(searchId)) {
                List<Table> tablesInMap = tableMap.get(searchId);
                tablesInMap.add(table);
                tableMap.put(searchId, tablesInMap);
            } else {
                tableMap.put(searchId, new ArrayList<>(Collections.singleton(table)));
            }
        }
        return tableMap;
    }

    @Scheduled(cron = "${cron.check.table.reservation.time}")
    private void checkReservationTime() {
        List<Reservation> outdatedReservations = reservationRepository.findByEndTimeIs(TimeUtil.parseDateTime(new LocalDateTime()));
        for (Reservation reservation : outdatedReservations) {
            reservation.setActive(false);
            List<String> tableIds = reservation.getTables().stream().map(Table::getSearchId).collect(Collectors.toList());
            List<Table> tablesToSetInactive = tableService.getTablesBySearchIdsAndTime(tableIds,
                    TimeUtil.parseTimeToSeconds(reservation.getStartTime().toLocalTime()),
                    TimeUtil.parseTimeToSeconds(reservation.getEndTime().toLocalTime()));
            tableService.setNotReserveTables(tablesToSetInactive);
        }
    }

    @Scheduled(cron = "${cron.check.table.reservation.time1}")
    private void checkTableReservationTime1() {

        Map<String, List<Table>> map = getFreeTables(TimeUtil.parseDateTime(new LocalDateTime().minusMonths(1).minusHours(5)),
                new LocalDateTime().plusDays(26).plusHours(1));

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
