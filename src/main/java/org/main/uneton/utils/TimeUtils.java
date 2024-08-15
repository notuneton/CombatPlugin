package org.main.uneton.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static String getCurrentFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}


