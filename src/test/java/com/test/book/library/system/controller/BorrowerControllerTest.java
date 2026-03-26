package com.test.book.library.system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.book.library.system.dto.request.BorrowerRequest;
import com.test.book.library.system.entity.Borrower;
import com.test.book.library.system.service.BorrowerService;

import java.util.List;
import java.util.UUID;

/**
 * Test class for BorrowerController, which contains integration tests for the
 * borrower management endpoints in the library system.
 */
class BorrowerControllerTest {

  private MockMvc mockMvc;

  @Mock
  private BorrowerService borrowerService;

  @InjectMocks
  private BorrowerController borrowerController;

  private ObjectMapper objectMapper = new ObjectMapper();

  private BorrowerRequest borrowerRequest;
  private Borrower borrower;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(borrowerController).build();

    UUID borrowerId = UUID.randomUUID();

    borrowerRequest = BorrowerRequest.builder().name("John Doe").email("john@example.com").build();

    borrower = Borrower.builder().id(borrowerId).name(borrowerRequest.getName()).email(borrowerRequest.getEmail())
        .build();
  }

  @Test
  void registerBorrower_ShouldReturnBorrowerResponse() throws Exception {
    when(borrowerService.registerBorrower(any(Borrower.class))).thenReturn(borrower);

    mockMvc
        .perform(post("/v1/library/borrowers").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(borrowerRequest)))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.borrowerId").value(borrower.getId().toString()))
        .andExpect(jsonPath("$.name").value(borrower.getName()))
        .andExpect(jsonPath("$.email").value(borrower.getEmail()));

    verify(borrowerService).registerBorrower(any(Borrower.class));
  }

  @Test
  void listBorrowers_ShouldReturnListOfBorrowerResponses() throws Exception {
    when(borrowerService.getAllBorrowers()).thenReturn(List.of(borrower));

    mockMvc.perform(get("/v1/library/borrowers").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$[0].borrowerId").value(borrower.getId().toString()))
        .andExpect(jsonPath("$[0].name").value(borrower.getName()))
        .andExpect(jsonPath("$[0].email").value(borrower.getEmail()));

    verify(borrowerService).getAllBorrowers();
  }
}
