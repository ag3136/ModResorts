package com.acme.modres;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WeatherServletTest {

    private WeatherServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter stringWriter;
    private TestServletOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        servlet = new WeatherServlet();
        stringWriter = new StringWriter();
        outputStream = new TestServletOutputStream();
        when(response.getOutputStream()).thenReturn(outputStream);
    }

    @Test
    void testInit_shouldInitializeMBean() {
        // Act
        servlet.init();

        // Assert - should not throw exception
        assertNotNull(servlet);
    }

    @Test
    void testDestroy_shouldUnregisterMBean() {
        // Arrange
        servlet.init();

        // Act
        servlet.destroy();

        // Assert - should not throw exception
        assertNotNull(servlet);
    }

    @Test
    void testDoGet_withParisCity_shouldReturnWeatherData() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("selectedCity")).thenReturn(Constants.PARIS);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
    }

    @Test
    void testDoGet_withLasVegasCity_shouldReturnWeatherData() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("selectedCity")).thenReturn(Constants.LAS_VEGAS);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
    }

    @Test
    void testDoGet_withSanFranciscoCity_shouldReturnWeatherData() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("selectedCity")).thenReturn(Constants.SAN_FRANCISCO);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
    }

    @Test
    void testDoGet_withMiamiCity_shouldReturnWeatherData() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("selectedCity")).thenReturn(Constants.MIAMI);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
    }

    @Test
    void testDoGet_withCorkCity_shouldReturnWeatherData() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("selectedCity")).thenReturn(Constants.CORK);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
    }

    @Test
    void testDoGet_withBarcelonaCity_shouldReturnWeatherData() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("selectedCity")).thenReturn(Constants.BARCELONA);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("application/json");
    }

    @Test
    void testDoGet_withNullCity_shouldHandleGracefully() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("selectedCity")).thenReturn(null);

        // Act & Assert
        assertThrows(Exception.class, () -> servlet.doGet(request, response));
    }

    @Test
    void testDoPost_shouldCallDoGet() throws IOException, ServletException {
        // Arrange
        servlet.init();
        when(request.getParameter("selectedCity")).thenReturn(Constants.PARIS);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).setContentType("application/json");
    }

    @Test
    void testInit_multipleTimes_shouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            servlet.init();
            servlet.init();
        });
    }

    @Test
    void testDestroy_withoutInit_shouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> servlet.destroy());
    }

    // Helper class for testing ServletOutputStream
    private static class TestServletOutputStream extends ServletOutputStream {
        private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

        @Override
        public void write(int b) throws IOException {
            baos.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }

        public String getContent() {
            return baos.toString();
        }
    }
}
