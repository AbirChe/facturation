<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<html>
    <head>
       
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
        input[type="date"],
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

        p.error-message {
            color: #ff0000;
            margin-bottom: 10px;
        }

        p.success-message {
            color: #008000;
            margin-bottom: 10px;
        }
        
        .payment-mode-options {
        display: flex;
        flex-direction: row;
    }

    .payment-mode-options label {
        margin-right: 10px;
        margin-left: 15;
        
    }
    </style>
    </head>
    
    <body>
        
         <%
               String formId = request.getParameter("formId");
         %>
         
         <% String errorMessage = (String) session.getAttribute("errorMessage"); %>
<% session.removeAttribute("errorMessage"); %>
<% if (errorMessage != null) { %>
    <p>Erreur: <%= errorMessage %></p><br>
    
    
    
<% } %> 
<% String successMessage = (String) session.getAttribute("successMessage"); %>
<% session.removeAttribute("successMessage"); %>
<% if (successMessage != null) { %>
    <p><%= successMessage %></p><br>
<% } %>

<%
  // Generate CSRF token and store it in the session
  String csrfToken = (String) session.getAttribute("csrfToken");
  if (csrfToken == null) {
    csrfToken = RandomStringUtils.randomAlphanumeric(32);
    session.setAttribute("csrfToken", csrfToken);
  }
%>
        
        <form id="form1" style="<%= formId.equals("form1") ? "" : "display: none;" %>" action="ajouter" method="post">
         <h1>Ajouter Client</h1>
            <input type="hidden" name ="form" value="form1">
            
            <label for="fname1">Client Name:</label><br>
            <input type="text"  id="fname1 "name="Nom_client" required ><br>
            <label for="fname2">Telephne number:</label><br>
            <input type="text" id="fname2" name="Telephone" required ><br>
            <label for="fname3">Email:</label><br>
            <input type="text" id="fname3" name="Email" required ><br><br>
        
            <input type="hidden" name="csrfToken" value="<%= csrfToken %>" required>
            
            <input type="submit" value="Submit">
        </form>
        
        
        
        <form id="form2" style="<%= formId.equals("form2") ? "" : "display: none;" %>" action="ajouter" method="post">
            <h1>Ajouter Article</h1>
            <input type="hidden" name ="form" value="form2">
            
            <label for="fname">Article ref:</label><br>
            <input type="text"  name="Ref_article" required ><br>
            <label for="fname">Article Name:</label><br>
            <input type="text"  name="Nom_article" required ><br>
            <label for="fname">Article Quantite:</label><br>
            <input type="text"  name="Quantite" required ><br>
            <label for="fname">Article Prix:</label><br>
            <input type="text"  name="Prix_vente" required ><br><br>
            
             <input type="hidden" name="csrfToken" value="<%= csrfToken %>" required>
            
            <input type="submit" value="Submit">
        </form>
        
        
        <form id="form3" style="<%= formId.equals("form3") ? "" : "display: none;" %>" action="ajouter" method="post">
            <h1>Ajouter Facture</h1>
            <input type="hidden" name ="form" value="form3">
            
            <label for="fname">Client ID:</label><br>
            <input type="text"  name="Id_client" required ><br><br>
            <label for="fname">Date:</label><br>
            <input type="date"  name="Date_facture" id= "date_facture" required ><br><br>
            <label for="fname">Mode Paiement:</label><br>
            
            <div class="payment-mode-options">
           <label for="mode_paiement_cash">Cash</label>
           <input type="radio" name="Mode_paiement" id="mode_paiement_cash" value="Cash" required >
           <label for="mode_paiement_credit">Credit</label>
           <input type="radio" name="Mode_paiement" id="mode_paiement_credit" value="Credit" required ><br><br>
            </div>
            
            
            
            <input type="hidden" name="csrfToken" value="<%= csrfToken %>" required>
           <br><br>
            <input type="submit" value="Submit">
        </form>
      
        
     
    </body>
</html>