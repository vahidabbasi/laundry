package com.laundry.db.exceptions;

import org.springframework.dao.DataAccessException;

/**
 * Thrown when a resource is not found in the database
 */
public class NotFoundException extends DataAccessException {
    private final String displayMessage;

    public NotFoundException(final String message) {
        super(message);
        displayMessage = message;
    }

    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
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
}
