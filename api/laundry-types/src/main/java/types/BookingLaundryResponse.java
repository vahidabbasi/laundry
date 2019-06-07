package types;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Api(description = "The request class that provides data for booking a laundry")
public class BookingLaundryResponse {

    @ApiModelProperty(value = "Reference number for a booking")
    private Integer bookingReference;
}