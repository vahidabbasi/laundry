package com.volvocar.laundry.utils;

import com.volvocar.laundry.exceptions.LaundryValidationException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateValidator {

    private static final LocalTime ALLOWED_FORM = LocalTime.of(07,00,00);
    private static final LocalTime ALLOWED_TO =  LocalTime.of(22,00,00);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private DateValidator() {
        // no instances of this class
    }

    public static void validateBookingTimeAllowed(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate.toLocalTime().isBefore(ALLOWED_FORM) || toDate.toLocalTime().isAfter(ALLOWED_TO)) {
            throw new LaundryValidationException("Time is available between 07:00 to 22:00");
        }
    }

    public static void validateBookingTime(LocalDateTime fromDate, LocalDateTime toDate) {
        if (toDate.compareTo(fromDate)<0) {
            throw new LaundryValidationException("ToDate has to bigger than FromDate ");
        }
    }
}

