import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

 //Cross-site scripting (XSS) 
import org.apache.commons.text.StringEscapeUtils;
//Cross-site request forgery (CSRF) 
import org.apache.commons.lang3.StringUtils;


@WebServlet(urlPatterns = "/login")
public class login extends HttpServlet {
//Attaques par force brute
   private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    
   @Override
   public void doPost(HttpServletRequest r, HttpServletResponse s)throws ServletException, IOException {
         
        
       try{
           // Vérifier le nombre de tentatives de connexion
            if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                throw new ServletException("Trop de tentatives de connexion infructueuses, veuillez réessayer plus tard.");
            }
           
           
           //Cross-site request forgery (CSRF) 
           String csrfToken = (String) r.getSession().getAttribute("csrfToken");
           String requestCsrfToken = r.getParameter("csrfToken");

    // Validate CSRF token
    if (StringUtils.isBlank(csrfToken) || !csrfToken.equals(requestCsrfToken)) {
      throw new ServletException("Invalid CSRF token.");
    }
    
       String userP = r.getParameter("user");
       String pwdP = r.getParameter("pwd");
       
       
       //Cross-site scripting (XSS) only string
       if (StringUtils.isNumeric(userP)) {
           throw new ServletException("Invalid user.");
       }else {
           String user = StringEscapeUtils.escapeHtml4(userP);
           if (StringUtils.isNumeric(pwdP)) {
               throw new ServletException("Invalid password.");
           } else {
               String pwd = StringEscapeUtils.escapeHtml4(pwdP);
               
               //Les injections SQL
               
               if (isValidUsername(user) && isValidPassword(pwd)) {
                   if (user.equals("admin") && pwd.equals("admin")) {
                       HttpSession session = r.getSession();
                       session.setAttribute("user", user);
                       loginAttempts = 0;
                       RequestDispatcher rd = r.getRequestDispatcher("accueil.jsp");
                       rd.forward(r, s);
                   } else {
                       loginAttempts++;
                       RequestDispatcher rd2 = r.getRequestDispatcher("index.jsp");
                       PrintWriter p = s.getWriter();
                       p.print("<html><body>Erreur: Identifiants invalides</body></form>");
                       rd2.include(r, s);
                   }
               } else {
                   RequestDispatcher rd2 = r.getRequestDispatcher("index.jsp");
                   PrintWriter p = s.getWriter();
                   p.print("<html><body>Erreur: Identifiants invalides</body></form>");
                   rd2.include(r, s);
               }
           }
       }
       } catch (ServletException e) {
    HttpSession session = r.getSession();
    session.setAttribute("errorMessage", e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
    }
   }
     //Les injections SQL
   private boolean isValidUsername(String username) {
       return username != null && !username.matches("[^a-zA-Z0-9]");
   }

   private boolean isValidPassword(String password) {
       return password != null && !password.matches("[^a-zA-Z0-9]");
   }

}
