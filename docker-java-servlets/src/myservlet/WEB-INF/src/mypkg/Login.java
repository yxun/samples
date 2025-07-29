package mypkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class Login extends HttpServlet
{
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
      HttpSession session=request.getSession();
      Cookie[] cookies = request.getCookies();
      for (int i = 0; i < cookies.length; i++) {
        String name = cookies[i].getName();
        if (name.equals("JSESSIONID")) {
          String value = cookies[i].getName() + "=" + cookies[i].getValue() + ";" + " Path=/PORTAL; Secure; HttpOnly;HttpOnly;Secure";
          response.setHeader("set-cookie", value);
        }
      }
      response.setContentType("application/octet-stream");
      response.setStatus(HttpServletResponse.SC_SEE_OTHER);
      String redirectUrl = "/myservlet/portal";
      response.setHeader("location", redirectUrl);  
  }
}
