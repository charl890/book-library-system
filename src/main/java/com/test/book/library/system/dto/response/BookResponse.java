package com.test.book.library.system.dto.response;

import java.util.UUID;

import com.test.book.library.system.entity.Book;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing a book in the library system.
 * This class is used to send book information back to clients in API responses.
 * It includes fields for the book's ID, ISBN, title, and author, and a static
 * method to create a BookResponse from a Book entity.
 */
@Data
@Builder
public class BookResponse {

  private UUID bookId;
  private String isbn;
  private String title;
  private String author;

  public static BookResponse of(Book book) {
    return BookResponse.builder().bookId(book.getId()).isbn(book.getIsbn()).title(book.getTitle())
        .author(book.getAuthor()).build();
  }
}
