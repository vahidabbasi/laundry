package types;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Api(description = "The request class that provides data for booking a laundry")
public class BookingLaundryInfoResponse {
    private List<BookingInfo> bookingInfos;
}
