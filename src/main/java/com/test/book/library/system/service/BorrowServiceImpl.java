package com.test.book.library.system.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.test.book.library.system.entity.Book;
import com.test.book.library.system.entity.BorrowRecord;
import com.test.book.library.system.entity.Borrower;
import com.test.book.library.system.exception.BorrowBookFailedException;
import com.test.book.library.system.exception.BorrowStateException;
import com.test.book.library.system.exception.ReturnBookFailedException;
import com.test.book.library.system.repository.BookRepository;
import com.test.book.library.system.repository.BorrowRecordRepository;
import com.test.book.library.system.repository.BorrowerRepository;

/**
 * Service implementation for managing borrow operations in the library system.
 * This class implements the BorrowService interface and contains the business
 * logic for borrowing and returning books, as well as retrieving current borrow
 * records. It interacts with the BookRepository, BorrowerRepository, and
 * BorrowRecordRepository to perform database operations related to these
 * entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowServiceImpl implements BorrowService {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Override
    @Transactional
    public BorrowRecord borrowBook(UUID borrowerId, UUID bookId, String message) {

        log.info("Borrow request received: borrowerId={}, bookId={}, message={}", borrowerId, bookId, message);

        // check if book already borrowed
        if (borrowRecordRepository.existsByBookIdAndActiveTrue(bookId)) {
            log.warn("Borrow failed: Book {} is already borrowed", bookId);
            throw new BorrowBookFailedException("Book is already borrowed");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("Borrow failed: Book {} not found", bookId);
            return new BorrowBookFailedException("Book not found");
        });

        Borrower borrower = borrowerRepository.findById(borrowerId).orElseThrow(() -> {
            log.error("Borrow failed: Borrower {} not found", borrowerId);
            return new BorrowBookFailedException("Borrower not found");
        });

        BorrowRecord borrowRecord = BorrowRecord.createBorrow(book, borrower, message);
        try {
            BorrowRecord savedRecord = borrowRecordRepository.save(borrowRecord);

            log.info("Borrow successful: borrowRecordId={}, borrowerId={}, bookId={}", savedRecord.getRecordId(),
                    borrowerId, bookId);

            return savedRecord;
        } catch (Exception e) {
            log.error("Borrow failed: invalid state for borrowerId={}, bookId={}", borrowerId, bookId, e);
            throw new BorrowBookFailedException("Failed to borrow book", e);
        }
    }

    @Transactional
    public BorrowRecord returnBook(UUID borrowRecordId) {
        log.info("Return request received: borrowRecordId={}", borrowRecordId);

        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId).orElseThrow(() -> {
            log.error("Return failed: Borrow record {} not found", borrowRecordId);
            return new ReturnBookFailedException("Borrow record not found");
        });

        try {
            BorrowRecord.createReturn(borrowRecord);
        } catch (BorrowStateException e) {
            log.error("Return failed: invalid state for borrowRecordId={}", borrowRecordId, e);
            throw new ReturnBookFailedException("Failed to return book", e);
        }

        try {
            BorrowRecord savedRecord = borrowRecordRepository.save(borrowRecord);
            log.info("Return successful: borrowRecordId={}", savedRecord.getRecordId());
            return savedRecord;
        } catch (Exception e) {
            log.error("Return failed: could not save updated borrow record for borrowRecordId={}", borrowRecordId, e);
            throw new ReturnBookFailedException("Failed to return book", e);
        }
    }

    public List<BorrowRecord> getCurrentBorrowRecords() {
        log.info("Fetching all currently active borrow records");

        List<BorrowRecord> activeRecords = borrowRecordRepository.findByActiveIsTrue();
        log.debug("Fetched {} active borrow records", activeRecords.size());

        return activeRecords;
    }
}
