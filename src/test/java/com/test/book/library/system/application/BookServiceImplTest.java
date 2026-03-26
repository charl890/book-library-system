package com.test.book.library.system.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.test.book.library.system.entity.Book;
import com.test.book.library.system.exception.BookRegistrationFailureException;
import com.test.book.library.system.repository.BookRepository;
import com.test.book.library.system.service.BookServiceImpl;

/**
 * Test class for BookServiceImpl, which contains unit tests for the book
 * management functionality in the library system. This class uses Mockito to
 * mock dependencies and JUnit 5 for assertions and test structure. The tests
 * cover scenarios such as registering a new book, handling duplicate ISBNs with
 * consistent and inconsistent data, and retrieving all books from the
 * repository.
 */
class BookServiceImplTest {

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookServiceImpl bookService;

  private Book newBook;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    newBook = Book.builder()
        .id(UUID.randomUUID())
        .isbn("123-ABC")
        .title("Domain-Driven Design")
        .author("Eric Evans")
        .build();
  }

  @Test
  void registerBook_WhenIsbnIsNull_ShouldThrowException() {

    newBook.setIsbn(null);

    assertThrows(Exception.class, () -> bookService.registerBook(newBook));

    verify(bookRepository, never()).save(any());
  }

  @Test
  void registerBook_WhenIsbnNotExists_ShouldSaveBook() {

    when(bookRepository.findTopByIsbnOrderByIdAsc(newBook.getIsbn())).thenReturn(Optional.empty());
    when(bookRepository.save(newBook)).thenReturn(newBook);

    Book savedBook = bookService.registerBook(newBook);

    assertNotNull(savedBook);
    assertEquals("123-ABC", savedBook.getIsbn());
    verify(bookRepository).save(newBook);
  }

  @Test
  void registerBook_WhenSaveFails_ShouldThrowException() {

    when(bookRepository.findTopByIsbnOrderByIdAsc(newBook.getIsbn()))
        .thenReturn(Optional.empty());

    when(bookRepository.save(newBook))
        .thenThrow(new RuntimeException("Save failed"));

    assertThrows(RuntimeException.class,
        () -> bookService.registerBook(newBook));
  }

  @Test
  void registerBook_WhenRepositoryFails_ShouldPropagateException() {

    when(bookRepository.findTopByIsbnOrderByIdAsc(newBook.getIsbn()))
        .thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class,
        () -> bookService.registerBook(newBook));
  }

  @Test
  void getAvailableBooks_ShouldReturnAvailableBooks() {

    Book book1 = Book.builder().id(UUID.randomUUID()).build();
    Book book2 = Book.builder().id(UUID.randomUUID()).build();

    when(bookRepository.findAllAvailableBooks())
        .thenReturn(List.of(book1, book2));

    List<Book> result = bookService.getAvailableBooks();

    assertEquals(2, result.size());
    verify(bookRepository).findAllAvailableBooks();
  }

  @Test
  void registerBook_WhenIsbnExistsAndConsistent_ShouldSaveNewCopy() {

    Book consistentExistingBook = Book.builder()
        .id(UUID.randomUUID())
        .isbn(newBook.getIsbn())
        .title(newBook.getTitle()) // same title
        .author(newBook.getAuthor()) // same author
        .build();

    when(bookRepository.findTopByIsbnOrderByIdAsc(newBook.getIsbn())).thenReturn(
        Optional.of(consistentExistingBook));
    when(bookRepository.save(newBook)).thenReturn(newBook);

    Book savedBook = bookService.registerBook(newBook);

    assertNotNull(savedBook);
    verify(bookRepository).save(newBook);
  }

  @Test
  void registerBook_WhenIsbnExistsAndInconsistent_ShouldThrowBookRegistrationFailureException() {

    Book inconsistentBook = Book.builder()
        .id(UUID.randomUUID())
        .isbn(newBook.getIsbn())
        .title("Different Title") // different to force inconsistency
        .author("Different Author")
        .build();

    when(bookRepository.findTopByIsbnOrderByIdAsc(newBook.getIsbn())).thenReturn(
        Optional.of(inconsistentBook));

    assertThrows(BookRegistrationFailureException.class, () -> bookService.registerBook(newBook));
    verify(bookRepository, never()).save(any());
  }

  @Test
  void getAllBooks_ShouldReturnListOfBooks() {

    Book book1 = Book.builder()
        .id(UUID.randomUUID())
        .isbn("111-AAA")
        .title("Book One")
        .author("Author A")
        .build();

    Book book2 = Book.builder()
        .id(UUID.randomUUID())
        .isbn("222-BBB")
        .title("Book Two")
        .author("Author B")
        .build();

    when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

    List<Book> books = bookService.getAllBooks();

    assertNotNull(books);
    assertEquals(2, books.size());
    assertTrue(books.contains(book1));
    assertTrue(books.contains(book2));

    verify(bookRepository).findAll();
  }
}
