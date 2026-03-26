package com.test.book.library.system.dto.response;

import java.util.UUID;

import com.test.book.library.system.entity.BorrowRecord;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing a borrow response in the library
 * system.
 * This class is used to send information about a borrow operation back to
 * clients in API responses.
 * It includes fields for the borrow record's ID, borrower ID, book ID, a
 * message, and a flag
 * indicating whether the book is currently borrowed. It also includes a static
 * method to create
 * a BorrowResponse from a BorrowRecord entity.
 */
@Data
@Builder
public class BorrowResponse {

  private UUID recordId;
  private UUID borrowerId;
  private UUID bookId;
  private String message;
  private boolean hasBorrowed;

  public static BorrowResponse of(BorrowRecord borrowRecord) {
    return BorrowResponse.builder().recordId(borrowRecord.getRecordId()).borrowerId(borrowRecord.getBorrower().getId())
        .bookId(borrowRecord.getBook().getId()).hasBorrowed(borrowRecord.getReturnedAt() == null/* .isActive() */)
        .message(borrowRecord.getBorrowMsg()).build();
  }
}