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
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Hello World</title>");
      out.println("</head>");
      out.println("<body>");
          
      HttpSession session=request.getSession();
      Cookie[] cookies = request.getCookies();
      for (int i = 0; i < cookies.length; i++) {
        out.println("<p>" + cookies[i].getName() + " : " + cookies[i].getValue() + "</p>");
      }

      out.println("</body></html>");
      out.close();  
  }
}
