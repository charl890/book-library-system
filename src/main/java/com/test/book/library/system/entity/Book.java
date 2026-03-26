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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.test.book.library.system.exception.BookStateException;

/**
 * Entity class representing a book in the library system.
 * This class is mapped to the "book" table in the database and includes fields
 * for the book's ID, ISBN, title, author, and a list of borrow records
 * associated with the book. Duplicate ISBNs are allowed but must have the same
 * title and author to ensure consistency.
 * The class also includes a method to validate consistency with another book
 * based on ISBN, title, and author.
 * It also includes a method to validate consistency with another book based on
 * ISBN.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOOK", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "BOOK_ID", "ISBN", "TITLE", "AUTHOR" })
})
@Slf4j
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "BOOK_ID", updatable = false, nullable = false, columnDefinition = "UUID", unique = true)
    private UUID id;

    @Column(name = "ISBN", nullable = false)
    private String isbn;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @OneToMany(mappedBy = "book")
    private List<BorrowRecord> borrowRecords = new ArrayList<>();

    /*
     * Validate same isbn exsistance
     */
    public void validateConsistencyWith(Book other) throws BookStateException {
        // same ISBN => must have same title and author
        if (!this.getTitle().equals(other.getTitle()) || !this.getAuthor().equals(other.getAuthor())) {

            String errorMessage = String.format(
                    "Invalid book registration: ISBN %s already exists with title='%s' and author='%s', "
                            + "but attempted registration has title='%s' and author='%s'.",
                    other.getIsbn(), other.getTitle(), other.getAuthor(), this.getTitle(), this.getAuthor());

            // Add logging for visibility (optional, but helpful in domain validations)
            log.warn("Book consistency check failed: {}", errorMessage);

            throw new BookStateException(errorMessage);
        }
    }
}
