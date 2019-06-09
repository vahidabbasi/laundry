package com.laundry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingInfo {
    private Integer bookingReference;
    private Integer houseHolderId;
    private Integer laundryId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
