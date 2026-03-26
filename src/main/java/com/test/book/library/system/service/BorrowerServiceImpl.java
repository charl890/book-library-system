package com.test.book.library.system.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.test.book.library.system.entity.Book;
import com.test.book.library.system.entity.Borrower;
import com.test.book.library.system.exception.BookRegistrationFailureException;
import com.test.book.library.system.exception.BookStateException;
import com.test.book.library.system.exception.BorrowerRegistrationFailureException;
import com.test.book.library.system.repository.BorrowerRepository;

import jakarta.transaction.Transactional;

/**
 * Service implementation for managing borrowers in the library system.
 * This class implements the BorrowerService interface and contains the business
 * logic for registering new borrowers and retrieving all borrowers from the
 * library.
 * It interacts with the BorrowerRepository to perform database operations
 * related to borrowers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Override
    @Transactional
    public Borrower registerBorrower(Borrower borrower) {

        log.info("Attempting to register borrower with name={} and email={}", borrower.getName(), borrower.getEmail());

        // Orchestration of the borrower registration
        Optional<Borrower> existingBorrowerOpt = borrowerRepository.findByEmail(borrower.getEmail());

        if (existingBorrowerOpt.isPresent()) {
            Borrower existingBorrower = existingBorrowerOpt.get();
            log.error("Borrower with email={} already exists ...", borrower.getEmail());

            throw new BorrowerRegistrationFailureException(
                    "Failed to register borrower: Email conflict wth existing borrower");
        }
        log.debug("Saving Borrower to repository: {}", borrower);

        try {
            Borrower savedBorrower = borrowerRepository.save(borrower);
            log.info("Borrower successfully registered with ID={} and email={}", savedBorrower.getId(),
                    savedBorrower.getEmail());
            return savedBorrower;
        } catch (Exception e) {
            log.error("Failed to save borrower to repository: {}", borrower, e);
            throw new BorrowerRegistrationFailureException("Failed to save borrower to repository", e);
        }
    }

    @Override
    public List<Borrower> getAllBorrowers() {
        log.info("Fetching all borrowers from repository");

        List<Borrower> borrowers = borrowerRepository.findAll();
        log.debug("Fetched {} borrowers", borrowers.size());

        return borrowers;
    }
}
