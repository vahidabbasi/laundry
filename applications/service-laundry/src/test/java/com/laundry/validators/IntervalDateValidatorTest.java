package com.laundry.validators;

import com.laundry.db.dao.LaundryDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import types.laundry.BookingInfo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IntervalDateValidatorTest {

    private static final int LAUNDRY_ID = 1;
    private static final LocalDateTime FROM_DATE = LocalDateTime.of(2019, 6, 8, 10, 20, 0);
    private static final LocalDateTime TO_DATE = LocalDateTime.of(2019, 6, 8, 10, 50, 0);
    private static final int HOUSE_HOLDER_ID = 3;

    @Mock
    private LaundryDao laundryDao;

    @InjectMocks
    private IntervalDateValidator intervalDateValidator;

    @Test
    public void shouldCheckBookingTimeAvailability() {
        when(laundryDao.getFutureBookings()).thenReturn(getBookingInfo());

        assertFalse(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE, TO_DATE));
        assertFalse(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE.plusMinutes(10), TO_DATE.minusMinutes(10)));
        assertFalse(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE.plusMinutes(10), TO_DATE));
        assertFalse(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE.plusMinutes(10), TO_DATE.plusMinutes(5)));
        assertFalse(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE.plusMinutes(20), TO_DATE.plusMinutes(5)));
        assertFalse(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE, TO_DATE.minusMinutes(20)));
        assertFalse(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE.minusMinutes(15), TO_DATE.minusMinutes(20)));
        assertFalse(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE.minusMinutes(15), TO_DATE.minusMinutes(30)));
        assertTrue(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE.plusMinutes(35), TO_DATE.plusMinutes(9)));
        assertTrue(intervalDateValidator.isTimeAvailable(LAUNDRY_ID, FROM_DATE.minusMinutes(15), TO_DATE.minusMinutes(40)));
    }

    private List<BookingInfo> getBookingInfo() {
        return Collections
                .singletonList(BookingInfo.builder()
                        .fromDate(FROM_DATE)
                        .toDate(TO_DATE)
                        .laundryId(LAUNDRY_ID)
                        .houseHolderId(HOUSE_HOLDER_ID)
                        .build());
    }

}

