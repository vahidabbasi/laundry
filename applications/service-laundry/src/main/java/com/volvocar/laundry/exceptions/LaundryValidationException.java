package com.volvocar.laundry.exceptions;

import org.springframework.http.HttpStatus;
import types.ErrorStatus;

/**
 * Exception thrown when validation failed
 */
public class LaundryValidationException extends RuntimeException {
    private final String displayMessage;
    private final HttpStatus httpStatus;
    private final ErrorStatus errorStatus;

    public LaundryValidationException(final String message) {
        super(message);
        displayMessage = message;
        httpStatus = HttpStatus.FORBIDDEN;
        errorStatus = ErrorStatus.VALIDATION_FAILED;
    }
    
    public LaundryValidationException(final String message,
                                      final HttpStatus httpStatus,
                                      final ErrorStatus errorStatus) {
        super(message);
        displayMessage = message;
        this.httpStatus = httpStatus;
        this.errorStatus = errorStatus;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
