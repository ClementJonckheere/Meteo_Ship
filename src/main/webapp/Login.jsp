<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Page de Connexion</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/login.css">
</head>
<body>
    <div class="main-container">
        <header>
            <h1 class="site-title">Meteo Ship</h1>
        </header>
        
        <div class="container">
            <h1>Connexion</h1>
            <form method='post' action='login'>
                <label for='txtLogin'>Login :</label>
                <input name='txtLogin' type='text' value='<%= (session.getAttribute("login") != null) ? session.getAttribute("login") : "" %>' autofocus required/>

                <label for='txtPassword'>Password :</label>
                <input name='txtPassword' type='password' required/>

                <input type="submit" value="Se connecter">
            </form>
            <a href="Register.jsp">Pas de compte ? Inscris-toi !</a>
        </div>
    </div>
<div id="contentCloud"></div>

</body>
</html>


