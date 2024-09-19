package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static boolean isWithinDateRange(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return !ld.isBefore(startDate != null ? startDate : LocalDate.MIN) && !ld.isAfter(endDate != null ? endDate : LocalDate.MAX);
    }

    public static boolean isWithinTimeRange(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return !lt.isBefore(startTime != null ? startTime : LocalTime.MIN) && !lt.isAfter(endTime != null ? endTime : LocalTime.MAX);
    }
}