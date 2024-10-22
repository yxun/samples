package mypkg;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class HelloServlet extends HttpServlet
{
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
      response.setContentType("text/html");  
      PrintWriter out = response.getWriter();  
      out.print("Hello world ");  
          
      HttpSession session=request.getSession();
      Cookie[] cookies = request.getCookies();
      for (int i = 0; i < cookies.length; i++) {
        out.print("cookie: " + cookies[i].getName() + ":" + cookies[i].getValue());
      }

      out.close();  
  }
}
