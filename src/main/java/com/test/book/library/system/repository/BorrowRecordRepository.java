package com.test.book.library.system.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.test.book.library.system.entity.BorrowRecord;

/**
 * Repository interface for managing BorrowRecord entities in the library
 * system.
 * This interface extends JpaRepository, providing CRUD operations and custom
 * query methods for interacting with the "borrow_record" table in the database.
 * It includes methods to check if a book is currently borrowed, retrieve active
 * borrow records, and fetch specific borrow records based on book ID and
 * return status.
 */
@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, UUID> {
    // save is inherited from JpaRepository, no need to declare it explicitly
    // BorrowRecord save(BorrowRecord borrowRecord);

    // findById is inherited from JpaRepository, no need to declare it explicitly
    // Optional<BorrowRecord> findById(UUID id);

    // Check if a book is currently borrowed (active record exists)
    // Custom query to check if a book is currently borrowed (active record exists)
    @Query("SELECT COUNT(b) > 0 FROM BorrowRecord b WHERE b.book.id = :bookId AND b.returnedAt IS NULL")
    boolean existsByBookIdAndReturnDateIsNull(UUID bookId);

    // existsByBookIdAndActiveTrue is an alternative if you have an 'active' boolean
    // field instead of checking returnedAt == null
    // boolean existsByBookIdAndActiveTrue(UUID bookId);

    // If you have an 'active' boolean field, you can use this instead
    // List<BorrowRecord> findByActiveIsTrue();

    // Get all currently borrowed books, derive active status
    // from returnedAt == null
    // (no need for redundant active column)
    // Custom query to find all active borrow records (books currently borrowed)
    @Query("SELECT b FROM BorrowRecord b WHERE b.returnedAt IS NULL")
    List<BorrowRecord> findByReturnDateIsNull();

    // Optional: fetch active record for a specific book
    // Custom query to fetch the active borrow record for a specific book
    @Query("SELECT b FROM BorrowRecord b WHERE b.book.id = :bookId AND b.returnedAt IS NULL")
    Optional<BorrowRecord> findByBookIdAndReturnDateIsNull(UUID bookId);

    // Custom query to check if a book is currently borrowed (active record exists)
    @Query("SELECT COUNT(b) > 0 FROM BorrowRecord b WHERE b.book.id = :bookId AND b.returnedAt IS NULL")
    boolean existsByBookIdAndActiveTrue(@Param("bookId") UUID bookId);

    // Custom query to find books that are currently borrowed (active record exists)
    @Query("SELECT b FROM BorrowRecord b WHERE b.returnedAt IS NULL")
    List<BorrowRecord> findByActiveIsTrue();
}
