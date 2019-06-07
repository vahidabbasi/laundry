package com.volvocar.laundry.controllers;

import com.volvocar.laundry.services.LaundryService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import types.BookingLaundryInfoResponse;
import types.BookingLaundryRequest;
import types.BookingLaundryResponse;
import types.ErrorResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.HttpURLConnection;
import java.util.Objects;

//TODO: unit test, IT tests
//TODO: comments
//TODO: logs
//TODO: validation

@RestController
@RequestMapping("/v1")
@Api(description = "A REST-controller to handle booking laundries")
@Validated
@Slf4j
public class LaundryController {

    private final LaundryService laundryService;

    @Inject
    public LaundryController(final LaundryService laundryService) {
        Objects.requireNonNull(laundryService, "laundryService was null when injected");
        this.laundryService = laundryService;
    }

    @RequestMapping(value = "/laundries/{laundryId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Book a laundry")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Household book a laundry"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "The request is missing or have badly formatted fields.", response = ErrorResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_CONFLICT, message = "This time is already booked", response = ErrorResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Unexpected error, such as DB-connection problem etc.", response = ErrorResponse.class)
    })
    public ResponseEntity<BookingLaundryResponse> bookLaundry(
            @PathVariable("laundryId")
            @Min(1) @Max(2) @ApiParam(value = "Laundry number that you want to book", required = true) final Integer laundryId,
            @Valid @RequestBody final BookingLaundryRequest request) {

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(BookingLaundryResponse.builder().
                        bookingReference(laundryService.bookLaundry(request, laundryId)).build());
    }

    @RequestMapping(value = "/laundries/laundry/{referenceNumber}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cancel the booking time")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "Household cancel a laundry"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "The request is missing or have badly formatted fields.", response = ErrorResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "If the reference could not found", response = ErrorResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Unexpected error, such as DB-connection problem etc.", response = ErrorResponse.class)
    })
    public ResponseEntity<Void> cancelLaundryBooking(
            @PathVariable("referenceNumber")
            @ApiParam(value = "Laundry number that you want to book", required = true) final Integer referenceNumber) {
            laundryService.cancelLaundryBooking(referenceNumber);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/laundries/booking_times", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get the list of booking time")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Get list of booking"),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Unexpected error, such as DB-connection problem etc.", response = ErrorResponse.class)
    })
    public ResponseEntity<BookingLaundryInfoResponse> getLaundriesBooking() {

        return ResponseEntity.ok(BookingLaundryInfoResponse.builder().
                bookingInfos(laundryService.getListOfBooking()).build());
    }
}
