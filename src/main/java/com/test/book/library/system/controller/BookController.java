package com.test.book.library.system.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.book.library.system.dto.request.BookRequest;
import com.test.book.library.system.dto.response.BookResponse;
import com.test.book.library.system.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing books in the library system.
 * Provides endpoints for registering new books and listing all books.
 */
@Validated
@RestController
@RequestMapping("/v1/library/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    // Inject the BookService to handle business logic related to books
    private final BookService bookService;

    // Endpoint to register a new book
    @Operation(summary = "Register a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<BookResponse> registerBook(@Valid @RequestBody BookRequest bookRequest) {

        // Controller receives DTO -> validates -> maps to Entity
        log.info("Register book request received. ISBN={}", bookRequest.getIsbn());
        var book = bookRequest.toEntity();
        if (book == null) {
            log.error("Failed to map BookRequest to Book entity. Request: {}", bookRequest);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.debug("Mapped BookRequest to Book entity: {}", book);

        // passes Entity to Service -> Service applies business rules -> returns Entity
        var bookSaved = bookService.registerBook(book);
        log.info("Book successfully registered with ID: {}", bookSaved.getId());

        // maps Entity back to DTO -> passes back as HTTP response
        var response = BookResponse.of(bookSaved);
        log.debug("Mapped Book entity to BookResponse: {}", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint to list all books
    @Operation(summary = "List all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping
    public ResponseEntity<List<BookResponse>> listBooks() {
        log.info("Received request to list all books");

        var books = bookService.getAllBooks();
        log.debug("Fetched {} books from service", books.size());

        var responseList = books.stream().map(BookResponse::of).toList();

        log.info("Returning {} books in response", responseList.size());

        return ResponseEntity.ok(responseList);
    }

    // Endpoint to list all available books (not currently borrowed)
    @Operation(summary = "List all available books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available books retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        log.info("Received request to list all available books");

        var books = bookService.getAvailableBooks();
        log.debug("Fetched {} books from service", books.size());

        var responseList = books.stream().map(BookResponse::of).toList();

        log.info("Returning {} books in response", responseList.size());

        return ResponseEntity.ok(responseList);
    }
}
