<%@page import="java.sql.ResultSet"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
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

        table {
            width: 100%;
            border-collapse: collapse;
        }

        table th,
        table td {
            padding: 10px;
            border: 1px solid #006666;
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

        a {
            color: #660000;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        .confirmation-message {
            color: #008000;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .no-data {
            color: #006666;
            text-align: center;
        }
    </style>
    <script>
        function confirmDelete(id, table) {
            var confirmMessage = "Are you sure you want to delete this " + table + "?";
            var deleteUrl = "delete?id=" + id + "&table=" + table;
            var redirectUrl = "afficher?content=" + table;

            var result = confirm(confirmMessage);
            if (result) {
                window.location.href = deleteUrl;
            } else {
                window.location.href = redirectUrl;
            }
        }
    </script>
</head>
<body>
    <h1>Liste des <%= request.getParameter("content") %></h1>

    <% if (request.getAttribute("clientData") != null) { %>
        <table border="1">
            <thead>
                <tr>
                    <th>Client ID</th>
                    <th>Nom client</th>
                    <th>Téléphone</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% while (((ResultSet)request.getAttribute("clientData")).next()) { %>
                    <tr>
                        <td><%= ((ResultSet)request.getAttribute("clientData")).getString("Id_client") %></td>
                        <td><%= ((ResultSet)request.getAttribute("clientData")).getString("Nom_client") %></td>
                        <td><%= ((ResultSet)request.getAttribute("clientData")).getString("Telephone") %></td>
                        <td><%= ((ResultSet)request.getAttribute("clientData")).getString("Email") %></td>
                        <td>
                            <a href="javascript:confirmDelete('<%= ((ResultSet)request.getAttribute("clientData")).getInt("Id_client") %>', 'client')">Supprimer</a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else if (request.getAttribute("articleData") != null) { %>
        <table border="1">
            <thead>
                <tr>
                    <th>Article ID</th>
                    <th>Nom article</th>
                    <th>Quantité</th>
                    <th>Prix vente</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% while (((ResultSet)request.getAttribute("articleData")).next()) { %>
                    <tr>
                        <td><%= ((ResultSet)request.getAttribute("articleData")).getString("Ref_article") %></td>
                        <td><%= ((ResultSet)request.getAttribute("articleData")).getString("Nom_article") %></td>
                        <td><%= ((ResultSet)request.getAttribute("articleData")).getString("Quantite") %></td>
                        <td><%= ((ResultSet)request.getAttribute("articleData")).getString("Prix_vente") %></td>
                        <td>
                            <a href="javascript:confirmDelete('<%= ((ResultSet)request.getAttribute("articleData")).getInt("Ref_article") %>', 'article')">Supprimer</a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else if (request.getAttribute("factureData") != null) { %>
        <table border="1">
            <thead>
                <tr>
                    <th>Facture ID</th>
                    <th>Date facture</th>
                    <th>Mode paiement</th>
                    
                    <th>Client Name</th>
                    <th>Action</th>
                    <th>delete</th>
                </tr>
            </thead>
            <tbody>
                <% while (((ResultSet)request.getAttribute("factureData")).next()) { %>
                    <tr>
                        <td><%= ((ResultSet)request.getAttribute("factureData")).getString("N_facture") %></td>
                        <td><%= ((ResultSet)request.getAttribute("factureData")).getString("Date_facture") %></td>
                        <td><%= ((ResultSet)request.getAttribute("factureData")).getString("Mode_paiement") %></td>
                        
                        <td><%= ((ResultSet)request.getAttribute("factureData")).getString("Nom_client") %></td>
                        <td>
                            <a href="detail?factureId=<%= ((ResultSet)request.getAttribute("factureData")).getInt("N_facture") %>">Détail</a> </td>
                            <td> <a href="javascript:confirmDelete('<%= ((ResultSet)request.getAttribute("factureData")).getInt("N_facture") %>', 'facture')">Supprimer</a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else { %>
        <h2>No data available</h2>
    <% } %>
</body>
</html>
