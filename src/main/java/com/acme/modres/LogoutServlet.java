package com.acme.modres;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet({ "/logout" })
public class LogoutServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    try {
      // Replace IBM WebSphere proprietary WSSecurityHelper.revokeSSOCookies
      // with standard Jakarta EE session invalidation
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }
      
      // Clear authentication cookies if present
      // This is a standard approach that works across different application servers
      jakarta.servlet.http.Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (jakarta.servlet.http.Cookie cookie : cookies) {
          if (cookie.getName().contains("SSO") || cookie.getName().contains("JSESSIONID")) {
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
          }
        }
      }
    } catch (Exception e) {
      System.err.println("[ERROR] Error logging out");
      e.printStackTrace();
    }

    response.sendRedirect("login.jsp");
  }
}
