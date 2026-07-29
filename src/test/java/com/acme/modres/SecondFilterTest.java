package com.acme.modres;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SecondFilterTest {

    private SecondFilter filter;

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
        filter = new SecondFilter();
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
    void testDoFilter_withRequestContent_shouldAppendMessage() throws IOException, ServletException {
        // Arrange
        String requestContent = "Hello";
        BufferedReader reader = new BufferedReader(new StringReader(requestContent));
        when(request.getReader()).thenReturn(reader);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        verify(chain).doFilter(request, response);
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Hello to our site!"));
    }

    @Test
    void testDoFilter_withEmptyContent_shouldAppendMessage() throws IOException, ServletException {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader(""));
        when(request.getReader()).thenReturn(reader);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        verify(chain).doFilter(request, response);
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains(" to our site!"));
    }

    @Test
    void testDoFilter_withMultilineContent_shouldConcatenate() throws IOException, ServletException {
        // Arrange
        String requestContent = "Line1\nLine2\nLine3";
        BufferedReader reader = new BufferedReader(new StringReader(requestContent));
        when(request.getReader()).thenReturn(reader);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Line1Line2Line3 to our site!"));
    }

    @Test
    void testDoFilter_shouldCallChainDoFilter() throws IOException, ServletException {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("Test"));
        when(request.getReader()).thenReturn(reader);

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
        String requestContent = "Special@#$%";
        BufferedReader reader = new BufferedReader(new StringReader(requestContent));
        when(request.getReader()).thenReturn(reader);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Special@#$% to our site!"));
    }

    @Test
    void testDoFilter_withLongContent_shouldHandleCorrectly() throws IOException, ServletException {
        // Arrange
        String requestContent = "This is a very long content that should be handled correctly by the filter";
        BufferedReader reader = new BufferedReader(new StringReader(requestContent));
        when(request.getReader()).thenReturn(reader);

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        verify(response).setContentType("text/plain");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains(requestContent + " to our site!"));
    }
}
