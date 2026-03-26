package com.test.book.library.system.service;

import java.util.List;

import com.test.book.library.system.entity.Book;

/**
 * Service interface for managing books in the library system.
 * This interface defines the contract for book-related operations, such as
 * registering new books and retrieving all books from the library.
 * Implementations of this interface will contain the business logic for these
 * operations, interacting with the underlying data repositories as needed.
 */
public interface BookService {

    Book registerBook(Book book);

    List<Book> getAllBooks();

    List<Book> getAvailableBooks();

}
