package com.laundry.utils;

import com.laundry.exceptions.LaundryValidationException;

import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateValidator {

    private static final LocalTime ALLOWED_FORM = LocalTime.of(7,0,0);
    private static final LocalTime ALLOWED_TO =  LocalTime.of(22,0,0);

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

