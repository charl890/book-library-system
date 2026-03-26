package com.test.book.library.system.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.test.book.library.system.entity.Borrower;
import com.test.book.library.system.repository.BorrowerRepository;
import com.test.book.library.system.service.BorrowerServiceImpl;

import com.test.book.library.system.exception.BorrowerRegistrationFailureException;

/**
 * Test class for BorrowerServiceImpl, which contains unit tests for the
 * borrower management functionality in the library system.
 * This class uses Mockito to mock dependencies and JUnit 5 for assertions and
 * test structure.
 * The tests cover scenarios such as registering a new borrower, and retrieving
 * all borrowers from the repository.
 */
class BorrowerServiceImplTest {

  @Mock
  private BorrowerRepository borrowerRepository;

  @InjectMocks
  private BorrowerServiceImpl borrowerService;

  private Borrower borrower1;
  private Borrower borrower2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    borrower1 = Borrower.builder()
        .id(UUID.randomUUID())
        .name("Eric")
        .email("eramda@example.com")
        .build();

    borrower2 = Borrower.builder()
        .id(UUID.randomUUID())
        .name("Bob")
        .email("enu@example.com")
        .build();
  }

  @Test
  void registerBorrower_ShouldReturnSavedBorrower() {
    when(borrowerRepository.save(borrower1)).thenReturn(borrower1);

    Borrower saved = borrowerService.registerBorrower(borrower1);

    assertNotNull(saved);
    assertEquals("Eric", saved.getName());
    verify(borrowerRepository).save(borrower1);
  }

  @Test
  void getAllBorrowers_ShouldReturnListOfBorrowers() {
    when(borrowerRepository.findAll()).thenReturn(Arrays.asList(borrower1, borrower2));

    List<Borrower> borrowers = borrowerService.getAllBorrowers();

    assertNotNull(borrowers);
    assertEquals(2, borrowers.size());
    assertTrue(borrowers.contains(borrower1));
    assertTrue(borrowers.contains(borrower2));
    verify(borrowerRepository).findAll();
  }

  @Test
  void getAllBorrowers_WhenNoBorrowers_ShouldReturnEmptyList() {
    when(borrowerRepository.findAll()).thenReturn(List.of());

    List<Borrower> borrowers = borrowerService.getAllBorrowers();

    assertNotNull(borrowers);
    assertTrue(borrowers.isEmpty());
    verify(borrowerRepository).findAll();
  }

  @Test
  void shouldRegisterBorrowerSuccessfully() {
    Borrower borrower = new Borrower();
    UUID uuid = UUID.randomUUID();

    borrower.setName("John");
    borrower.setEmail("john@example.com");

    when(borrowerRepository.findByEmail("john@example.com"))
        .thenReturn(Optional.empty());

    when(borrowerRepository.save(any(Borrower.class)))
        .thenAnswer(invocation -> {
          Borrower b = invocation.getArgument(0);
          b.setId(uuid); // Simulate generated ID
          return b;
        });

    Borrower result = borrowerService.registerBorrower(borrower);

    assertNotNull(result);
    assertEquals(uuid, result.getId());
    assertEquals("john@example.com", result.getEmail());

    verify(borrowerRepository).findByEmail("john@example.com");
    verify(borrowerRepository).save(borrower);
  }

  @Test
  void shouldThrowExceptionWhenEmailAlreadyExists() {
    Borrower borrower = new Borrower();
    borrower.setName("John");
    borrower.setEmail("john@example.com");

    when(borrowerRepository.findByEmail("john@example.com"))
        .thenReturn(Optional.of(new Borrower()));

    BorrowerRegistrationFailureException exception = assertThrows(
        BorrowerRegistrationFailureException.class,
        () -> borrowerService.registerBorrower(borrower));

    assertTrue(exception.getMessage().contains("Email conflict"));

    verify(borrowerRepository).findByEmail("john@example.com");
    verify(borrowerRepository, never()).save(any());
  }

  @Test
  void shouldThrowExceptionWhenSaveFails() {
    Borrower borrower = new Borrower();
    borrower.setName("John");
    borrower.setEmail("john@example.com");

    when(borrowerRepository.findByEmail("john@example.com"))
        .thenReturn(Optional.empty());

    when(borrowerRepository.save(any()))
        .thenThrow(new RuntimeException("DB failure"));

    BorrowerRegistrationFailureException exception = assertThrows(
        BorrowerRegistrationFailureException.class,
        () -> borrowerService.registerBorrower(borrower));

    assertEquals("Failed to save borrower to repository", exception.getMessage());

    verify(borrowerRepository).save(borrower);
  }
}
