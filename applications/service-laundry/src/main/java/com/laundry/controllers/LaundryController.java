package com.laundry.controllers;

import com.laundry.services.LaundryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import types.laundry.BookingLaundryInfoResponse;
import types.laundry.BookingLaundryRequest;
import types.laundry.BookingLaundryResponse;
import types.laundry.ErrorResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.HttpURLConnection;
import java.util.Objects;

@RestController
@RequestMapping("/v1")
@Api("A REST-controller to handle booking laundries")
@Validated
@Slf4j
public class LaundryController {

    private final LaundryService laundryService;

    @Inject
    public LaundryController(final LaundryService laundryService) {
        Objects.requireNonNull(laundryService, "laundryService was null when injected");
        this.laundryService = laundryService;
    }

    @PostMapping(value = "/laundries/{laundryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Book a laundry")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Household book a laundry"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "The request is missing or have badly " +
                    "formatted fields.", response = ErrorResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_CONFLICT, message = "This time is already booked",
                    response = ErrorResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Unexpected error, such as DB " +
                    "connection problem etc.", response = ErrorResponse.class)
    })
    public ResponseEntity<BookingLaundryResponse> bookLaundry(
            @PathVariable("laundryId")
            @Min(1) @Max(2) @ApiParam(value = "Laundry number that you want to book", required = true) final Integer
                    laundryId, @Valid @RequestBody final BookingLaundryRequest request) {

        final Integer bookingReference = laundryService.bookLaundry(laundryId, request);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(BookingLaundryResponse
                        .builder()
                        .bookingReference(bookingReference)
                        .build());
    }

    @DeleteMapping(value = "/laundries/laundry/{referenceNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cancel the booking time")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message = "Household cancel a laundry"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "The request is missing or have badly " +
                    "formatted fields.", response = ErrorResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "If the reference could not found",
                    response = ErrorResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Unexpected error, such as DB " +
                    "connection problem etc.", response = ErrorResponse.class)
    })
    public ResponseEntity<Void> cancelLaundryBooking(
            @PathVariable("referenceNumber")
            @ApiParam(value = "Laundry number that you want to book", required = true) final Integer referenceNumber) {
        laundryService.cancelLaundryBooking(referenceNumber);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/laundries/booking_times", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get the list of booking time")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Get list of booking"),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Unexpected error, such as DB " +
                    "connection problem etc.", response = ErrorResponse.class)
    })
    public ResponseEntity<BookingLaundryInfoResponse> getLaundriesBookings() {

        return ResponseEntity.ok(BookingLaundryInfoResponse.builder().
                bookingInfos(laundryService.getAllBookings()).build());
    }
}
