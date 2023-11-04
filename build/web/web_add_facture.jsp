


<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            background-color: #f5f5f5;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
        }

        h1 {
            color: #006666;
            text-align: center;
        }

        p {
            color: #ff0000;
            text-align: center;
        }

        form {
            background-color: #ffffff;
            border: 1px solid #dddddd;
            border-radius: 4px;
            padding: 20px;
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            color: #006666;
        }

        input[type="text"],
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #dddddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #006666;
            color: #ffffff;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        table th,
        table td {
            padding: 10px;
            border: 1px solid #dddddd;
            text-align: left;
        }

        table th {
            background-color: #f5f5f5;
            color: #006666cccc;
            font-weight: bold;
        }

        table tbody tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
    <h1>Facture N°<%=request.getParameter("N_facture") %></h1><br>
    
    
    <% String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) { %>
        <p><%= errorMessage %></p><br>
    <% } %>
    
    <%
  // Generate CSRF token and store it in the session
  String csrfToken = (String) session.getAttribute("csrfToken");
  if (csrfToken == null) {
    csrfToken = RandomStringUtils.randomAlphanumeric(32);
    session.setAttribute("csrfToken", csrfToken);
  }
%>
    
    <form id="form4" action="add_ligne" method="post">
        
       <input type="hidden" name="N_facture" value="<%= request.getParameter("N_facture") %>" required >
        
        <label for="ref_article">Ref Article:</label><br>
        <input type="text" name="Ref_article" id="ref_article" required><br><br>
        
        <label for="quantite">Quantité vendue:</label><br>
        <input type="text" name="Quantite_vendue" id="quantite" required ><br><br>
        <input type="hidden" name="csrfToken" value="<%= csrfToken %>" required>
        <input type="submit" value="submit"><br><br>
    </form>
    
    <table border='1'>
        <thead>
            <tr>
                 <th>N° Facture</th>
                <th> Article ID</th>
                <th>Quantité vendue</th>
                <th>Prix vente</th>
                <th>Prix total</th>
            </tr>
        </thead>
        <tbody>
            <% 
                // Retrieve and display the ligne_facture data from the database
                try {
                    int nFacture = Integer.parseInt(request.getParameter("N_facture"));
                    
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact", "root", "");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT lf.Ref_article, lf.N_facture, lf.Quantite_vendue, a.Prix_vente, (lf.Quantite_vendue * a.Prix_vente) AS Prix_total FROM ligne_facture lf INNER JOIN article a ON lf.Ref_article = a.Ref_article WHERE lf.N_facture = ?");
                    preparedStatement.setInt(1, nFacture);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                    while (resultSet.next()) {
                        int refArticle = resultSet.getInt("Ref_article");
                        int nFactureValue = resultSet.getInt("N_facture");
                        int quantiteVendue = resultSet.getInt("Quantite_vendue");
                        double prixVente = resultSet.getDouble("Prix_vente");
                        double prixTotal = resultSet.getDouble("Prix_total");
                        
                        out.println("<tr>");
                        out.println("<td>" + nFactureValue + "</td>");
                        out.println("<td>" + refArticle + "</td>");
                        out.println("<td>" + quantiteVendue + "</td>");
                        out.println("<td>" + prixVente + "</td>");
                        out.println("<td>" + prixTotal + "</td>");
                        out.println("</tr>");
                    }
                    
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            %>
        </tbody>
    </table>
    
   
</body>
</html>
