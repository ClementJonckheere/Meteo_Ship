package com.meteo.hello;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/removeFavorite")
public class RemoveFavorite extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isConnected") == null || !(Boolean) session.getAttribute("isConnected")) {
            response.getWriter().write("⚠️ Vous devez être connecté pour supprimer un favori.");
            return;
        }

        String city = request.getParameter("city");
        if (city == null || city.trim().isEmpty()) {
            response.getWriter().write("⚠️ Ville invalide.");
            return;
        }

        List<String> favoris = (List<String>) session.getAttribute("favoris");
        if (favoris != null && favoris.remove(city)) {
            session.setAttribute("favoris", favoris);
            response.getWriter().write("✅ " + city + " supprimé des favoris !");
        } else {
            response.getWriter().write("⚠️ Cette ville n'est pas dans vos favoris.");
        }
    }
}
