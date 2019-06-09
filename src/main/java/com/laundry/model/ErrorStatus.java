package com.laundry.model;

/**
 * Contains the possible error statuses that can be returned
 */
public enum ErrorStatus {
    INTERNAL_SERVER_ERROR,
    VALIDATION_FAILED,
    FORMAT_NOT_SUPPORTED,
    DATA_ALREADY_EXISTS,
    TIME_IS_ALREADY_BOOKED,
    NOT_FOUND
}
