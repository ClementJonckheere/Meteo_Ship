<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscription</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/login.css">
</head>
<body>
    <div class="main-container">
    	<div class="container">
        <h1>Inscription</h1>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
        <% } %>
        <form method='post' action='register'>
            <label for='username'>Nom d'utilisateur :</label>
            <input name='username' type='text' required/>

            <label for='email'>Email :</label>
            <input name='email' type='email' required/>

            <label for='password'>Mot de passe :</label>
            <input name='password' type='password' required/>

            <input type="submit" value="S'inscrire">
        </form>
        <a href="Login.jsp">Déjà inscrit ? Connectez-vous ici</a>
        </div>
    </div>
</body>
</html>
