package com.volvocar.laundry.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import types.BookingInfo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingInfoDto {
    private Integer booking_reference;
    private Integer householder_id;
    private Integer laundry_id;
    private String started_at;
    private String ended_at;

    public BookingInfo toBookingInfo() {

        return BookingInfo.builder()
                .bookingReference(booking_reference)
                .houseHolderId(householder_id)
                .laundryId(laundry_id)
                .fromDate(started_at)
                .toDate(ended_at)
                .build();
    }
}
