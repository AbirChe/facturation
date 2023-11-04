<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="com.mysql.jdbc.Driver" %>

<html>
<head>
    <title>Detail Facture</title>
    
    <style>
        body {
            background-color: #f5f5f5;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
        }

        h2 {
            color: #006666;
            text-align: center;
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
            color: #006666;
            font-weight: bold;
        }

        table tbody tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        
    </style>
    
</head>
<body>
    <h2>More Detail about facture N ${param.factureId}: </h2>
    <br>
    <table border="1">
        <thead>
            <tr>
                <th>Article ID</th>
                <th>Quantite vendue</th>
                <th>Prix vente</th>
                <th>Prix total</th>
            </tr>
        </thead>
        <tbody>
            <% try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection c = DriverManager.getConnection("jdbc:mysql://localhost/bd_fact", "root", "");
                PreparedStatement pst = c.prepareStatement("SELECT lf.*, a.Prix_vente, (lf.Quantite_vendue * a.Prix_vente) AS Prix_total FROM ligne_facture lf INNER JOIN article a ON lf.Ref_article = a.Ref_article WHERE lf.N_facture = ?;");
                pst.setInt(1, Integer.parseInt(request.getParameter("factureId")));
                ResultSet rs = pst.executeQuery();
                while (rs.next()) { %>
                    <tr>
                        <td><%= rs.getString("Ref_article") %></td>
                        <td><%= rs.getString("Quantite_vendue") %></td>
                        <td><%= rs.getString("Prix_vente") %></td>
                        <td><%= rs.getString("Prix_total") %></td>
                    </tr>
                <% }
            } catch (Exception e) {
                System.out.print(e);
            } %>
        </tbody>
    </table>
</body>
</html>
