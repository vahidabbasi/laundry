package types.laundry;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Api("The request class that provides data for booking a laundry")
public class BookingLaundryRequest {


    @ApiModelProperty(value = "House holder identification")
    @Min(1) @Max(20)
    private Integer houseHolderId;

    @ApiModelProperty(value = "Start booking date and time: Should be with this format: yyyy-MM-dd HH:mm:ss")
    private String fromDate;

    @ApiModelProperty(value = "End booking date and time: Time should be with this format: yyyy-MM-dd HH:mm:ss")
    private String toDate;
}
