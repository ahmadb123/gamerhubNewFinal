package com.utility;
import java.time.LocalDate;

public class CurrentDate {
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalDate getLastMonth() {
        return LocalDate.now().minusMonths(1);
    }
}