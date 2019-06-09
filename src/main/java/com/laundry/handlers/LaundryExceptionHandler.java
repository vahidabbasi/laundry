package com.laundry.handlers;


import com.laundry.exceptions.ConflictException;
import com.laundry.exceptions.LaundryValidationException;
import com.laundry.exceptions.NotFoundException;
import com.laundry.model.ErrorResponse;
import com.laundry.model.ErrorStatus;
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
        log.info("CompleteSigningException: {}", exception.getMessage());
        return createError(exception.getErrorStatus(), exception.getHttpStatus(), exception.getDisplayMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException: ", exception);
        final FieldError fieldError = exception.getBindingResult().getFieldError();
        return badRequest(ErrorStatus.FORMAT_NOT_SUPPORTED, fieldError.getField() + ": " +
                fieldError.getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentMismatchException(final MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException: ", exception);
        return badRequest(ErrorStatus.FORMAT_NOT_SUPPORTED, exception.getName() + ": " + exception.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity handleConflictException(final ConflictException exception) {
        log.info("ConflictException: {}", exception.getDisplayMessage());
        return conflictWithCurrentResource(ErrorStatus.DATA_ALREADY_EXISTS, exception.getDisplayMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(final NotFoundException exception) {
        log.info("NotFoundException: {}", exception.getMessage());
        return notFound(ErrorStatus.NOT_FOUND, exception.getDisplayMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(final ConstraintViolationException exception) {
        log.error("Validation exception encountered: ", exception);
        String message = "";
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        for (final ConstraintViolation<?> violation : violations) {
            message = violation.getPropertyPath() + " " + violation.getMessage();
        }
        return badRequest(ErrorStatus.FORMAT_NOT_SUPPORTED, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException: ", exception);
        return badRequest(ErrorStatus.FORMAT_NOT_SUPPORTED, null);
    }


    /**
     * Returns a 400 BAD_REQUEST response with the specified status.
     */
    private ResponseEntity<ErrorResponse> badRequest(final ErrorStatus status, final String message) {
        return createError(status, HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Returns a 409 CONFLICT response with the specified status.
     */
    private ResponseEntity<ErrorResponse> conflictWithCurrentResource(final ErrorStatus status, final String message) {
        return createError(status, HttpStatus.CONFLICT, message);
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
        return createError(ErrorStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, displayMessage);
    }

    /**
     * Returns a 404 NOT_FOUND response with the specified status.
     */
    private ResponseEntity<ErrorResponse> notFound(final ErrorStatus status, final String message) {
        return createError(status, HttpStatus.NOT_FOUND, message);
    }

    /**
     * Returns an HTTP error with the given statuses.
     */
    private static ResponseEntity<ErrorResponse> createError(final ErrorStatus status, final HttpStatus httpStatus,
                                                             final String message) {
        return status(httpStatus).body(ErrorResponse.builder()
                .status(status)
                .message(message)
                .build());
    }
}
