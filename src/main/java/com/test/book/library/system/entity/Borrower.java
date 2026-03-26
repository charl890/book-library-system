package com.test.book.library.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.test.book.library.system.exception.BookStateException;
import com.test.book.library.system.exception.BorrowerStateException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;

/**
 * Entity class representing a borrower in the library system.
 * This class is mapped to the "borrower" table in the database and includes
 * fields for the borrower's ID, name, email, and a list of borrow records
 * associated with the borrower.
 * The email field is unique to prevent multiple borrowers from registering with
 * the same email address,
 * which helps maintain data integrity and allows for reliable identification of
 * borrowers in the system.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BORROWER", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "BORROWER_ID", "NAME", "EMAIL" })
})
@Slf4j
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "BORROWER_ID", updatable = false, nullable = false, columnDefinition = "UUID", unique = true)
    private UUID id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "borrower")
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
}