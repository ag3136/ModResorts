package com.acme.modres.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.logging.Logger;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ExceptionHandlerTest {

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleException_withNullException_shouldThrowServletException() {
        // Arrange
        String errorMsg = "Test error message";

        // Act & Assert
        ServletException exception = assertThrows(ServletException.class, () -> {
            ExceptionHandler.handleException(null, errorMsg, logger);
        });

        assertEquals(errorMsg, exception.getMessage());
    }

    @Test
    void testHandleException_withException_shouldThrowServletException() {
        // Arrange
        Exception originalException = new RuntimeException("Original exception");
        String errorMsg = "Test error message";

        // Act & Assert
        ServletException exception = assertThrows(ServletException.class, () -> {
            ExceptionHandler.handleException(originalException, errorMsg, logger);
        });

        assertEquals(errorMsg, exception.getMessage());
        assertEquals(originalException, exception.getCause());
    }

    @Test
    void testHandleException_withNullException_shouldLogSevere() {
        // Arrange
        String errorMsg = "Test error message";

        // Act & Assert
        assertThrows(ServletException.class, () -> {
            ExceptionHandler.handleException(null, errorMsg, logger);
        });

        verify(logger).severe(errorMsg);
    }

    @Test
    void testHandleException_withException_shouldLogWithLevel() {
        // Arrange
        Exception originalException = new RuntimeException("Original exception");
        String errorMsg = "Test error message";

        // Act & Assert
        assertThrows(ServletException.class, () -> {
            ExceptionHandler.handleException(originalException, errorMsg, logger);
        });

        verify(logger).log(any(), eq(errorMsg), eq(originalException));
    }

    @Test
    void testHandleException_withEmptyMessage_shouldThrowWithEmptyMessage() {
        // Arrange
        String errorMsg = "";

        // Act & Assert
        ServletException exception = assertThrows(ServletException.class, () -> {
            ExceptionHandler.handleException(null, errorMsg, logger);
        });

        assertEquals(errorMsg, exception.getMessage());
    }

    @Test
    void testHandleException_withLongMessage_shouldHandleCorrectly() {
        // Arrange
        String errorMsg = "This is a very long error message that should be handled correctly by the exception handler";
        Exception originalException = new RuntimeException("Test");

        // Act & Assert
        ServletException exception = assertThrows(ServletException.class, () -> {
            ExceptionHandler.handleException(originalException, errorMsg, logger);
        });

        assertEquals(errorMsg, exception.getMessage());
    }

    @Test
    void testHandleException_withIOException_shouldWrapInServletException() {
        // Arrange
        Exception originalException = new java.io.IOException("IO error");
        String errorMsg = "IO operation failed";

        // Act & Assert
        ServletException exception = assertThrows(ServletException.class, () -> {
            ExceptionHandler.handleException(originalException, errorMsg, logger);
        });

        assertEquals(errorMsg, exception.getMessage());
        assertTrue(exception.getCause() instanceof java.io.IOException);
    }
}
