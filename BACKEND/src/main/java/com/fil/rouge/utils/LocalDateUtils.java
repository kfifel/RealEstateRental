package com.fil.rouge.utils;


import java.time.LocalDate;

public class LocalDateUtils {

    private LocalDateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return Math.abs(endDate.toEpochDay() - startDate.toEpochDay());
    }

    public static boolean isDateAfter(LocalDate dateToCheck, LocalDate referenceDate) {
        return dateToCheck.isAfter(referenceDate);
    }

    public static boolean isDateBefore(LocalDate dateToCheck, LocalDate referenceDate) {
        return dateToCheck.isBefore(referenceDate);
    }

    public static boolean isDateInRange(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
        return !dateToCheck.isBefore(startDate) && !dateToCheck.isAfter(endDate);
    }

    public static LocalDate parseStringToDate(String dateString) {
        return LocalDate.parse(dateString);
    }
}
