package types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingInfo {
    private Integer bookingReference;
    private Integer houseHolderId;
    private Integer laundryId;
    private String fromDate;
    private String toDate;
}
