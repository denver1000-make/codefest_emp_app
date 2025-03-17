package com.denprog.codefestapp.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
