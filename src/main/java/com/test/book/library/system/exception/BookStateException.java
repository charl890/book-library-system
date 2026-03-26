package com.test.book.library.system.exception;

/**
 * Custom exception class for handling invalid book states in the library
 * system.
 * This exception can be thrown when an operation is attempted on a book that is
 * in an inappropriate state, such as trying to borrow a book that is already
 * borrowed or trying to return a book that is not currently borrowed.
 * It extends RuntimeException, allowing it to be used without mandatory
 * try-catch blocks.
 */
public class BookStateException extends RuntimeException {

    public BookStateException(String message) {
        super(message);
    }
}
