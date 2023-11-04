
<%@ page import="org.apache.commons.lang3.RandomStringUtils" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            padding: 20px;
        }

        .container {
            max-width: 400px;
            margin: 0 auto;
            background-color: #ffffff;
            border: 1px solid #ddd;
            padding: 20px;
            margin-top: 10%;
        }

        h1 {
            text-align: center;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #006666;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-left: 40%;
        }

        .message {
            margin-bottom: 10px;
            text-align: center;
            color: #ff0000;
        }
    </style>
    </head>
    <body>
        
        <div class="container">
    <h1>Login Form</h1>
        <% if(request.getAttribute("msg")!=null) 
                out.print(request.getAttribute("msg"));%><br><br>
                
                <%
  // Generate CSRF token and store it in the session
  String csrfToken = (String) session.getAttribute("csrfToken");
  if (csrfToken == null) {
    csrfToken = RandomStringUtils.randomAlphanumeric(32);
    session.setAttribute("csrfToken", csrfToken);
  }
%>
                
        <form method="post" action="login">
            <label for="fname">User Name:</label><br>
            <input type="text" name="user"><br>
            <label for="fname">Password:</label><br>
            <input type="password" name="pwd"><br><br>
            
            <input type="hidden" name="csrfToken" value="<%= csrfToken %>" required>
            
            <input type="submit" value="Submit"><br>
         </form>   
            
            </div>
        
    </body>
</html>
