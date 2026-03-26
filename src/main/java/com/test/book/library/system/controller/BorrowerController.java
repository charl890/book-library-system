package com.test.book.library.system.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.book.library.system.dto.request.BorrowerRequest;
import com.test.book.library.system.dto.response.BorrowerResponse;
import com.test.book.library.system.service.BorrowerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing borrowers in the library system.
 * Provides endpoints for registering new borrowers and listing all borrowers.
 */
@Validated
@RestController
@RequestMapping("/v1/library/borrowers")
@RequiredArgsConstructor
@Slf4j
public class BorrowerController {

    // Inject the BorrowerService to handle business logic related to borrowers
    private final BorrowerService borrowerService;

    // Endpoint to register a new borrower
    @Operation(summary = "Register a new borrower")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Borrower registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<BorrowerResponse> registerBorrower(@Valid @RequestBody BorrowerRequest borrowerRequest) {

        log.info("Register borrower request received. Email={}", borrowerRequest.getEmail());

        // Controller receives DTO -> validates -> maps to Entity
        var borrower = borrowerRequest.toEntity();
        log.debug("Mapped BorrowerRequest to Borrower entity: {}", borrower);

        // passes Entity to Service -> Service applies business rules -> returns Entity
        var borrowerSaved = borrowerService.registerBorrower(borrower);
        log.info("Borrower successfully registered with ID: {}", borrowerSaved.getId());

        // maps Entity back to DTO -> passes back as HTTP response
        var response = BorrowerResponse.of(borrowerSaved);
        log.debug("Mapped Borrower entity to BorrowerResponse: {}", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint to list all borrowers
    @Operation(summary = "List all borrowers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrowers retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping
    public ResponseEntity<List<BorrowerResponse>> listBorrowers() {

        log.info("Received request to list all borrowers");

        var borrowers = borrowerService.getAllBorrowers();
        log.debug("Fetched {} borrowers from service", borrowers.size());

        var responseList = borrowers.stream().map(BorrowerResponse::of).toList();

        log.info("Returning {} borrowers in response", responseList.size());

        return ResponseEntity.ok(responseList);
    }
}
