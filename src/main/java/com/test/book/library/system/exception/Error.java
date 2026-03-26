package com.test.book.library.system.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing an error in the library system.
 * This class is used to send error information back to clients in API
 * responses.
 * It includes fields for a general error message and a more detailed error
 * message, which can be useful for debugging or providing additional context
 * about the error.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private String message;

    private String detailedErrorMessage;
}