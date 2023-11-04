
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>accueil</title>
        <style>
            body {
                display: flex;
                height: 100vh;
                margin: 0;
            }
            
            .left {
                background: linear-gradient(to right, #006666,#006666);
                width: 15%;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
            }
            
            .right {
                background-color: white;
                width: 85%;
                display: flex;
                flex-direction: column;
            }
            
            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 10px;
                background-color: #cccccc;
            }
            
            .aa {
                display: flex;
                margin-left: 40px;
                marker-attachment: 30px;
            }
            
            .b {
                margin-left: 20px;
            }
            
            .affichage {
                flex-grow: 1;
                padding: 20px;
            }
            
            .cool-link {
                color: white ;
                text-decoration: none;
                padding: 5px 10px;
                margin-right: 10px;
                background-color: #333;
                border-radius: 3px;
                font-family: Arial;
            }
            
            a {
                margin-bottom: 20px;
                font-size: 20px;
            }
            
            /* Modify the link styles */
            .cool-link:hover {
                background-color: grey;
                color: whitesmoke;
            }
            
           
        </style>
    </head>
    <body>
         <% 
        HttpSession s= request.getSession(false);
        String user= (String)s.getAttribute("user");
        if(user!=null){
          %>
         
       <div class="left">
        <a href="web_add.jsp?formId=form1" target="iframe_a" class="cool-link">ajouter client</a>
        <a href="web_add.jsp?formId=form2" target="iframe_a" class="cool-link">ajouter article</a>
        <a href="web_add.jsp?formId=form3" target="iframe_a" class="cool-link">ajouter facture</a>
    </div>
    <div class="right">
        <div class="header">
            <div class="aa">
               <a href="afficher?content=client" target="iframe_a" class="cool-link">client</a>
               <a href="afficher?content=article" target="iframe_a" class="cool-link">article</a>
               <a href="afficher?content=facture" target="iframe_a" class="cool-link">facture</a>

            </div>
            <div class="b">
                <a href="logout"  class="cool-link">se deconnecter</a>
            </div>
        </div>
        <div class="affichage">
           <iframe  name="iframe_a" height="100%" width="100%" title="Iframe example"></iframe>
        </div>
    </div>
         <% }
        else{ response.sendRedirect("index.jsp");}
        %>
    </body>
</html>
