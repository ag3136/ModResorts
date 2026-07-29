package com.acme.modres;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WelcomeServletTest {

    private WelcomeServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        servlet = new WelcomeServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_shouldReturnEnjoyMessage() throws ServletException, IOException {
        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/plain");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Enjoy!"));
    }

    @Test
    void testDoGet_shouldSetCorrectContentType() throws ServletException, IOException {
        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response, times(1)).setContentType("text/plain");
    }

    @Test
    void testDoGet_shouldWriteToResponse() throws ServletException, IOException {
        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).getWriter();
        writer.flush();
        assertFalse(stringWriter.toString().isEmpty());
    }

    @Test
    void testDoGet_multipleInvocations_shouldReturnSameMessage() throws ServletException, IOException {
        // Act
        servlet.doGet(request, response);
        writer.flush();
        String firstOutput = stringWriter.toString();

        // Reset writer
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);
        writer.flush();
        String secondOutput = stringWriter.toString();

        // Assert
        assertEquals(firstOutput, secondOutput);
    }

    @Test
    void testDoGet_shouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> servlet.doGet(request, response));
    }
}
