import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

    @WebServlet(urlPatterns = "/logout")
public class logout extends HttpServlet {

   
    
    public void doGet(HttpServletRequest r, HttpServletResponse s)
            throws ServletException, IOException {
            
        
         HttpSession session = r.getSession(false);
         session.invalidate();
         
         
         s.sendRedirect("index.jsp");
         
       
       
    }}
