package com.test.book.library.system.exception;

/**
 * Custom exception class for handling failures during borrower registration in
 * the library system.
 * This exception can be thrown when there is an issue with registering a new
 * borrower, such as validation errors or database constraints.
 * It extends RuntimeException, allowing it to be used without mandatory
 * try-catch blocks.
 */
public class BorrowerRegistrationFailureException extends RuntimeException {

    public BorrowerRegistrationFailureException(String message) {
        super(message);
    }

    public BorrowerRegistrationFailureException(String message, Exception inner) {
        super(message, inner);
    }
}
