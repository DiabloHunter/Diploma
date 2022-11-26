package com.example.project.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtil {

    public static LocalDateTime parseTime(LocalDateTime dateTimeToParse){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        String value = dateTimeToParse.toString();
        value = value.substring(0, value.lastIndexOf('.'));
        return formatter.parseLocalDateTime(value);
    }

}
