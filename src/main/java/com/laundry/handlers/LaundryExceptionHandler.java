package com.laundry.handlers;


import com.laundry.exceptions.ConflictException;
import com.laundry.exceptions.LaundryValidationException;
import com.laundry.exceptions.NotFoundException;
import com.laundry.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Slf4j
public class LaundryExceptionHandler {

    /**
     * To make sure that we do not let any exception through to the customer.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(final Exception exception) {
        log.error("RuntimeException: ", exception);
        return internalServerError();
    }

    @ExceptionHandler(LaundryValidationException.class)
    public ResponseEntity handleLaundryValidationException(final LaundryValidationException exception) {
        log.error("CompleteSigningException: {}", exception.getMessage());
        return createError(exception.getHttpStatus(), exception.getDisplayMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException: ", exception);
        final FieldError fieldError = exception.getBindingResult().getFieldError();
        return badRequest(fieldError.getField() + ": " + fieldError.getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException: ", exception);
        final Throwable mostSpecificCause = exception.getMostSpecificCause();
        final String errorMessage;
        if (mostSpecificCause != null) {
            final String exceptionName = mostSpecificCause.getClass().getName();
            final String message = mostSpecificCause.getMessage();
            errorMessage = exceptionName + message;
        } else {
            errorMessage = exception.getMessage();
        }
        return badRequest(errorMessage);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentMismatchException(final MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException: ", exception);
        return badRequest(exception.getName() + ": " + exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(final ConstraintViolationException exception) {
        log.error("Validation exception encountered: ", exception);
        String errorMessage = "";
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        for (final ConstraintViolation<?> violation : violations) {
            errorMessage = violation.getPropertyPath() + " " + violation.getMessage();
        }
        return badRequest(errorMessage);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity handleConflictException(final ConflictException exception) {
        log.error("ConflictException: {}", exception.getDisplayMessage());
        return conflictWithCurrentResource(exception.getDisplayMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(final NotFoundException exception) {
        log.error("NotFoundException: {}", exception.getMessage());
        return notFound(exception.getDisplayMessage());
    }

    /**
     * Returns a 400 BAD_REQUEST response with the specified status.
     */
    private ResponseEntity<ErrorResponse> badRequest(final String message) {
        return createError(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Returns a 409 CONFLICT response with the specified status.
     */
    private ResponseEntity<ErrorResponse> conflictWithCurrentResource(final String message) {
        return createError(HttpStatus.CONFLICT, message);
    }

    /**
     * Returns an INTERNAL_SERVER_ERROR to the client with the given error message.
     */
    private static ResponseEntity<ErrorResponse> internalServerError() {
        return internalServerError(null);
    }

    /**
     * Returns an INTERNAL_SERVER_ERROR to the client with the given error message.
     */
    private static ResponseEntity<ErrorResponse> internalServerError(final String displayMessage) {
        return createError(HttpStatus.INTERNAL_SERVER_ERROR, displayMessage);
    }

    /**
     * Returns a 404 NOT_FOUND response with the specified status.
     */
    private ResponseEntity<ErrorResponse> notFound(final String message) {
        return createError(HttpStatus.NOT_FOUND, message);
    }

    /**
     * Returns an HTTP error with the given statuses.
     */
    private static ResponseEntity<ErrorResponse> createError(final HttpStatus httpStatus, final String message) {
        return status(httpStatus).body(ErrorResponse.builder()
                .message(message)
                .build());
    }
}
