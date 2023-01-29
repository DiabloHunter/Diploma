package com.example.project.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class TimeUtil {

    public static Date formatDate(Date time) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        String value = time.toString();
        value = value.substring(0, value.lastIndexOf('.')).substring(0, value.lastIndexOf(':')) + ":00";
        return formatter.parseLocalDateTime(value).toDate();
    }


    public static LocalDateTime formatLocalDateTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        String value = time.toString();
        value = value.substring(0, value.lastIndexOf('.')).substring(0, value.lastIndexOf(':')) + ":00";
        return formatter.parseLocalDateTime(value);
    }

    public static LocalDateTime formatLocalDateTimeFromLocalDate(LocalDate time) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.parseLocalDateTime(time.toString() + "T00:00:00");
    }

    public static LocalTime formatLocalTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        String value = time.toString();
        value = value.substring(0, value.lastIndexOf('.'));
        return formatter.parseLocalTime(value);
    }

    public static LocalTime formatLocalTimeToSeconds(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        String value = time.toString();
        value = value.substring(0, value.lastIndexOf('.')).substring(0, value.lastIndexOf(':')) + ":00";
        return formatter.parseLocalTime(value);
    }

    public static LocalTime formatStringToLocalTime(String time) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        time = time.substring(0, time.lastIndexOf('.'));
        return formatter.parseLocalTime(time);
    }

}
