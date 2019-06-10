package com.laundry.services;

import com.laundry.exceptions.LaundryValidationException;
import com.laundry.model.BookingInfo;
import com.laundry.model.BookingLaundryRequest;
import com.laundry.repository.dao.LaundryDao;
import com.laundry.validators.DateValidator;
import com.laundry.validators.IntervalDateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class LaundryService {

    private final LaundryDao laundryDao;
    private final IntervalDateValidator intervalDateValidator;
    private static final String TIME_IS_ALREADY_BOOKED = "Time is already booked";

    @Inject
    public LaundryService(final LaundryDao laundryDao, final IntervalDateValidator intervalDateValidator) {
        Objects.requireNonNull(laundryDao, "laundryDao was null when injected");
        Objects.requireNonNull(intervalDateValidator, "internalDateValidator was null when injected");
        this.laundryDao = laundryDao;
        this.intervalDateValidator = intervalDateValidator;
    }

    public Integer bookLaundry(final Integer laundryId, final BookingLaundryRequest request) {
        log.info("Book a laundry number {} for householder {}", laundryId, request.getHouseHolderId());

        log.info("Validate fromDate and endDate");
        validateDates(request.getFromDate(), request.getToDate());

        log.info("Check time availability");
        if (!intervalDateValidator.isTimeAvailable(laundryId, request.getFromDate(), request.getToDate())) {
            throw new LaundryValidationException(TIME_IS_ALREADY_BOOKED, HttpStatus.FORBIDDEN);
        }

        return laundryDao.bookLaundry(laundryId, request.getHouseHolderId(), request.getFromDate(),
                request.getToDate());
    }

    public void cancelLaundryBooking(final Integer referenceNumber) {
        log.info("Cancel a booking for the laundry with reference number{}", referenceNumber);
        laundryDao.cancelLaundryBooking(referenceNumber);
    }

    public List<BookingInfo> getAllBookings() {
        log.info("Get list of all bookings");
        return laundryDao.getAllBookings();
    }

    private void validateDates(final LocalDateTime fromDate, final LocalDateTime toDate) {
        DateValidator.checkLaundryAccessibility(fromDate, toDate);
        DateValidator.validateFromDateIsBeforeToDate(fromDate, toDate);
        DateValidator.validateDatesWereNotPassed(fromDate, toDate);
    }

}
