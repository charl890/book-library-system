package com.test.book.library.system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.test.book.library.system.dto.request.BorrowRequest;
import com.test.book.library.system.entity.Book;
import com.test.book.library.system.entity.BorrowRecord;
import com.test.book.library.system.entity.Borrower;
import com.test.book.library.system.service.BorrowService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Test class for BorrowController, which contains integration tests for the
 * borrowing endpoints in the library system.
 */
class BorrowControllerTest {

        private MockMvc mockMvc;

        @Mock
        private BorrowService borrowService;

        @InjectMocks
        private BorrowController borrowController;

        private ObjectMapper objectMapper = new ObjectMapper();

        private BorrowRequest borrowRequest;
        private BorrowRecord borrowRecord;
        private Book book;
        private Borrower borrower;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                mockMvc = MockMvcBuilders.standaloneSetup(borrowController).build();

                UUID bookId = UUID.randomUUID();
                UUID borrowerId = UUID.randomUUID();
                UUID borrowRecordId = UUID.randomUUID();

                book = Book.builder().id(bookId).isbn("12345").title("Effective Java").author("Joshua Bloch").build();

                borrower = Borrower.builder().id(borrowerId).name("John Doe").email("john@example.com").build();

                borrowRecord = BorrowRecord.builder().recordId(borrowRecordId).book(book).borrower(borrower)
                                .borrowMsg("Borrowed for testing").borrowedAt(LocalDateTime.now()).returnedAt(null)
                                /* active(true) */.build();

                borrowRequest = BorrowRequest.builder().bookId(bookId).borrowerId(borrowerId)
                                .borrowMsg("Borrowed for testing").build();
        }

        @Test
        void borrowBook_ShouldReturnBorrowResponse() throws Exception {
                when(borrowService.borrowBook(any(UUID.class), any(UUID.class), anyString())).thenReturn(borrowRecord);

                mockMvc.perform(post("/v1/library/borrows").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(borrowRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.recordId").value(borrowRecord.getRecordId().toString()))
                                .andExpect(jsonPath("$.bookId").value(borrowRecord.getBook().getId().toString()))
                                .andExpect(jsonPath("$.borrowerId")
                                                .value(borrowRecord.getBorrower().getId().toString()));

                verify(borrowService).borrowBook(borrowRequest.getBorrowerId(), borrowRequest.getBookId(),
                                borrowRequest.getBorrowMsg());
        }

        @Test
        void returnBook_ShouldReturnBorrowResponse() throws Exception {
                UUID borrowRecordId = borrowRecord.getRecordId();
                when(borrowService.returnBook(borrowRecordId)).thenReturn(borrowRecord);

                mockMvc.perform(post("/v1/library/borrows/" + borrowRecordId + "/return")
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                                .andExpect(jsonPath("$.recordId").value(borrowRecord.getRecordId().toString()));

                verify(borrowService).returnBook(borrowRecordId);
        }

        @Test
        void getCurrentBorrowRecords_ShouldReturnListOfBorrowRecords() throws Exception {
                when(borrowService.getCurrentBorrowRecords()).thenReturn(List.of(borrowRecord));

                mockMvc.perform(get("/v1/library/borrows/active").contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].borrowId").value(borrowRecord.getRecordId().toString()));

                verify(borrowService).getCurrentBorrowRecords();
        }
}
