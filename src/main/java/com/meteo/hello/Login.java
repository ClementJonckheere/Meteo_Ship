package com.meteo.hello;

import com.meteo.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public Login() {
        super();
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("txtLogin");
        String password = request.getParameter("txtPassword");

        // creation de la session des users
        HttpSession session = request.getSession(true);
        session.setAttribute("login", login);

        // vérification des users
        if (userDAO.validateUser(login, password)) {
            int userId = userDAO.getUserId(login);  // Ajouter cette méthode dans `UserDAO`
            session.setAttribute("userId", userId); // Stocker l'ID utilisateur en session
            session.setAttribute("isConnected", true);
            response.sendRedirect("home");
        } else {
            session.setAttribute("isConnected", false);
            request.setAttribute("errorMessage", "Identifiants incorrects.");
            request.getRequestDispatcher("/Login.jsp").forward(request, response);
        }
    }


}
