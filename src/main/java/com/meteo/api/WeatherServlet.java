package com.meteo.api;

import com.meteo.model.Favori;
import com.meteo.model.WeatherData;
import com.meteo.service.FavorisService;
import com.meteo.service.WeatherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/home")
public class WeatherServlet extends HttpServlet {
   
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        String city = request.getParameter("city");
        String lat = request.getParameter("lat");
        String lon = request.getParameter("lon");

        WeatherData weatherData = null;

        if (city != null && !city.isEmpty()) {
            weatherData = WeatherService.getWeatherByCity(city);
        } else if (lat != null && lon != null) {
            weatherData = WeatherService.getWeatherByCoordinates(Double.parseDouble(lat), Double.parseDouble(lon));
        }

        request.setAttribute("weather", weatherData);

        List<Favori> favoris = new ArrayList<>();
        List<WeatherData> favorisMeteo = new ArrayList<>();

        if (userId != null) {
            // 🔹 Récupération des favoris depuis la base de données
            favoris = FavorisService.getFavoris(userId);

            // 🔹 Récupération des données météo pour chaque favori
            for (Favori favori : favoris) {
                WeatherData meteo = WeatherService.getWeatherByCity(favori.getCity());
                if (meteo != null) {
                    favorisMeteo.add(meteo);
                }
            }

            // 🔹 On envoie les listes à la JSP
            request.setAttribute("favoris", favoris);
            request.setAttribute("favorisMeteo", favorisMeteo);

            System.out.println("📌 Favoris récupérés : " + favoris.size());
            System.out.println("📌 Données météo des favoris récupérées : " + favorisMeteo.size());
        }

        request.getRequestDispatcher("Home.jsp").forward(request, response);
    }

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    HttpSession session = request.getSession(false);
		    Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

		    String city = request.getParameter("city");
		    String country = request.getParameter("country");
		    String deleteCity = request.getParameter("deleteCity"); 

		    System.out.println("📌 Récupération des données pour ajout en favori");
		    System.out.println("   → userId: " + userId);
		    System.out.println("   → city: " + city);
		    System.out.println("   → country: " + country);

		    if (userId != null) {
		        if (deleteCity != null) { // 🔴 Suppression d'un favori
		            System.out.println("📌 Suppression du favori : " + deleteCity);
		            FavorisService.supprimerFavori(userId, deleteCity);
		            System.out.println("✅ Favori supprimé !");
		        } else if (city != null && !city.isEmpty()) { // 🟢 Ajout d'un favori
		            System.out.println("📌 Ajout d'un favori : " + city);
		            FavorisService.ajouterFavori(userId, city, country);
		            System.out.println("✅ Favori ajouté !");
		        }
		    } else {
		        System.out.println("❌ Erreur: Impossible de modifier les favoris (user non connecté).");
		    }

		    response.sendRedirect("home");
		}
}
