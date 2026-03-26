package com.test.book.library.system.exception;

/**
 * Custom exception class for handling failures during the book borrowing
 * process
 * in the library system.
 * This exception can be thrown when there is an issue with borrowing a book,
 * such as validation errors, book state issues, or database constraints.
 * It extends RuntimeException, allowing it to be used without mandatory
 * try-catch blocks.
 */
public class BorrowBookFailedException extends RuntimeException {

    public BorrowBookFailedException(String message) {
        super(message);
    }

    public BorrowBookFailedException(String message, Exception inner) {
        super(message, inner);
    }
}
