package com.test.book.library.system.dto.request;

import com.test.book.library.system.entity.Book;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for creating a new book in the library system.
 * This class is used to receive book registration requests from clients.
 * It includes validation annotations to ensure that required fields are
 * provided.
 */
@Data
@Builder
public class BookRequest {

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    public Book toEntity() {
        return Book.builder().title(this.title).author(this.author).isbn(this.isbn).build();
    }
}