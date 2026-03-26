package com.test.book.library.system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.book.library.system.dto.request.BookRequest;
import com.test.book.library.system.entity.Book;
import com.test.book.library.system.service.BookService;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for BookController, which contains integration tests for the
 * book management endpoints in the library system.
 */
class BookControllerTest {

  private MockMvc mockMvc;

  @Mock
  private BookService bookService;

  @InjectMocks
  private BookController bookController;

  private ObjectMapper objectMapper = new ObjectMapper();

  private BookRequest bookRequest;
  private Book book;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

    bookRequest = BookRequest.builder().isbn("12345").title("Effective Java").author("Joshua Bloch").build();

    book = Book.builder().id(UUID.randomUUID()).isbn("12345").title("Effective Java").author("Joshua Bloch").build();
  }

  @Test
  void registerBook_ShouldReturnSavedBookResponse() throws Exception {
    when(bookService.registerBook(any(Book.class))).thenReturn(book);

    mockMvc
        .perform(post("/v1/library/books").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bookRequest)))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.bookId").value(book.getId().toString()))
        .andExpect(jsonPath("$.isbn").value(book.getIsbn())).andExpect(jsonPath("$.title").value(book.getTitle()))
        .andExpect(jsonPath("$.author").value(book.getAuthor()));

    verify(bookService).registerBook(any(Book.class));
  }

  @Test
  void listBooks_ShouldReturnListOfBookResponses() throws Exception {
    when(bookService.getAllBooks()).thenReturn(List.of(book));

    mockMvc.perform(get("/v1/library/books").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$[0].bookId").value(book.getId().toString()))
        .andExpect(jsonPath("$[0].isbn").value(book.getIsbn())).andExpect(jsonPath("$[0].title").value(book.getTitle()))
        .andExpect(jsonPath("$[0].author").value(book.getAuthor()));

    verify(bookService).getAllBooks();
  }

  @Test
  void getAvailableBooks_ShouldReturnListOfAvailableBookResponses() throws Exception {
    when(bookService.getAvailableBooks()).thenReturn(List.of(book));

    mockMvc.perform(get("/v1/library/books/available").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].bookId").value(book.getId().toString()))
        .andExpect(jsonPath("$[0].isbn").value(book.getIsbn())).andExpect(jsonPath("$[0].title").value(book.getTitle()))
        .andExpect(jsonPath("$[0].author").value(book.getAuthor()));

    verify(bookService).getAvailableBooks();
  }
}
