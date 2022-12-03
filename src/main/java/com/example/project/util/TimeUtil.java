package com.example.project.util;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtil {

    public static LocalDateTime parseDateTime(LocalDateTime dateTimeToParse) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        String value = dateTimeToParse.toString();
        value = value.substring(0, value.lastIndexOf('.')).substring(0, value.lastIndexOf(':')) + ":00";
        return formatter.parseLocalDateTime(value);
    }

    public static LocalTime parseTime(LocalTime timeToParse) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        String value = timeToParse.toString();
        value = value.substring(0, value.lastIndexOf('.'));
        return formatter.parseLocalTime(value);
    }

    public static LocalTime parseTimeToSeconds(LocalTime timeToParse) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        String value = timeToParse.toString();
        value = value.substring(0, value.lastIndexOf('.')).substring(0, value.lastIndexOf(':')) + ":00";
        return formatter.parseLocalTime(value);
    }

    public static LocalTime parseTimeFromString(String time) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        time = time.substring(0, time.lastIndexOf('.'));
        return formatter.parseLocalTime(time);
    }

}
