package com.laundry.validators;

import com.laundry.exceptions.LaundryValidationException;

import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateValidator {
    private static final LocalTime ALLOWED_FORM = LocalTime.of(7, 0, 0);
    private static final LocalTime ALLOWED_TO = LocalTime.of(22, 0, 0);

    private DateValidator() {
        // no instances of this class
    }

    public static void checkLaundryAccessibility(final LocalDateTime fromDate, final LocalDateTime toDate) {
        if (fromDateIsBeforeAccessTime(fromDate) || toDateIsAfterAccessTime(toDate)) {
            throw new LaundryValidationException("Time is available between 07:00 to 22:00");
        }
    }

    public static void validateFromDateIsBeforeToDate(final LocalDateTime fromDate, final LocalDateTime toDate) {
        if (toDate.isBefore(fromDate) || toDate.isEqual(fromDate)) {
            throw new LaundryValidationException("FromDate has to be before toDate");
        }
    }

    public static void validateDatesWereNotPassed(final LocalDateTime fromDate, final LocalDateTime toDate) {
        if (LocalDateTime.now().isAfter(fromDate) || LocalDateTime.now().isAfter(toDate)) {
            throw new LaundryValidationException("You are not able to book passed time");
        }
    }

    private static boolean toDateIsAfterAccessTime(final LocalDateTime toDate) {
        return toDate.toLocalTime().equals(ALLOWED_TO) || toDate.toLocalTime().isAfter(ALLOWED_TO);
    }

    private static boolean fromDateIsBeforeAccessTime(final LocalDateTime fromDate) {
        return fromDate.toLocalTime().equals(ALLOWED_FORM) || fromDate.toLocalTime().isBefore(ALLOWED_FORM);
    }
}

