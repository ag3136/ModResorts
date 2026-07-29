package com.acme.modres;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FirstFilterTest {

    private FirstFilter filter;

    @Mock
    private FilterConfig filterConfig;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        filter = new FirstFilter();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testInit_shouldInitializeFilter() throws ServletException {
        // Act
        filter.init(filterConfig);

        // Assert - should not throw exception
        assertNotNull(filter);
    }

    @Test
    void testDoFilter_withUserParameter_shouldWriteWelcomeMessage() throws IOException, ServletException {
        // Arrange
        when(request.getParameter("user")).thenReturn("John");

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        verify(chain).doFilter(request, response);
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Welcome John"));
    }

    @Test
    void testDoFilter_withoutUserParameter_shouldUseDefaultUser() throws IOException, ServletException {
        // Arrange
        when(request.getParameter("user")).thenReturn(null);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        verify(chain).doFilter(request, response);
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Welcome defaultUser"));
    }

    @Test
    void testDoFilter_withEmptyUserParameter_shouldUseEmptyString() throws IOException, ServletException {
        // Arrange
        when(request.getParameter("user")).thenReturn("");

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        verify(chain).doFilter(request, response);
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Welcome "));
    }

    @Test
    void testDoFilter_shouldCallChainDoFilter() throws IOException, ServletException {
        // Arrange
        when(request.getParameter("user")).thenReturn("TestUser");

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDestroy_shouldCleanupResources() {
        // Act
        filter.destroy();

        // Assert - should not throw exception
        assertNotNull(filter);
    }

    @Test
    void testDoFilter_withSpecialCharacters_shouldHandleCorrectly() throws IOException, ServletException {
        // Arrange
        when(request.getParameter("user")).thenReturn("User@123");

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Welcome User@123"));
    }

    @Test
    void testDoFilter_withLongUsername_shouldHandleCorrectly() throws IOException, ServletException {
        // Arrange
        String longUsername = "VeryLongUsernameWithManyCharacters";
        when(request.getParameter("user")).thenReturn(longUsername);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Welcome " + longUsername));
    }
}
