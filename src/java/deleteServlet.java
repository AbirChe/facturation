import com.mysql.jdbc.Driver;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns = "/delete")
public class deleteServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
            // Set the Content-Security-Policy header
        response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'");
        
        int id = Integer.parseInt(request.getParameter("id"));
        String table = request.getParameter("table");

        try {
            switch (table) {
                case "client":
                    deleteClient(id, response);
                    break;
                case "article":
                    deleteArticle(id, response);
                    break;
                case "facture":
                    deleteFacture(id, response);
                    break;
                default:
                    response.sendRedirect("afficher?content=" + table);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteClient(int id, HttpServletResponse response) throws IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact", "root", "");

            // Delete dependent records from Ligne_facture table
            PreparedStatement deleteLigneFacture = connection.prepareStatement("DELETE FROM Ligne_facture WHERE N_facture IN (SELECT N_facture FROM Facture WHERE Id_client = ?)");
            deleteLigneFacture.setInt(1, id);
            deleteLigneFacture.executeUpdate();
            deleteLigneFacture.close();

            // Delete records from Facture table
            PreparedStatement deleteFacture = connection.prepareStatement("DELETE FROM Facture WHERE Id_client = ?");
            deleteFacture.setInt(1, id);
            deleteFacture.executeUpdate();
            deleteFacture.close();

            // Delete record from Client table
            PreparedStatement deleteClient = connection.prepareStatement("DELETE FROM Client WHERE Id_client = ?");
            deleteClient.setInt(1, id);
            deleteClient.executeUpdate();
            deleteClient.close();

            connection.close();

            response.sendRedirect("afficher?content=client");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteArticle(int id, HttpServletResponse response) throws IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact", "root", "");

            // Delete records from Ligne_facture table
            PreparedStatement deleteLigneFacture = connection.prepareStatement("DELETE FROM Ligne_facture WHERE Ref_article = ?");
            deleteLigneFacture.setInt(1, id);
            deleteLigneFacture.executeUpdate();
            deleteLigneFacture.close();

            // Delete record from Article table
            PreparedStatement deleteArticle = connection.prepareStatement("DELETE FROM Article WHERE Ref_article = ?");
            deleteArticle.setInt(1, id);
            deleteArticle.executeUpdate();
            deleteArticle.close();

            connection.close();

            response.sendRedirect("afficher?content=article");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteFacture(int id, HttpServletResponse response) throws IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact", "root", "");

            // Delete records from Ligne_facture table
            PreparedStatement deleteLigneFacture = connection.prepareStatement("DELETE FROM Ligne_facture WHERE N_facture = ?");
            deleteLigneFacture.setInt(1, id);
            deleteLigneFacture.executeUpdate();
            deleteLigneFacture.close();

            // Delete record from Facture table
            PreparedStatement deleteFacture = connection.prepareStatement("DELETE FROM Facture WHERE N_facture = ?");
            deleteFacture.setInt(1, id);
            deleteFacture.executeUpdate();
            deleteFacture.close();

            connection.close();

            response.sendRedirect("afficher?content=facture");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
