package com.test.book.library.system.dto.request;

import com.test.book.library.system.entity.Borrower;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for creating a new borrower in the library system.
 * This class is used to receive borrower registration requests from clients.
 * It includes validation annotations to ensure that required fields are
 * provided
 * and that the email is in a valid format.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    public Borrower toEntity() {
        return Borrower.builder().name(this.name).email(this.email).build();
    }

}