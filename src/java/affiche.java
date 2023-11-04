import com.mysql.jdbc.Driver;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

//Les injections SQL 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(urlPatterns="/afficher")
public class affiche extends HttpServlet {
    //Les injections SQL
private static final Logger log = LogManager.getLogger(affiche.class);

    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
          
        try {
            String parameter = request.getParameter("content");
            if (parameter != null) {
                if (parameter.equals("client")) {
                    displayClientContent(request, response);
                } else if (parameter.equals("article")) {
                    displayArticleContent(request, response);
                } else if (parameter.equals("facture")) {
                    displayFactureContent(request, response);
                } else {
                    displayDefaultContent(response);
                }
            } else {
                displayDefaultContent(response);
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }
    
    private void displayClientContent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {           
            // Fetch client data from the database
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact","root","");
            PreparedStatement pst = c.prepareStatement("select * from client;");
            ResultSet rs = pst.executeQuery();
            
            // Set the client data as a request attribute
            request.setAttribute("clientData", rs);
            
            // Forward the request to affiche_table.jsp for rendering
            RequestDispatcher dispatcher = request.getRequestDispatcher("affiche_table.jsp");
            dispatcher.forward(request, response);
            
            //Les injections SQL 
         } catch (SQLException e) {
        log.error("An error occurred while processing the request.", e);
        throw new ServletException("An error occurred while processing the request.", e);
       } catch (Exception e) {
        log.error("An unexpected error occurred.", e);
        throw new ServletException("An unexpected error occurred.", e);
    }
        
    }
    
    private void displayArticleContent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Fetch article data from the database
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact","root","");
            PreparedStatement pst = c.prepareStatement("select * from article;");
            ResultSet rs = pst.executeQuery();
            
            // Set the article data as a request attribute
            request.setAttribute("articleData", rs);
            
            // Forward the request to affiche_table.jsp for rendering
            RequestDispatcher dispatcher = request.getRequestDispatcher("affiche_table.jsp");
            dispatcher.forward(request, response);
            
            //Les injections SQL 
        } catch (SQLException e) {
        log.error("An error occurred while processing the request.", e);
        throw new ServletException("An error occurred while processing the request.", e);
       } catch (Exception e) {
        log.error("An unexpected error occurred.", e);
        throw new ServletException("An unexpected error occurred.", e);
    }
    }
    
    private void displayFactureContent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Fetch facture data from the database
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact","root","");
            PreparedStatement pst = c.prepareStatement("SELECT facture.*, client.Nom_client FROM facture INNER JOIN client ON facture.Id_client = client.Id_client;");
            ResultSet rs = pst.executeQuery();
            
            // Set the facture data as a request attribute
            request.setAttribute("factureData", rs);
            
            // Forward the request to affiche_table.jsp for rendering
            RequestDispatcher dispatcher = request.getRequestDispatcher("affiche_table.jsp");
            dispatcher.forward(request, response);
            
            //Les injections SQL Évitez d'afficher des messages d'erreur détaillés
        } catch (SQLException e) {
        log.error("An error occurred while processing the request.", e);
        throw new ServletException("An error occurred while processing the request.", e);
       } catch (Exception e) {
        log.error("An unexpected error occurred.", e);
        throw new ServletException("An unexpected error occurred.", e);
    }
    }
    
    private void displayDefaultContent(HttpServletResponse response) throws IOException {
        // Display default content
        PrintWriter p = response.getWriter();
        p.print("<html><body>"
                + "<h1>Default Content</h1>"
                + "</body></html>");
    }
}
