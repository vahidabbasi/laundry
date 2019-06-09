package com.laundry.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

public class ConflictException extends DataAccessException {

    private final String displayMessage;
    private final HttpStatus httpStatus;

    public ConflictException(final String message) {
        super(message);
        httpStatus = HttpStatus.CONFLICT;
        displayMessage = message;
    }

    public ConflictException(final String message, final Throwable cause) {
        super(message, cause);
        httpStatus = HttpStatus.CONFLICT;
        displayMessage = message;
    }

    /**
     * Returns a message that does not contain messages from nested exceptions
     *
     * @return A message
     */
    public String getDisplayMessage() {
        return displayMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
