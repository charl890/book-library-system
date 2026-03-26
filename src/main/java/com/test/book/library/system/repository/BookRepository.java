package com.test.book.library.system.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.test.book.library.system.entity.Book;

import jakarta.persistence.LockModeType;

/**
 * Repository interface for managing Book entities in the library system.
 * This interface extends JpaRepository, providing CRUD operations and custom
 * query methods for interacting with the "book" table in the database.
 * It includes methods to find books by ISBN, check for the existence of books
 * by various attributes, and retrieve books with pessimistic locking for
 * concurrent updates.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    // findByIsbn is inherited from JpaRepository, no need to declare it explicitly
    // Optional<Book> findByIsbn(String isbn);

    // Custom query to find the first book with a specific ISBN
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
    Optional<Book> findFirstByIsbn(String isbn);

    // save is inherited from JpaRepository, no need to declare it explicitly
    // Book save(Book book);

    // findById is inherited from JpaRepository, no need to declare it explicitly
    // Optional<Book> findById(UUID id);

    // findAll is inherited from JpaRepository, no need to declare it explicitly
    // List<Book> findAll();

    // Custom query to find all books with a specific ISBN
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
    List<Book> findAllByIsbn(@Param("isbn") String isbn);

    // List<Book> findAllByIsbn(String isbn);

    // Custom query to find all available books
    // @Query("SELECT b FROM Book b WHERE b.available = true")
    // List<Book> findByAvailableTrue();

    // Check if a book with the given ISBN already exists
    boolean existsByIsbn(String isbn);

    // Check if a book with the given title already exists
    boolean existsByTitle(String title);

    // Check if a book with the given author already exists
    boolean existsByAuthor(String author);

    // Get a book by ID with a pessimistic lock to prevent concurrent updates
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findByIdForUpdate(@Param("id") UUID id);

    // Custom query to find all available books
    @Query("""
            SELECT b FROM Book b
            WHERE NOT EXISTS (
                SELECT 1 FROM BorrowRecord br
                WHERE br.book = b
                AND br.returnedAt IS NULL
                            )
            """)
    List<Book> findAllAvailableBooks();
}
