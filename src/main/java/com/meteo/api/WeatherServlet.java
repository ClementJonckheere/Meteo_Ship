package com.meteo.api;

import com.meteo.model.Favori;
import com.meteo.model.FavoriWidget; // Ajout de l'import manquant
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
import java.util.*;

@WebServlet("/home")
public class WeatherServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        String city = request.getParameter("city");
        String lat = request.getParameter("lat");
        String lon = request.getParameter("lon");

        WeatherData weatherData = null;

        // Récupérer la météo en fonction des paramètres
        if (city != null && !city.isEmpty()) {
            weatherData = WeatherService.getWeatherByCity(city);
        } else if (lat != null && lon != null) {
            weatherData = WeatherService.getWeatherByCoordinates(Double.parseDouble(lat), Double.parseDouble(lon));
        }

        request.setAttribute("weather", weatherData);

        List<Favori> favoris = new ArrayList<>();
        List<WeatherData> favorisMeteo = new ArrayList<>();
        Map<String, FavoriWidget> widgetPreferences = new HashMap<>();

        // Si l'utilisateur est connecté, récupérer ses favoris
        if (userId != null) {
            favoris = FavorisService.getFavoris(userId);
            
            for (Favori favori : favoris) {
                // Récupération des préférences de widgets pour chaque favori
                FavoriWidget widget = FavorisService.getWidgetPreferences(userId, favori.getCity());
                widgetPreferences.put(favori.getCity(), widget);

                // Récupération des données météo pour chaque favori
                WeatherData meteo = WeatherService.getWeatherByCity(favori.getCity());
                if (meteo != null) {
                    favorisMeteo.add(meteo);
                }
            }

            request.setAttribute("widgetPreferences", widgetPreferences);
            request.setAttribute("favoris", favoris);
            request.setAttribute("favorisMeteo", favorisMeteo);
        }

        request.getRequestDispatcher("Home.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String deleteCity = request.getParameter("deleteCity");

        if (userId != null) {
            if (deleteCity != null) {
                System.out.println("Suppression du favori : " + deleteCity);
                FavorisService.supprimerFavori(userId, deleteCity);
            } else if (city != null && !city.isEmpty()) {
                System.out.println("Ajout d'un favori : " + city);
                FavorisService.ajouterFavori(userId, city, country);
            }
        }

        // Gérer la mise à jour des préférences utilisateur
        if (request.getParameter("updatePreferences") != null) {
            boolean showTemp = request.getParameter("showTemp") != null;
            boolean showHumidity = request.getParameter("showHumidity") != null;
            boolean showWind = request.getParameter("showWind") != null;

            FavorisService.updateWidgetPreferences(userId, city, showTemp, showHumidity, showWind);
        }

        response.sendRedirect("home");
    }
}
