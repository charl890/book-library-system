package com.test.book.library.system.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.test.book.library.system.entity.Book;
import com.test.book.library.system.exception.BookStateException;

import org.junit.jupiter.api.Test;

/**
 * Test class for Book domain model, which contains unit tests for the
 * book validation logic in the library system.
 */
class BookTest {

        @Test
        void validateConsistencyWith_SameIsbnSameTitleAuthor_ShouldPass() {
                Book existingBook = Book.builder().isbn("12345").title("Effective Java").author("Joshua Bloch").build();

                Book newBook = Book.builder().isbn("12345").title("Effective Java").author("Joshua Bloch").build();

                assertDoesNotThrow(() -> existingBook.validateConsistencyWith(newBook));
        }

        @Test
        void validateConsistencyWith_SameIsbnDifferentTitle_ShouldThrowException() {
                Book existingBook = Book.builder().isbn("12345").title("Effective Java").author("Joshua Bloch").build();

                Book newBook = Book.builder().isbn("12345").title("Java Concurrency").author("Joshua Bloch").build();

                BookStateException exception = assertThrows(BookStateException.class,
                                () -> existingBook.validateConsistencyWith(newBook));

                assertTrue(exception.getMessage().contains("Invalid book registration"));
        }

        @Test
        void validateConsistencyWith_SameIsbnDifferentAuthor_ShouldThrowException() {
                Book existingBook = Book.builder().isbn("12345").title("Effective Java").author("Joshua Bloch").build();

                Book newBook = Book.builder().isbn("12345").title("Effective Java").author("Someone Else").build();

                BookStateException exception = assertThrows(BookStateException.class,
                                () -> existingBook.validateConsistencyWith(newBook));

                assertTrue(exception.getMessage().contains("Invalid book registration"));
        }
}
