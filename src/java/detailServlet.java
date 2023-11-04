import com.mysql.jdbc.Driver;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.mysql.jdbc.Driver;

@WebServlet(urlPatterns = "/detail")
public class detailServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
          
        
        try {
            int factureId = Integer.parseInt(request.getParameter("factureId"));
            displayLigneFactureContent(factureId, request, response);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

  private void displayLigneFactureContent(int factureId,HttpServletRequest request,  HttpServletResponse response) throws IOException, ServletException {
    try {
        request.setAttribute("factureId", factureId);
        RequestDispatcher dispatcher = request.getRequestDispatcher("detail_facture.jsp");
        dispatcher.forward(request, response);
    } catch (Exception e) {
        System.out.print(e);
    }
}

}
