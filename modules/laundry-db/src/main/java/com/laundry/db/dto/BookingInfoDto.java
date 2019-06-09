package com.laundry.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import types.laundry.BookingInfo;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingInfoDto {
    private Integer bookingReference;
    private Integer householderId;
    private Integer laundryId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public BookingInfo toBookingInfo() {

        return BookingInfo.builder()
                .bookingReference(bookingReference)
                .houseHolderId(householderId)
                .laundryId(laundryId)
                .fromDate(startedAt)
                .toDate(endedAt)
                .build();
    }
}
