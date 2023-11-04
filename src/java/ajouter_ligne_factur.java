import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
//Cross-site scripting (XSS)
import org.apache.commons.lang3.StringUtils;
//Les injections SQL 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.StringUtils;

@WebServlet(urlPatterns = "/add_ligne")
public class ajouter_ligne_factur extends HttpServlet {
       
      
    @Override
public void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException, ServletException {
    
    
    
    try {
        String csrfToken = (String) r.getSession().getAttribute("csrfToken");
    String requestCsrfToken = r.getParameter("csrfToken");

    // Validate CSRF token
    if (StringUtils.isBlank(csrfToken) || !csrfToken.equals(requestCsrfToken)) {
      throw new ServletException("Invalid CSRF token.");
    }
        
       String refArticleP = r.getParameter("Ref_article");
       String nFactureP = r.getParameter("N_facture");
       String quantiteVendueP = r.getParameter("Quantite_vendue");
        //Cross-site scripting (XSS)
         if (!StringUtils.isNumeric(refArticleP)) {
             throw new ServletException("Invalid article name.");
         }else {
             int refArticle = Integer.parseInt(refArticleP);
             
             if (!StringUtils.isNumeric(nFactureP)) {
                 throw new ServletException("Invalid quantity value.");
             } else {
                 int nFacture = Integer.parseInt(nFactureP);
                 
                 if (!StringUtils.isNumeric(quantiteVendueP)) {
                     throw new ServletException("Invalid selling price value.");
                 } else {
                     int quantiteVendue = Integer.parseInt(quantiteVendueP);
                     
                     //Les injections SQL
                     if (!isValidArticleReference(refArticle)) {
                         throw new ServletException("Invalid article reference number.");
                     }
                     if (!isValidInvoiceNumber(nFacture)) {
                         throw new ServletException("Invalid invoice number.");
                     }
                     if (!isValidQuantitySold(quantiteVendue)) {
                         throw new ServletException("Invalid quantity sold.");
                     }
                     Class.forName("com.mysql.jdbc.Driver");
                     Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact", "root", "");
                     
                     // Check if the article has enough quantity
                     PreparedStatement checkStatement = connection.prepareStatement("SELECT Quantite FROM article WHERE Ref_article = ?");
                     checkStatement.setInt(1, refArticle);
                     ResultSet checkResult = checkStatement.executeQuery();
                     
                     if (checkResult.next()) {
                         int articleQuantite = checkResult.getInt("Quantite");
                         // Check if quantiteVendue is within the valid range
                         if (quantiteVendue >= 0 && quantiteVendue <= articleQuantite) {
                             // Check if the ligne_facture record already exists
                             PreparedStatement existingLigneStatement = connection.prepareStatement("SELECT * FROM ligne_facture WHERE Ref_article = ? AND N_facture = ?");
                             existingLigneStatement.setInt(1, refArticle);
                             existingLigneStatement.setInt(2, nFacture);
                             ResultSet existingLigneResult = existingLigneStatement.executeQuery();
                             
                             
                             
                             
                             if (existingLigneResult.next()) {
                                 // Update existing ligne_facture record
                                 int existingQuantiteVendue = existingLigneResult.getInt("Quantite_vendue");
                                 int newQuantiteVendue = existingQuantiteVendue + quantiteVendue;
                                 PreparedStatement updateStatement = connection.prepareStatement("UPDATE ligne_facture SET Quantite_vendue = ? WHERE Ref_article = ? AND N_facture = ?");
                                 updateStatement.setInt(1, newQuantiteVendue);
                                 updateStatement.setInt(2, refArticle);
                                 updateStatement.setInt(3, nFacture);
                                 updateStatement.executeUpdate();
                                 
                                 // Update article.Quantite
                                 int updatedQuantite = articleQuantite - newQuantiteVendue;
                                 PreparedStatement updateArticleStatement = connection.prepareStatement("UPDATE article SET Quantite = ? WHERE Ref_article = ?");
                                 updateArticleStatement.setInt(1, updatedQuantite);
                                 updateArticleStatement.setInt(2, refArticle);
                                 updateArticleStatement.executeUpdate();
                             } else {
                                 // Insert new ligne_facture record
                                 PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO ligne_facture (Ref_article, N_facture, Quantite_vendue) VALUES (?, ?, ?)");
                                 insertStatement.setInt(1, refArticle);
                                 insertStatement.setInt(2, nFacture);
                                 insertStatement.setInt(3, quantiteVendue);
                                 insertStatement.executeUpdate();
                                 
                                 // Update article.Quantite
                                 int updatedQuantite = articleQuantite - quantiteVendue;
                                 PreparedStatement updateArticleStatement = connection.prepareStatement("UPDATE article SET Quantite = ? WHERE Ref_article = ?");
                                 updateArticleStatement.setInt(1, updatedQuantite);
                                 updateArticleStatement.setInt(2, refArticle);
                                 updateArticleStatement.executeUpdate();
                             }
                             
                             s.sendRedirect("web_add_facture.jsp?N_facture=" + nFacture);
                         } else {
                             
                             // Quantity validation failed
                             r.setAttribute("errorMessage", "Invalid quantity. Please enter a quantity between 0 and " + articleQuantite);
                             RequestDispatcher dispatcher = r.getRequestDispatcher("web_add_facture.jsp");
                             dispatcher.forward(r, s);
                         }
                     } else {
                         // Article not found
                         String errorMessage = "Article not found. Please enter a valid article.";
                         r.setAttribute("errorMessage", errorMessage);
                         r.getRequestDispatcher("web_add_facture.jsp").forward(r, s);
                         
                     }
                     
                     checkResult.close();
                     checkStatement.close();
                     connection.close();
                 }
             }
         }
      
        
     } catch (ServletException e) {
    HttpSession session = r.getSession();
    session.setAttribute("errorMessage", e.getMessage());
                           // Set the HTTPOnly cookie
                        Cookie cookie = new Cookie("sessionId", session.getId());
                        cookie.setHttpOnly(true);
                        s.addCookie(cookie);
    
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private boolean isValidArticleReference(int articleReference) {
    return articleReference > 0;  
}
private boolean isValidInvoiceNumber(int invoiceNumber) {
    return invoiceNumber > 0;  
}
private boolean isValidQuantitySold(int quantitySold) {
  
    return quantitySold >= 0;  
}
}