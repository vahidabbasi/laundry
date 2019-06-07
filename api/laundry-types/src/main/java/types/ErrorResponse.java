package types;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The response returned to the client in case of errors.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    @ApiModelProperty(value = "An enum value that describes the error.", required = true, example = "VALIDATION_FAILED")
    private ErrorStatus status;

    @ApiModelProperty(value = "An optional message that may detail the error.", example = "Please enter right value with correct format")
    private String message;
}