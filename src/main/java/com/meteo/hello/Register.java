package com.meteo.hello;

import com.meteo.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public Register() {
        super();
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (userDAO.registerUser(username, email, password)) {
            response.sendRedirect("Login.jsp?message=Inscription réussie, veuillez vous connecter");
        } else {
            request.setAttribute("errorMessage", "Erreur lors de l'inscription, réessayez.");
            request.getRequestDispatcher("/Register.jsp").forward(request, response);
        }
    }
}
