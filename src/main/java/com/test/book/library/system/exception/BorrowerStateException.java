package com.test.book.library.system.exception;

/**
 * Custom exception class for handling invalid borrower states in the library
 * system.
 * This exception can be thrown when an operation is attempted on a borrower
 * that is
 * in an inappropriate state.
 * It extends RuntimeException, allowing it to be used without mandatory
 * try-catch blocks.
 */
public class BorrowerStateException extends RuntimeException {

    public BorrowerStateException(String message) {
        super(message);
    }
}
