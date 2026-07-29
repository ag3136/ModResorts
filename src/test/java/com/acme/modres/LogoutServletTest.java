package com.acme.modres;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LogoutServletTest {

    private LogoutServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new LogoutServlet();
    }

    @Test
    void testDoGet_withValidSession_shouldInvalidateSession() throws IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(request.getCookies()).thenReturn(null);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void testDoGet_withNullSession_shouldNotThrowException() throws IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void testDoGet_withSSOCookie_shouldClearCookie() throws IOException {
        // Arrange
        Cookie ssoCookie = new Cookie("SSO_TOKEN", "value");
        Cookie[] cookies = { ssoCookie };
        when(request.getSession(false)).thenReturn(session);
        when(request.getCookies()).thenReturn(cookies);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(session).invalidate();
        verify(response).addCookie(any(Cookie.class));
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void testDoGet_withJSESSIONIDCookie_shouldClearCookie() throws IOException {
        // Arrange
        Cookie sessionCookie = new Cookie("JSESSIONID", "sessionValue");
        Cookie[] cookies = { sessionCookie };
        when(request.getSession(false)).thenReturn(session);
        when(request.getCookies()).thenReturn(cookies);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(session).invalidate();
        verify(response).addCookie(any(Cookie.class));
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void testDoGet_withMultipleCookies_shouldClearOnlyAuthCookies() throws IOException {
        // Arrange
        Cookie ssoCookie = new Cookie("SSO_TOKEN", "value");
        Cookie sessionCookie = new Cookie("JSESSIONID", "sessionValue");
        Cookie otherCookie = new Cookie("OTHER", "otherValue");
        Cookie[] cookies = { ssoCookie, sessionCookie, otherCookie };
        when(request.getSession(false)).thenReturn(session);
        when(request.getCookies()).thenReturn(cookies);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(session).invalidate();
        verify(response, atLeast(2)).addCookie(any(Cookie.class));
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void testDoGet_withNoCookies_shouldStillInvalidateSession() throws IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(request.getCookies()).thenReturn(new Cookie[0]);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(session).invalidate();
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void testDoGet_withException_shouldStillRedirect() throws IOException {
        // Arrange
        when(request.getSession(false)).thenThrow(new RuntimeException("Test exception"));

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void testDoGet_shouldAlwaysRedirectToLoginPage() throws IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response, times(1)).sendRedirect("login.jsp");
    }
}
