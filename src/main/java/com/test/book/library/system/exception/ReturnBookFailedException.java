package com.test.book.library.system.exception;

/**
 * Custom exception class for handling failures during the book return process
 * in the library system.
 * This exception can be thrown when there is an issue with returning a book,
 * such as validation errors, book state issues, or database constraints.
 * It extends RuntimeException, allowing it to be used without mandatory
 * try-catch blocks.
 */
public class ReturnBookFailedException extends RuntimeException {

    public ReturnBookFailedException(String message) {
        super(message);
    }

    public ReturnBookFailedException(String message, Exception inner) {
        super(message, inner);
    }
}
