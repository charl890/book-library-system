package com.test.book.library.system.service;

import java.util.List;
import java.util.UUID;

import com.test.book.library.system.entity.BorrowRecord;

/**
 * Service interface for managing borrow operations in the library system.
 * This interface defines the contract for borrowing and returning books, as
 * well as retrieving current borrow records. Implementations of this interface
 * will contain the business logic for these operations, interacting with the
 * underlying data repositories as needed.
 */
public interface BorrowService {

    BorrowRecord borrowBook(UUID borrowerId, UUID bookId, String message);

    BorrowRecord returnBook(UUID borrowRecordId);

    List<BorrowRecord> getCurrentBorrowRecords();
}
