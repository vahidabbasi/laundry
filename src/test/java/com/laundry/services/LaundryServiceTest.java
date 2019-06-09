package com.laundry.services;

import com.laundry.model.BookingInfo;
import com.laundry.model.BookingLaundryRequest;
import com.laundry.repository.dao.LaundryDao;
import com.laundry.validators.IntervalDateValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LaundryServiceTest {

    private static final int LAUNDRY_ID = 1;
    private static final LocalDateTime FUTURE_DATE = LocalDateTime.now().withHour(8).plusDays(1);
    private static final int HOUSE_HOLDER_ID = 3;

    @Mock
    private LaundryDao laundryDao;

    @Mock
    private IntervalDateValidator intervalDateValidator;

    @InjectMocks
    private LaundryService laundryService;

    @Test
    public void shouldBookLaundry() {
        final Integer expectedBookingReference = 0;

        when(intervalDateValidator.isTimeAvailable(anyInt(), any(), any())).thenReturn(true);
        when(laundryDao.bookLaundry(anyInt(), anyInt(), any(), any())).thenReturn(expectedBookingReference);

        final Integer actualBookingReference = laundryService.bookLaundry(LAUNDRY_ID, createBookingLaundryRequest());

        assertEquals(expectedBookingReference, actualBookingReference);
        verify(intervalDateValidator).isTimeAvailable(LAUNDRY_ID, FUTURE_DATE, FUTURE_DATE.plusSeconds(1));
        verify(laundryDao).bookLaundry(LAUNDRY_ID, HOUSE_HOLDER_ID, FUTURE_DATE, FUTURE_DATE.plusSeconds(1));
    }

    @Test
    public void shouldCancelLaundryBooking() {
        final Integer bookingReference = 0;

        doNothing().when(laundryDao).cancelLaundryBooking(anyInt());

        laundryService.cancelLaundryBooking(bookingReference);

        verify(laundryDao).cancelLaundryBooking(bookingReference);
    }

    @Test
    public void shouldGetAllBookings() {
        final List<BookingInfo> expectedList = Collections.emptyList();
        when(laundryDao.getAllBookings()).thenReturn(expectedList);

        final List<BookingInfo> actualBookings = laundryService.getAllBookings();

        assertEquals(expectedList, actualBookings);
        verify(laundryDao).getAllBookings();
    }

    private BookingLaundryRequest createBookingLaundryRequest() {
        return BookingLaundryRequest
                .builder()
                .fromDate(FUTURE_DATE)
                .toDate(FUTURE_DATE.plusSeconds(1))
                .houseHolderId(HOUSE_HOLDER_ID)
                .build();
    }
}
