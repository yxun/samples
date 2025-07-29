package mypkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class Role extends HttpServlet
{
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
      response.setContentType("text/html");  
      response.setHeader("cache-control", "no-cache, no-store Expires: 0");
      response.setStatus(HttpServletResponse.SC_FOUND);
      String redirectUrl = "/myservlet/portal";
      response.setHeader("location", redirectUrl);
  }
}
