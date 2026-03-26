package com.test.book.library.system.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.book.library.system.entity.Borrower;

/**
 * Repository interface for managing Borrower entities in the library system.
 * This interface extends JpaRepository, providing CRUD operations and custom
 * query methods for interacting with the "borrower" table in the database.
 * It includes methods to find borrowers by email and check for the existence
 * of borrowers by email.
 */
@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, UUID> {

    // save is inherited from JpaRepository, no need to declare it explicitly
    // Borrower save(Borrower borrower);

    // findAll is inherited from JpaRepository, no need to declare it explicitly
    // List<Borrower> findAll();

    // findById is inherited from JpaRepository, no need to declare it explicitly
    // Optional<Borrower> findById(UUID borrowerId);

    // Custom query to find a borrower by email
    Optional<Borrower> findByEmail(String email);

    // Check if a borrower with the given email already exists
    boolean existsByEmail(String email);
}
