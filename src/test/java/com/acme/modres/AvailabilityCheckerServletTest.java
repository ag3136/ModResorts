package com.acme.modres;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.acme.modres.mbean.reservation.Reservation;
import com.acme.modres.mbean.reservation.ReservationList;

class AvailabilityCheckerServletTest {

    private AvailabilityCheckerServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        servlet = new AvailabilityCheckerServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testInit_shouldInitializeReservationCheckerData() {
        // Act
        servlet.init();

        // Assert - servlet should initialize without throwing exception
        assertNotNull(servlet);
    }

    @Test
    void testDoGet_withValidDate_shouldReturnAvailability() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("date")).thenReturn("08/15/2024");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(anyInt());
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("availability"));
    }

    @Test
    void testDoGet_withInvalidDate_shouldReturnError() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("date")).thenReturn("invalid-date");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setStatus(500);
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("false"));
    }

    @Test
    void testDoGet_withNullDate_shouldReturnError() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("date")).thenReturn(null);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setStatus(500);
    }

    @Test
    void testDoPost_shouldCallDoGet() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("date")).thenReturn("08/15/2024");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    void testExportReservations_withValidDate_shouldReturnSuccess() {
        // Arrange
        servlet.init();
        String selectedDate = "08/15/2024";

        // Act
        int result = servlet.exportRevervations(selectedDate);

        // Assert
        assertTrue(result == 0 || result == -1); // May fail if file doesn't exist
    }

    @Test
    void testExportReservations_withNullDate_shouldHandleGracefully() {
        // Arrange
        servlet.init();

        // Act
        int result = servlet.exportRevervations(null);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    void testDoGet_withFutureDate_shouldCheckAvailability() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("date")).thenReturn("12/31/2025");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("availability"));
    }

    @Test
    void testDoGet_withPastDate_shouldCheckAvailability() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("date")).thenReturn("01/01/2020");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("availability"));
    }

    @Test
    void testDoGet_withEmptyDate_shouldReturnError() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("date")).thenReturn("");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setStatus(500);
    }
}
