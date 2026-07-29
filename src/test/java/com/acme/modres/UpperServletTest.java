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

class UpperServletTest {

    private UpperServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        servlet = new UpperServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_withValidInput_shouldReturnUpperCase() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("input")).thenReturn("hello");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("upper case input"));
        assertTrue(output.contains("HELLO"));
    }

    @Test
    void testDoGet_withNullInput_shouldReturnEmptyUpperCase() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("input")).thenReturn(null);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("upper case input"));
    }

    @Test
    void testDoGet_withEmptyInput_shouldReturnEmpty() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("input")).thenReturn("");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("upper case input"));
    }

    @Test
    void testDoGet_withMixedCaseInput_shouldReturnUpperCase() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("input")).thenReturn("HeLLo WoRLd");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("upper case input"));
        assertTrue(output.contains("HELLO"));
    }

    @Test
    void testDoGet_withSpecialCharacters_shouldHandleCorrectly() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("input")).thenReturn("test@123");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("upper case input"));
    }

    @Test
    void testDoGet_withNumbers_shouldReturnUpperCase() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("input")).thenReturn("test123");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("upper case input"));
        assertTrue(output.contains("TEST123"));
    }

    @Test
    void testDoGet_withWhitespace_shouldHandleCorrectly() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("input")).thenReturn("hello world");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("upper case input"));
    }

    @Test
    void testDoGet_withLongString_shouldHandleCorrectly() throws ServletException, IOException {
        // Arrange
        String longInput = "this is a very long string that should be converted to uppercase";
        when(request.getParameter("input")).thenReturn(longInput);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("upper case input"));
    }
}
