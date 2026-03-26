package com.test.book.library.system.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.test.book.library.system.entity.Book;
import com.test.book.library.system.entity.BorrowRecord;
import com.test.book.library.system.entity.Borrower;
import com.test.book.library.system.exception.BorrowStateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Test class for BorrowRecord domain model, which contains unit tests for the
 * borrow record validation and creation logic in the library system.
 */
class BorrowRecordTest {

  @Test
  void createBorrow_ValidBookAndBorrower_ShouldReturnBorrowRecord() {
    Book book = Book.builder().id(UUID.randomUUID()).isbn("12345").title("Effective Java").author("Joshua Bloch")
        .build();

    Borrower borrower = Borrower.builder().id(UUID.randomUUID()).name("Eranda").email("eranda@example.com").build();

    String message = "Borrowed for study";

    BorrowRecord record = BorrowRecord.createBorrow(book, borrower, message);

    assertNotNull(record);
    assertEquals(book, record.getBook());
    assertEquals(borrower, record.getBorrower());
    assertEquals(message, record.getBorrowMsg());
    assertTrue(record.getReturnedAt() == null /* .isActive() */);
    assertNotNull(record.getBorrowedAt());
  }

  @Test
  void createBorrow_NullBook_ShouldThrowException() {
    Borrower borrower = Borrower.builder().id(UUID.randomUUID()).name("Eranda").email("eranda@example.com").build();

    BorrowStateException exception = assertThrows(BorrowStateException.class,
        () -> BorrowRecord.createBorrow(null, borrower, "message"));

    assertEquals("Book cannot be null when creating a borrow record.", exception.getMessage());
  }

  @Test
  void createBorrow_NullBorrower_ShouldThrowException() {
    Book book = Book.builder().id(UUID.randomUUID()).isbn("12345").title("Effective Java").author("Joshua Bloch")
        .build();

    BorrowStateException exception = assertThrows(BorrowStateException.class,
        () -> BorrowRecord.createBorrow(book, null, "message"));

    assertEquals("Borrower cannot be null when creating a borrow record.", exception.getMessage());
  }

  @Test
  void createReturn_ValidActiveBorrowRecord_ShouldReturnUpdatedRecord() throws BorrowStateException {
    Book book = Book.builder().id(UUID.randomUUID()).title("Book").isbn("111").author("Author").build();
    Borrower borrower = Borrower.builder().id(UUID.randomUUID()).name("Eranda").email("e@mail.com").build();

    BorrowRecord record = BorrowRecord.builder().recordId(UUID.randomUUID()).book(book).borrower(borrower)
        .borrowMsg("Borrowed").borrowedAt(LocalDateTime.now()).returnedAt(null)/* active(true) */.build();

    BorrowRecord.createReturn(record);

    assertFalse(record.getReturnedAt() == null /* .isActive() */);
    assertNotNull(record.getReturnedAt());
    assertTrue(record.getBorrowMsg().contains("Returned"));
  }

  @Test
  void createReturn_AlreadyReturned_ShouldThrowException() {
    Book book = Book.builder().id(UUID.randomUUID()).title("Book").isbn("111").author("Author").build();
    Borrower borrower = Borrower.builder().id(UUID.randomUUID()).name("Eranda").email("e@mail.com").build();

    BorrowRecord record = BorrowRecord.builder().recordId(UUID.randomUUID()).book(book).borrower(borrower)
        .borrowMsg("Borrowed").borrowedAt(LocalDateTime.now()).returnedAt(LocalDateTime.now())
        /* active(false) */.build();

    BorrowStateException exception = assertThrows(BorrowStateException.class, () -> BorrowRecord.createReturn(record));

    assertEquals("Book already returned", exception.getMessage());
  }
}
