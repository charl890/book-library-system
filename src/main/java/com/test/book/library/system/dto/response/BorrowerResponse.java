package com.test.book.library.system.dto.response;

import java.util.UUID;

import com.test.book.library.system.entity.Borrower;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing a borrower in the library system.
 * This class is used to send borrower information back to clients in API
 * responses.
 * It includes fields for the borrower's ID, name, and email, and a static
 * method to create a BorrowerResponse from a Borrower entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerResponse {

  private UUID borrowerId;
  private String name;
  private String email;

  public static BorrowerResponse of(Borrower borrower) {
    return BorrowerResponse.builder().borrowerId(borrower.getId()).name(borrower.getName()).email(borrower.getEmail())
        .build();
  }

}