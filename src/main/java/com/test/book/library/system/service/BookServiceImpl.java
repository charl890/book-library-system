package com.test.book.library.system.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.test.book.library.system.entity.Book;
import com.test.book.library.system.exception.BookRegistrationFailureException;
import com.test.book.library.system.exception.BookStateException;
import com.test.book.library.system.repository.BookRepository;

import jakarta.transaction.Transactional;

/**
 * Service implementation for managing books in the library system.
 * This class implements the BookService interface and contains the business
 * logic for registering new books and retrieving all books from the library.
 * It interacts with the BookRepository to perform database operations related
 * to books.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Book registerBook(Book book) {
        if (book == null || book.getIsbn() == null || book.getIsbn().isBlank()) {
            log.error("Attempted to register a null or blank ISBN book");
            throw new BookRegistrationFailureException("ISBN must not be null or blank");
        }
        log.info("Attempting to register book with ISBN={}, title={}, author={}", book.getIsbn(), book.getTitle(),
                book.getAuthor());

        // Orchestration of the book registration
        Optional<Book> existingBookOpt = bookRepository.findTopByIsbnOrderByIdAsc(book.getIsbn());

        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();
            log.warn("Book with ISBN={} already exists. Validating consistency...", book.getIsbn());

            try {
                existingBook.validateConsistencyWith(book);
                log.info("Existing book with ISBN={} is consistent with new registration", book.getIsbn());
            } catch (BookStateException e) {
                log.error("Book state inconsistency detected for ISBN={}", book.getIsbn(), e);
                throw new BookRegistrationFailureException(
                        "Failed to register book: ISBN conflict: existing book has different title/author", e);
            }
        }

        // save new copy of the book (or the original if it was consistent)
        log.debug("Saving book to repository: {}", book);

        try {
            Book savedBook = bookRepository.save(book);
            log.info("Book successfully registered with ID={} and ISBN={}", savedBook.getId(), savedBook.getIsbn());
            return savedBook;
        } catch (Exception e) {
            log.error("Failed to save book to repository: {}", book, e);
            throw new BookRegistrationFailureException("Failed to save book to repository", e);
        }
    }

    @Override
    public List<Book> getAllBooks() {
        log.info("Fetching all books from repository");

        List<Book> books = bookRepository.findAll();
        log.debug("Fetched {} books", books.size());

        return books;
    }

    @Override
    public List<Book> getAvailableBooks() {
        log.info("Fetching all available books from repository");
        List<Book> books = bookRepository.findAllAvailableBooks();

        log.debug("Fetched {} books", books.size());
        return books;
    }
}
