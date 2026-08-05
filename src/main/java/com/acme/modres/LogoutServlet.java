package com.acme.modres;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ibm.websphere.security.WSSecurityHelper;

import java.io.IOException;

/**
 * LogoutServlet - Uses IBM WebSphere-specific security APIs
 * Note: This servlet uses com.ibm.websphere.security.WSSecurityHelper
 * which is a vendor-specific API. This will only work on IBM WebSphere servers.
 * For Java 11 compatibility, ensure WebSphere Liberty or WebSphere 9.0+ is used.
 */
@WebServlet({ "/logout" })
public class LogoutServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    try {
      // Using WebSphere-specific security helper
      WSSecurityHelper.revokeSSOCookies(request, response);
    } catch (Exception e) {
      System.err.println("[ERROR] Error logging out");
      e.printStackTrace();
    }

    response.sendRedirect("login.jsp");
  }
}
