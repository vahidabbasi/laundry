package com.laundry.validators;

import com.laundry.exceptions.LaundryValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static com.laundry.validators.DateValidator.checkLaundryAccessibility;
import static com.laundry.validators.DateValidator.validateDatesWereNotPassed;
import static com.laundry.validators.DateValidator.validateFromDateIsBeforeToDate;

@RunWith(MockitoJUnitRunner.class)
public class DateValidatorTest {

    private static final LocalDateTime INVALID_FROM_DATE =
            LocalDateTime.of(2019, 6, 8, 7, 0, 0);
    private static final LocalDateTime INVALID_TO_DATE =
            LocalDateTime.of(2019, 6, 8, 22, 0, 0);
    private static final LocalDateTime VALID_DATE =
            LocalDateTime.of(2019, 6, 8, 9, 0, 0);
    private static final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusSeconds(1);

    @Test
    public void shouldCheckLaundryAccessibilityWithValidDate() {
        checkLaundryAccessibility(VALID_DATE, VALID_DATE);
    }

    @Test(expected = LaundryValidationException.class)
    public void shouldCheckLaundryAccessibilityThrowExceptionWithInvalidFromDate() {
        checkLaundryAccessibility(INVALID_FROM_DATE, VALID_DATE);
    }

    @Test(expected = LaundryValidationException.class)
    public void shouldCheckLaundryAccessibilityThrowExceptionWithInvalidToDate() {
        checkLaundryAccessibility(VALID_DATE, INVALID_TO_DATE);
    }

    @Test
    public void shouldValidateFromDateIsBeforeToDateWithValidDate() {
        validateFromDateIsBeforeToDate(VALID_DATE, VALID_DATE.plusDays(1));
    }

    @Test(expected = LaundryValidationException.class)
    public void shouldValidateFromDateIsBeforeToDateThrowExceptionWithInvalidToDate() {
        validateFromDateIsBeforeToDate(VALID_DATE.plusDays(1), VALID_DATE);
    }

    @Test(expected = LaundryValidationException.class)
    public void shouldValidateFromDateIsBeforeToDateThrowException() {
        validateFromDateIsBeforeToDate(VALID_DATE.plusDays(1), VALID_DATE.plusDays(1));
    }

    @Test
    public void shouldValidateDatesWereNotPassedWithValidDate() {
        validateDatesWereNotPassed(FUTURE_DATE, FUTURE_DATE);
    }

    @Test(expected = LaundryValidationException.class)
    public void shouldValidateDatesWereNotPassedThrowExceptionWithFromDatePassed() {
        validateDatesWereNotPassed(VALID_DATE, FUTURE_DATE);
    }

    @Test(expected = LaundryValidationException.class)
    public void shouldValidateDatesWereNotPassedThrowExceptionWithToDatePassed() {
        validateDatesWereNotPassed(FUTURE_DATE, VALID_DATE);
    }
}
