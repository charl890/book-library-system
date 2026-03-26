package com.test.book.library.system.service;

import java.util.List;

import com.test.book.library.system.entity.Borrower;

/**
 * Service interface for managing borrowers in the library system.
 * This interface defines the contract for borrower-related operations, such as
 * registering new borrowers and retrieving all borrowers from the library.
 * Implementations of this interface will contain the business logic for these
 * operations, interacting with the underlying data repositories as needed.
 */
public interface BorrowerService {

    Borrower registerBorrower(Borrower borrower);

    List<Borrower> getAllBorrowers();
}
