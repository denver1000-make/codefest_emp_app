package com.denprog.codefestapp.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

public class TimeUtil {

    public static long getCurrentTimeSeconds() {
        return Instant.now().getEpochSecond();
    }

    public static String getFormattedTime(long epochSecond) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");
        return dateTimeFormatter.format(localDateTime);
    }

}
