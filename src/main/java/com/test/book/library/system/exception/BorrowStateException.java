package com.test.book.library.system.exception;

/**
 * Custom exception class for handling invalid borrow states in the library
 * system.
 * This exception can be thrown when an operation is attempted on a borrow
 * record that is in an inappropriate state, such as trying to return a book
 * that is not currently borrowed or trying to borrow a book that is already
 * borrowed.
 * It extends RuntimeException, allowing it to be used without mandatory
 * try-catch blocks.
 */
public class BorrowStateException extends RuntimeException {

    public BorrowStateException(String message) {
        super(message);
    }
}
