package com.test.book.library.system.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler for the library system.
 * This class uses Spring's @ControllerAdvice to handle exceptions thrown by
 * controllers and return appropriate HTTP responses with error information.
 * It includes handlers for specific custom exceptions related to book
 * registration, borrowing, and returning, as well as a generic error response
 * structure.
 */
@ControllerAdvice
@ResponseBody
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handle(BookRegistrationFailureException exception) {
        log.error(exception.getMessage());
        return error(exception.getMessage(), exception.getCause() != null ? exception.getCause().getMessage() : "");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handle(BorrowerRegistrationFailureException exception) {
        log.error(exception.getMessage());
        return error(exception.getMessage(), exception.getCause() != null ? exception.getCause().getMessage() : "");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handle(BorrowBookFailedException exception) {
        log.error(exception.getMessage());
        return error(exception.getMessage(), exception.getCause() != null ? exception.getCause().getMessage() : "");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handle(ReturnBookFailedException exception) {
        log.error(exception.getMessage());
        return error(exception.getMessage(), exception.getCause() != null ? exception.getCause().getMessage() : "");
    }

    private Error error(String message) {
        return Error.builder().message(message).build();
    }

    private Error error(String message, String detailedErrorMessage) {
        return Error.builder().message(message).detailedErrorMessage(detailedErrorMessage).build();
    }
}
