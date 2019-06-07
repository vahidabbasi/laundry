package com.volvocar.laundry.services;

import com.volvocar.laundry.db.dao.LaundryDao;
import com.volvocar.laundry.exceptions.LaundryValidationException;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import types.BookingInfo;
import types.BookingLaundryRequest;
import types.ErrorStatus;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

import static com.volvocar.laundry.utils.DateValidator.validateBookingTime;
import static com.volvocar.laundry.utils.DateValidator.validateBookingTimeAllowed;


@Component
@Slf4j
public class LaundryService {

    private final LaundryDao laundryDao;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Inject
    public LaundryService(final LaundryDao laundryDao) {
        Objects.requireNonNull(laundryDao, "laundryDao was null when injected");
        this.laundryDao = laundryDao;
    }

    public Integer bookLaundry(BookingLaundryRequest request, Integer laundryId) {
        log.info("Book a laundry number {} for householder {}", laundryId, request.getHouseHolderId());

        Pair<LocalDateTime, LocalDateTime> convertedTime = validateBookingTimeFormat(request.getFromDate(), request.getToDate());

        validateBookingTime(convertedTime.getKey(), convertedTime.getValue());
        validateBookingTimeAllowed(convertedTime.getKey(), convertedTime.getValue());//Time should be between 7:00 to 22:00

        checkBookingTimeAvailability(laundryId, convertedTime.getKey(), convertedTime.getValue());

        return laundryDao.bookLaundry(request.getFromDate(), request.getToDate(), request.getHouseHolderId(),laundryId);
    }

    public void cancelLaundryBooking(Integer referenceNumber) {
        log.info("Cancel a booking for the laundry with reference number{}", referenceNumber);
        laundryDao.cancelLaundryBooking(referenceNumber);
    }

    public List<BookingInfo> getListOfBooking() {
        return laundryDao.getListOfBooking();
    }

    private Pair<LocalDateTime, LocalDateTime> validateBookingTimeFormat(String fromDate, String toDate) {
        LocalDateTime convertedFrom;
        LocalDateTime convertedTo;
        try {
            convertedFrom = LocalDateTime.parse(fromDate, FORMATTER);
            convertedTo = LocalDateTime.parse(toDate, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new LaundryValidationException("Time should be with this format: yyyy-MM-dd HH:mm:ss", HttpStatus.BAD_REQUEST, ErrorStatus.VALIDATION_FAILED);
        }
        return new Pair<>(convertedFrom, convertedTo);
    }

    private void checkBookingTimeAvailability(Integer laundryId, LocalDateTime fromDate, LocalDateTime toDate) {
        List<BookingInfo> bookingsInfo = laundryDao.getListOfBooking();

        for (BookingInfo bookingInfo: bookingsInfo) {
            checkLaundryAvailability(laundryId, fromDate, toDate, bookingInfo);
        }
    }

    private void checkLaundryAvailability(Integer laundryIdRequested, LocalDateTime fromDate, LocalDateTime toDate, BookingInfo bookingInfo) {
        if (laundryIdRequested.equals(bookingInfo.getLaundryId())) {
            checkTimeAvailability(fromDate, toDate, bookingInfo);
        }
    }

    private void checkTimeAvailability(LocalDateTime fromDate, LocalDateTime toDate, BookingInfo bookingInfo) {
        if(!fromDate.isAfter(LocalDateTime.parse(bookingInfo.getFromDate(), FORMATTER)) && !toDate.isBefore(LocalDateTime.parse(bookingInfo.getFromDate(), FORMATTER))) {
            throw new LaundryValidationException("Time is already booked", HttpStatus.FORBIDDEN, ErrorStatus.TIME_IS_ALREADY_BOOKED);

        } else if (!fromDate.isAfter(LocalDateTime.parse(bookingInfo.getToDate(), FORMATTER)) && !toDate.isAfter(LocalDateTime.parse(bookingInfo.getToDate(), FORMATTER))) {
            throw new LaundryValidationException("Time is already booked", HttpStatus.FORBIDDEN, ErrorStatus.TIME_IS_ALREADY_BOOKED);
        }
    }
}
