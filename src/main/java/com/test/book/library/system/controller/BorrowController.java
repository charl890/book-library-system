package com.test.book.library.system.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.book.library.system.dto.request.BorrowRequest;
import com.test.book.library.system.dto.response.BorrowRecordResponse;
import com.test.book.library.system.dto.response.BorrowResponse;
import com.test.book.library.system.service.BorrowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing borrow and return operations in the library
 * system.
 * Provides endpoints for borrowing books, returning books, and listing current
 * borrow records.
 */
@Validated
@RestController
@RequestMapping("/v1/library/borrows")
@RequiredArgsConstructor
@Slf4j
public class BorrowController {

        // Inject the BorrowService to handle business logic related to borrowing and
        // returning books
        private final BorrowService borrowService;

        // Endpoint to borrow a book
        @Operation(summary = "Borrow a book")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Book borrowed"),
                        @ApiResponse(responseCode = "400", description = "Invalid input")
        })
        @PostMapping
        public ResponseEntity<BorrowResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {

                log.info("Received borrow request. borrowerId={}, bookId={}",
                                request.getBorrowerId(), request.getBookId());

                var borrowRecord = borrowService.borrowBook(request.getBorrowerId(), request.getBookId(),
                                request.getBorrowMsg());

                log.info("Book borrowed successfully. BorrowRecordId={}", borrowRecord.getRecordId());

                var response = BorrowResponse.of(borrowRecord);
                log.debug("Mapped BorrowRecord to BorrowResponse: {}", response);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        // Endpoint to return a book
        @Operation(summary = "Return a book")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Book returned"),
                        @ApiResponse(responseCode = "400", description = "Invalid input")
        })
        @PostMapping("/{id}/return")
        public ResponseEntity<BorrowResponse> returnBook(@PathVariable("id") UUID borrowRecordId) {

                log.info("Received return request for BorrowRecordId={}", borrowRecordId);

                var borrowRecord = borrowService.returnBook(borrowRecordId);
                log.info("Book returned successfully. BorrowRecordId={}", borrowRecord.getRecordId());

                var response = BorrowResponse.of(borrowRecord);
                log.debug("Mapped BorrowRecord to BorrowResponse: {}", response);

                return ResponseEntity.ok(response);
        }

        // Endpoint to list all current borrow records (books that are currently
        // actively borrowed)
        @Operation(summary = "List current borrow records")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Borrow records retrieved"),
                        @ApiResponse(responseCode = "400", description = "Invalid input")
        })
        @GetMapping("/active")
        public ResponseEntity<List<BorrowRecordResponse>> getCurrentBorrowRecords() {

                log.info("Received request to fetch current borrow records");

                var records = borrowService.getCurrentBorrowRecords();
                log.debug("Fetched {} active borrow records", records.size());

                var responseList = records.stream().map(BorrowRecordResponse::of).toList();

                log.info("Returning {} borrow records in response", responseList.size());

                return ResponseEntity.ok(responseList);
        }
}
