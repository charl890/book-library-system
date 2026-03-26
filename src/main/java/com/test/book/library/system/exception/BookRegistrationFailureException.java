package com.test.book.library.system.exception;

/**
 * Custom exception class for handling failures during book registration in the
 * library system.
 * This exception can be thrown when there is an issue with registering a new
 * book, such as validation errors or database constraints.
 * It extends RuntimeException, allowing it to be used without mandatory
 * try-catch blocks.
 */
public class BookRegistrationFailureException extends RuntimeException {

    public BookRegistrationFailureException(String message) {
        super(message);
    }

    public BookRegistrationFailureException(String message, Exception inner) {
        super(message, inner);
    }
}
