package com.laundry.repository.dao;

import com.laundry.exceptions.ConflictException;
import com.laundry.exceptions.NotFoundException;
import com.laundry.model.BookingInfo;
import com.laundry.repository.dto.BookingInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class LaundryDao {

    private static final String INSERT_BOOKING_INFO_SQL = "INSERT INTO BOOKING " +
            "(HOUSEHOLDER_ID, LAUNDRY_ID, STARTED_AT, ENDED_AT) " +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_CANCEL_BOOKING_SQL = "UPDATE BOOKING " +
            "SET CANCEL_BOOKING_AT = :cancel_booking_at " +
            "WHERE BOOKING_REFERENCE = :booking_reference";

    private static final String GET_ALL_BOOKING_INFO_SQL = "SELECT BOOKING_REFERENCE, HOUSEHOLDER_ID, LAUNDRY_ID, " +
            "STARTED_AT, ENDED_AT " +
            "FROM BOOKING " +
            "WHERE CANCEL_BOOKING_AT IS NULL ";

    private static final String GET_FUTURE_BOOKING_INFO_SQL = "SELECT BOOKING_REFERENCE, HOUSEHOLDER_ID, LAUNDRY_ID, " +
            "STARTED_AT, ENDED_AT " +
            "FROM BOOKING " +
            "WHERE CANCEL_BOOKING_AT IS NULL AND STARTED_AT > NOW() AND ENDED_AT > NOW() ";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder;

    @Inject
    public LaundryDao(final NamedParameterJdbcTemplate jdbcTemplate, final KeyHolder keyHolder) {
        Objects.requireNonNull(jdbcTemplate, "jdbcTemplate was null when injected");
        Objects.requireNonNull(keyHolder, "keyholder was null when injected");
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = keyHolder;
    }

    public Integer bookLaundry(final Integer laundryId, final Integer houseHolderId, final LocalDateTime fromDate,
                               final LocalDateTime toDate) {
        try {
            jdbcTemplate.getJdbcOperations().update(
                    connection -> {
                        final PreparedStatement ps = connection.prepareStatement(INSERT_BOOKING_INFO_SQL,
                                new String[]{"booking_reference"});
                        ps.setInt(1, houseHolderId);
                        ps.setInt(2, laundryId);
                        ps.setTimestamp(3, java.sql.Timestamp.valueOf(fromDate));
                        ps.setTimestamp(4, java.sql.Timestamp.valueOf(toDate));
                        return ps;
                    },
                    keyHolder);
        } catch (final DuplicateKeyException exception) {
            throw new ConflictException("This time is already booked", exception);
        }

        return keyHolder.getKey().intValue();
    }

    public void cancelLaundryBooking(final Integer referenceNumber) {
        final Map<String, Object> params = new HashMap<>();
        params.put("booking_reference", referenceNumber);
        params.put("cancel_booking_at", java.sql.Date.valueOf(LocalDate.now()));
        if (jdbcTemplate.update(UPDATE_CANCEL_BOOKING_SQL, params) == 0) {
            throw new NotFoundException("Could not find booking.");
        }
    }

    public List<BookingInfo> getAllBookings() {
        return jdbcTemplate.query(GET_ALL_BOOKING_INFO_SQL, new BeanPropertyRowMapper<>(BookingInfoDto.class))
                .stream()
                .map(BookingInfoDto::toBookingInfo)
                .collect(Collectors.toList());
    }

    public List<BookingInfo> getFutureBookings() {
        return jdbcTemplate.query(GET_FUTURE_BOOKING_INFO_SQL, new BeanPropertyRowMapper<>(BookingInfoDto.class))
                .stream()
                .map(BookingInfoDto::toBookingInfo)
                .collect(Collectors.toList());
    }
}
