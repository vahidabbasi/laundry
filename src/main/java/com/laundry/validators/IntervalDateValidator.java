package com.laundry.validators;

import com.laundry.model.BookingInfo;
import com.laundry.repository.dao.LaundryDao;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class IntervalDateValidator {

    private final LaundryDao laundryDao;

    @Inject
    public IntervalDateValidator(final LaundryDao laundryDao) {
        Objects.requireNonNull(laundryDao, "laundryDao was null when injected");
        this.laundryDao = laundryDao;
    }

    public boolean isTimeAvailable(final Integer laundryId, final LocalDateTime fromDate,
                                   final LocalDateTime toDate) {
        final List<BookingInfo> bookingsInfo = laundryDao.getFutureBookings();
        boolean isTimeAvailable = true;
        for (final BookingInfo bookingInfo : bookingsInfo) {
            if (laundryId.equals(bookingInfo.getLaundryId())) {
                isTimeAvailable = checkTimeAvailability(fromDate, toDate, bookingInfo);
            }
        }
        return isTimeAvailable;
    }

    private boolean checkTimeAvailability(final LocalDateTime fromDate, final LocalDateTime toDate,
                                          final BookingInfo bookingInfo) {
        return toDate.isBefore(bookingInfo.getFromDate()) || fromDate.isAfter(bookingInfo.getToDate());
    }
}
