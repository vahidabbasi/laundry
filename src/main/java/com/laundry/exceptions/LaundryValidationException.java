package com.laundry.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when validation failed
 */
public class LaundryValidationException extends RuntimeException {
    private final String displayMessage;
    private final HttpStatus httpStatus;

    public LaundryValidationException(final String message) {
        super(message);
        displayMessage = message;
        httpStatus = HttpStatus.FORBIDDEN;
    }

    public LaundryValidationException(final String message,
                                      final HttpStatus httpStatus) {
        super(message);
        displayMessage = message;
        this.httpStatus = httpStatus;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
