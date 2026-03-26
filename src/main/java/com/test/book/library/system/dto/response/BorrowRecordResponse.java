package com.test.book.library.system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.test.book.library.system.entity.BorrowRecord;

/**
 * Data Transfer Object (DTO) for representing a borrow record in the library
 * system.
 * This class is used to send borrow record information back to clients in API
 * responses.
 * It includes fields for the borrow record's ID, book details, borrower
 * details,
 * and the timestamp of when the book was borrowed. It also includes a static
 * method to create a BorrowRecordResponse from a BorrowRecord entity.
 */
@Data
@Builder
public class BorrowRecordResponse {
  private UUID borrowId;
  private UUID bookId;
  private String bookTitle;
  private String isbn;
  private UUID borrowerId;
  private String borrowerName;
  private LocalDateTime borrowedAt;

  public static BorrowRecordResponse of(BorrowRecord record) {
    return BorrowRecordResponse.builder().borrowId(record.getRecordId()).bookId(record.getBook().getId())
        .bookTitle(record.getBook().getTitle()).isbn(record.getBook().getIsbn())
        .borrowerId(record.getBorrower().getId()).borrowerName(record.getBorrower().getName())
        .borrowedAt(record.getBorrowedAt()).build();
  }
}
