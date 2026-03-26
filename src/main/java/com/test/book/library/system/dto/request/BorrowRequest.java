package com.test.book.library.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for borrowing a book in the library system.
 * This class is used to receive borrow requests from clients, containing
 * the IDs of the borrower and the book, as well as an optional message.
 * It includes validation annotations to ensure that required fields are
 * provided.
 */
@Data
@Builder
public class BorrowRequest {

    @NotNull
    private UUID borrowerId;

    @NotNull
    private UUID bookId;

    @NotBlank(message = "Borrow message is required")
    private String borrowMsg;
}