package test.java.com.test.book.library.system.utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.test.book.library.system.exception.BookRegistrationFailureException;
import com.test.book.library.system.exception.BorrowBookFailedException;
import com.test.book.library.system.exception.BorrowerRegistrationFailureException;
import com.test.book.library.system.exception.ReturnBookFailedException;
import com.test.book.library.system.exception.GlobalExceptionHandler;
import com.test.book.library.system.exception.Error;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handle_BookRegistrationFailureException_ShouldReturnConflictError() {

        BookRegistrationFailureException ex = new BookRegistrationFailureException("Book registration failed",
                new RuntimeException("Root cause"));

        Error error = handler.handle(ex);

        assertNotNull(error);
        assertEquals("Book registration failed", error.getMessage());
        assertEquals("Root cause", error.getDetailedErrorMessage());
    }

    @Test
    void handle_BookRegistrationFailureException_WithoutCause_ShouldHandleGracefully() {

        BookRegistrationFailureException ex = new BookRegistrationFailureException("Book registration failed", null);

        Error error = handler.handle(ex);

        assertNotNull(error);
        assertEquals("Book registration failed", error.getMessage());
        assertEquals("", error.getDetailedErrorMessage());
    }

    @Test
    void handle_BorrowerRegistrationFailureException_ShouldReturnConflictError() {

        BorrowerRegistrationFailureException ex = new BorrowerRegistrationFailureException("Borrower failed",
                new RuntimeException("Cause"));

        Error error = handler.handle(ex);

        assertEquals("Borrower failed", error.getMessage());
        assertEquals("Cause", error.getDetailedErrorMessage());
    }

    @Test
    void handle_BorrowBookFailedException_ShouldReturnBadRequest() {

        BorrowBookFailedException ex = new BorrowBookFailedException("Borrow failed",
                new RuntimeException("Invalid state"));

        Error error = handler.handle(ex);

        assertEquals("Borrow failed", error.getMessage());
        assertEquals("Invalid state", error.getDetailedErrorMessage());
    }

    @Test
    void handle_ReturnBookFailedException_ShouldReturnBadRequest() {

        ReturnBookFailedException ex = new ReturnBookFailedException("Return failed",
                new RuntimeException("Already returned"));

        Error error = handler.handle(ex);

        assertEquals("Return failed", error.getMessage());
        assertEquals("Already returned", error.getDetailedErrorMessage());
    }
}