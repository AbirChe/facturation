import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Driver;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpSession;
//Cross-site scripting (XSS)
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
//Les injections SQL 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//Cross-site request forgery (CSRF) 
import org.apache.commons.lang3.StringUtils;

@WebServlet(urlPatterns = "/ajouter")
public class ajout extends HttpServlet {
    
    
   private static final Logger log = LogManager.getLogger(ajout.class);

    private void processForm1(HttpServletRequest r, HttpServletResponse s) throws IOException, ServletException {
        try {
            //Cross-site request forgery (CSRF) 
             String csrfToken = (String) r.getSession().getAttribute("csrfToken");
             String requestCsrfToken = r.getParameter("csrfToken");

    // Validate CSRF token
    if (StringUtils.isBlank(csrfToken) || !csrfToken.equals(requestCsrfToken)) {
      throw new ServletException("Invalid CSRF token.");
    }
            
            String Nom_clientP = r.getParameter("Nom_client");
            String TelephoneP = r.getParameter("Telephone");
            String EmailP = r.getParameter("Email");
            
            
            //Cross-site scripting (XSS) 
            // vérifier que les entrées de l'utilisateur sont du type attendu.
           if (StringUtils.isNumeric(Nom_clientP)) {
               throw new ServletException("Invalid client name.");
           }else {
               // pour nettoyer toutes les entrées de l'utilisateur avant de les utiliser
               String Nom_client = StringEscapeUtils.escapeHtml4(Nom_clientP);
               
               if (!StringUtils.isNumeric(TelephoneP)) {
                   throw new ServletException("Invalid phone number.");
               } else {
                   int Telephone = Integer.parseInt(TelephoneP);
                   
                   if (StringUtils.isNumeric(EmailP)) {
                       throw new ServletException("Invalid email address.");
                   } else {
                       String Email = StringEscapeUtils.escapeHtml4(EmailP);
                       
                       //Les injections SQL
                       if (!isValidname(Nom_client)) {
                           throw new ServletException("Invalid client name.");
                       }
                       if (!isValidPhoneNumber(Telephone)) {
                           throw new ServletException("Invalid phone number.");
                       }
                       if (!isValidEmail(Email)) {
                           throw new ServletException("Invalid email address.");
                       }
                       
                       Class.forName("com.mysql.jdbc.Driver");
                       Connection c = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact", "root", "");
                       PreparedStatement pst = c.prepareStatement("insert into client (Nom_client,Telephone,Email) values (?,?,?);");
                       
                       pst.setString(1, Nom_client);
                       pst.setInt(2, Telephone);
                       pst.setString(3, Email);
                       pst.executeUpdate();
                       
                       HttpSession session = r.getSession();
                       session.setAttribute("successMessage", "Client ajouté.");
                       
                       
                       // Set the HTTPOnly cookie
                        Cookie cookie = new Cookie("sessionId", session.getId());
                        cookie.setHttpOnly(true);
                        s.addCookie(cookie);
                        
                       s.sendRedirect("web_add.jsp?formId=form1");
                       
                   }
               }
           }
           } catch (SQLException e) {
        log.error("An error occurred while processing the request.", e);
        throw new ServletException("An error occurred while processing the request.", e);
        
           } catch (ServletException e) {
    HttpSession session = r.getSession();
    session.setAttribute("errorMessage", e.getMessage());
                      // Set the HTTPOnly cookie
                        Cookie cookie = new Cookie("sessionId", session.getId());
                        cookie.setHttpOnly(true);
                        s.addCookie(cookie);
    s.sendRedirect("web_add.jsp?formId=form1");
    
        } catch (Exception e) {
    HttpSession session = r.getSession();
    session.setAttribute("errorMessage", "Erreur lors de l'ajout de la client. Veuillez réessayer.");
                       // Set the HTTPOnly cookie
                        Cookie cookie = new Cookie("sessionId", session.getId());
                        cookie.setHttpOnly(true);
                        s.addCookie(cookie);
    s.sendRedirect("web_add.jsp?formId=form1");
} }

    
    private void processForm2(HttpServletRequest r, HttpServletResponse s) throws IOException, ServletException {
        try {
            
            
            //Cross-site request forgery (CSRF) 
             String csrfToken = (String) r.getSession().getAttribute("csrfToken");
             String requestCsrfToken = r.getParameter("csrfToken");

          // Validate CSRF token
           if (StringUtils.isBlank(csrfToken) || !csrfToken.equals(requestCsrfToken)) {
          throw new ServletException("Invalid CSRF token.");
           }
            String Ref_articleP = r.getParameter("Ref_article");
            String Nom_articleP = r.getParameter("Nom_article");
            String QuantiteP = r.getParameter("Quantite");
            String Prix_venteP = r.getParameter("Prix_vente");
           
       //Cross-site scripting (XSS) 
        if (!StringUtils.isNumeric(Ref_articleP)) {
                throw new ServletException("Invalid article Ref.");
            }else {
                int Ref_article = Integer.parseInt(Ref_articleP);
                
            if (StringUtils.isNumeric(Nom_articleP)) {
                throw new ServletException("Invalid article name.");
            }else {
                String Nom_article = StringEscapeUtils.escapeHtml4(Nom_articleP);
                
                if (!StringUtils.isNumeric(QuantiteP)) {
                    throw new ServletException("Invalid quantity value.");
                } else {
                    int Quantite = Integer.parseInt(QuantiteP);
                    
                    if (!StringUtils.isNumeric(Prix_venteP)) {
                        throw new ServletException("Invalid selling price value.");
                    } else {
                        double Prix_vente = Double.parseDouble(Prix_venteP);
                        
                        //Les injections SQL
                        if (!isValidArticleRef(Ref_article)) {
                            throw new ServletException("Invalid article Ref.");
                        }
                        if (!isValidArticleName(Nom_article)) {
                            throw new ServletException("Invalid article name.");
                        }
                        
                        if (!isValidQuantity(Quantite)) {
                            throw new ServletException("Invalid quantity value.");
                        }
                        
                        if (!isValidSellingPrice(Prix_vente)) {
                            throw new ServletException("Invalid selling price value.");
                        }
                        
                        
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact", "root", "");
                        PreparedStatement pst = connection.prepareStatement("insert into article (Ref_article,Nom_article,Quantite,Prix_vente) values (?,?,?,?);");
                        
                        pst.setInt(1, Ref_article);
                        pst.setString(2, Nom_article);
                        pst.setInt(3, Quantite);
                        pst.setDouble(4, Prix_vente);
                        pst.executeUpdate();
                        
                        HttpSession session = r.getSession();
                        session.setAttribute("successMessage", "Article ajouté.");
                        
                        // Set the HTTPOnly cookie
                        Cookie cookie = new Cookie("sessionId", session.getId());
                        cookie.setHttpOnly(true);
                        s.addCookie(cookie);
                        
                        s.sendRedirect("web_add.jsp?formId=form2");
                    }  
                    }
                }
            }
            } catch (SQLException e) {
        log.error("An error occurred while processing the request.", e);
        throw new ServletException("An error occurred while processing the request.", e);
        
        } catch (ServletException e) {
    HttpSession session = r.getSession();
    session.setAttribute("errorMessage", e.getMessage());
                          // Set the HTTPOnly cookie
                        Cookie cookie = new Cookie("sessionId", session.getId());
                        cookie.setHttpOnly(true);
                        s.addCookie(cookie);
    s.sendRedirect("web_add.jsp?formId=form2");

        }  catch (Exception e) {
    HttpSession session = r.getSession();
    session.setAttribute("errorMessage", "Erreur lors de l'ajout de l'article. Veuillez réessayer.");
                          // Set the HTTPOnly cookie
                        Cookie cookie = new Cookie("sessionId", session.getId());
                        cookie.setHttpOnly(true);
                        s.addCookie(cookie);
    s.sendRedirect("web_add.jsp?formId=form2");
}
    }

    
    private void processForm3(HttpServletRequest r, HttpServletResponse s) throws IOException, ServletException {
        try {
            
            //Cross-site request forgery (CSRF) 
             String csrfToken = (String) r.getSession().getAttribute("csrfToken");
             String requestCsrfToken = r.getParameter("csrfToken");

         // Validate CSRF token
         if (StringUtils.isBlank(csrfToken) || !csrfToken.equals(requestCsrfToken)) {
         throw new ServletException("Invalid CSRF token.");
         }
           
            String Id_clientP = r.getParameter("Id_client");
            String Date_factureP = r.getParameter("Date_facture");
            String Mode_paiementP = r.getParameter("Mode_paiement");
            
            //Cross-site scripting (XSS) 
            if (!StringUtils.isNumeric(Id_clientP)) {
                throw new ServletException("Invalid client ID.");
            }else {
                int Id_client = Integer.parseInt(r.getParameter("Id_client"));
                
                if (StringUtils.isNumeric(Date_factureP)) {
                    throw new ServletException("Invalid date.");
                } else {
                    String Date_facture = StringEscapeUtils.escapeHtml4(Date_factureP);
                    
                    if (StringUtils.isNumeric(Mode_paiementP)) {
                        throw new ServletException("Invalid mode of payment.");
                    } else {
                        String Mode_paiement = StringEscapeUtils.escapeHtml4(Mode_paiementP);
                        
                        //Les injections SQL
                        if (!isValidClientId(Id_client)) {
                            throw new ServletException("Invalid client ID.");
                        }
                        if (!isValidMode(Mode_paiement)) {
                            throw new ServletException("Invalid mode of payment.");
                        }
                        
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        
                        try {
                            java.util.Date parsedDate = dateFormat.parse(Date_facture);
                            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
                            
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact","root","");
                            PreparedStatement pst = c.prepareStatement("insert into facture(Id_client,Date_facture,Mode_paiement) values (?,?,?);");
                            
                            pst.setInt(1, Id_client);
                            pst.setDate(2,sqlDate);
                            pst.setString(3, Mode_paiement);
                            pst.executeUpdate();
                            
                            PreparedStatement pst2 = c.prepareStatement("SELECT MAX(N_facture) FROM facture;");
                            ResultSet rs = pst2.executeQuery();
                            int N_facture = -1;
                            if (rs.next()) {
                                N_facture = rs.getInt(1);
                            }
                            
                            s.sendRedirect("web_add_facture.jsp?N_facture=" + N_facture);
                            
                            
                        } catch (ParseException e) {
                            throw new ServletException("Invalid date format for Date_facture.");
                        }
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
    s.sendRedirect("web_add.jsp?formId=form3");
    
        }  catch (Exception e) {
    HttpSession session = r.getSession();
    session.setAttribute("errorMessage", "Erreur lors de l'ajout de la facture. Veuillez réessayer.");
                        // Set the HTTPOnly cookie
                        Cookie cookie = new Cookie("sessionId", session.getId());
                        cookie.setHttpOnly(true);
                        s.addCookie(cookie);
    s.sendRedirect("web_add.jsp?formId=form3");
}
        
    }

    @Override
    public void doPost(HttpServletRequest r, HttpServletResponse s) throws IOException , ServletException{
        
            // Set the Content-Security-Policy header
        s.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'");
        
        
        String form = r.getParameter("form");

        if (form != null) {
            switch (form) {
                case "form1":
                    processForm1(r, s);
                    break;
                case "form2":
                    processForm2(r, s);
                    break;
                case "form3":
                    processForm3(r, s);
                    break;
                default:
                    
                    break;
            }
        }
    }
    
    
    //Les injections SQL
    private boolean isValidArticleRef(int RefArticle) {
     String RefArticleString = String.valueOf(RefArticle);
     boolean ver = RefArticleString.length() == 6;
     return ver && RefArticle >= 0;
    }
     private boolean isValidname(String name) { 
    return name != null && !name.isEmpty();
      } 
     
     private boolean isValidPhoneNumber(int phoneNumber) {
     String phoneNumberString = String.valueOf(phoneNumber);
     boolean ver = phoneNumberString.length() == 10;
     return ver && phoneNumber >= 0;
    }
     
     private boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    return email != null && email.matches(emailRegex);
}
     
     private boolean isValidArticleName(String articleName) {
    return articleName != null && !articleName.isEmpty();
   }
     private boolean isValidQuantity(int quantity) {
    return quantity >= 0;  
}
    private boolean isValidSellingPrice(double sellingPrice) {
    return sellingPrice >= 0.0; 
} 
     
     private boolean isValidClientId(int clientId) {
      return clientId > 0;
      }
     
     private boolean isValidMode(String mode) {
    return mode != null && (mode.equalsIgnoreCase("Cash") || mode.equalsIgnoreCase("Credit"));
     }
     
     
     
     
     
}

