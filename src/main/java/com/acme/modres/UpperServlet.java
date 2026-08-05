package com.acme.modres;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ibm.websphere.servlet.response.ResponseUtils;

/**
 * UpperServlet - Uses IBM WebSphere-specific APIs
 * Note: This servlet uses com.ibm.websphere.servlet.response.ResponseUtils
 * which is a vendor-specific API. This will only work on IBM WebSphere servers.
 * For Java 11 compatibility, ensure WebSphere Liberty or WebSphere 9.0+ is used.
 */
@WebServlet("/resorts/upper")
public class UpperServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");

    String originalStr = request.getParameter("input");
    if (originalStr == null) {
      originalStr = "";
    }

    String newStr = originalStr.toUpperCase();
    // Using WebSphere-specific API for encoding
    newStr = ResponseUtils.encodeDataString(newStr);

    PrintWriter out = response.getWriter();
    out.print("<br/><b>upper case input " + newStr + "</b>");
  }
}
