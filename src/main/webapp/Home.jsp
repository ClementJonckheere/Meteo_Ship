<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meteo.model.WeatherData, com.meteo.model.Favori, java.util.List" %>
<%@ page import="jakarta.servlet.http.HttpSession, java.util.List, java.util.ArrayList" %>
<%@ page import="java.util.List, com.meteo.model.Favori" %>

<%
    HttpSession userSession = request.getSession(false);
    String login = (userSession != null && userSession.getAttribute("login") != null) ? (String) userSession.getAttribute("login") : "";
    Integer userId = (userSession != null) ? (Integer) userSession.getAttribute("userId") : null;
    boolean isConnected = (userSession != null && userId != null);

    WeatherData weather = (WeatherData) request.getAttribute("weather");
    List<Favori> favoris = (List<Favori>) request.getAttribute("favoris");
    List<WeatherData> favorisMeteo = (List<WeatherData>) request.getAttribute("favorisMeteo");

    System.out.println("📌 userId en session = " + userId);
    System.out.println("📌 Login en session = " + login);
    System.out.println("📌 isConnected = " + isConnected);
%>


<script>
document.addEventListener("DOMContentLoaded", function () {
    function fetchWeather(lat, lon) {
        console.log("📌 Récupération de la météo pour la position : ", lat, lon);
        
        fetch("home?lat=" + lat + "&lon=" + lon)
            .then(response => response.text())
            .then(html => {
                let parser = new DOMParser();
                let doc = parser.parseFromString(html, "text/html");
                let weatherInfo = doc.querySelector("#weather-info");
                
                if (weatherInfo) {
                    document.querySelector("#weather-info").innerHTML = weatherInfo.innerHTML;
                }
            })
            .catch(error => console.error("❌ Erreur lors de la récupération de la météo :", error));
    }

    function checkAndUpdateWeather() {
        let storedCity = sessionStorage.getItem("searched_city"); // 🔹 Vérifie si une ville a été recherchée
        let storedLat = localStorage.getItem("user_lat");
        let storedLon = localStorage.getItem("user_lon");

        if (storedCity) {
            console.log(`📌 Recherche détectée : ${storedCity}, on ne met PAS à jour avec la géolocalisation.`);
            return; // ⚠ Si une ville a été recherchée, on ne met pas à jour avec la géolocalisation
        }

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                function (position) {
                    let lat = position.coords.latitude;
                    let lon = position.coords.longitude;

                    try {
                        if (!storedLat || !storedLon || storedLat != lat || storedLon != lon) {
                            localStorage.setItem("user_lat", lat);
                            localStorage.setItem("user_lon", lon);
                        }

                        // 🛑 Si aucune recherche manuelle, on met à jour la météo
                        fetchWeather(lat, lon);

                    } catch (e) {
                        console.error("🚨 Erreur d'accès au stockage local:", e.message);
                    }
                },
                function (error) {
                    console.error("❌ Erreur de géolocalisation:", error.message);
                    fetchWeather(48.8566, 2.3522); // Paris par défaut
                }
            );
        } else {
            console.log("⚠️ La géolocalisation n'est pas supportée.");
            fetchWeather(48.8566, 2.3522); // Paris par défaut
        }
    }

    checkAndUpdateWeather(); // ✅ Lance la mise à jour SEULEMENT si aucune ville n'a été recherchée
});

</script>




<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Météo en temps réel</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/home.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/header.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/cart.css">    
    <script src="/js/bootstrap.bundle.min.js?t=1738759901"></script>
  	<script src="https://cdn.maptiler.com/maptiler-sdk-js/v2.5.1/maptiler-sdk.umd.min.js"></script>
  	<link href="https://cdn.maptiler.com/maptiler-sdk-js/v2.5.1/maptiler-sdk.css" rel="stylesheet" />
  	<script src="https://cdn.maptiler.com/maptiler-weather/v2.2.0/maptiler-weather.umd.min.js"></script>
</head>
<body>
<header class="header-outer">
	<div class="header-inner responsive-wrapper">
		<div class="header-logo">
			 <strong>Meteo Ship</strong>
		</div>
		<nav class="header-navigation">
            <% if (isConnected) { %>
                <form action="logout" method="post" style="display: inline;">
                   <button type="submit" class="button button--animated">
                		<span class="button__text">Deconnexion</span>
                		<span class="button__icon">→</span>
            		</button>
                </form>
            <% } else { %>
                <button class="button button--animated">
                	<a href="Login.jsp" class="link-connexion"><span class="button__text">Connexion</span></a>
                	<span class="button__icon">→</span>
                </button>
            <% } %>
		</nav>
	</div>
</header>

<section class="content-section">
    <h1>Météo en temps réel</h1>
    <form action="home" method="get" class="search-header" onsubmit="storeSearchCity()">
        <input type="text" name="city" id="search-city" placeholder="Rechercher une ville..." required>
        <button type="submit">Rechercher</button>
    </form>
</section>

<div id="weather-info">
        <% if (weather != null) { %>
    <div class="weather-container">
             <% if (isConnected) { %>
            <form action="home" method="post" class="favoris-btn">
                <input type="hidden" name="city" value="<%= weather.getCity() %>">
                <input type="hidden" name="country" value="<%= weather.getCountry() %>">
                <button type="submit" class="squishy squishy-classic">
                </button>
            </form>
        <% } %>
        <h2>🌍 Météo aujourd'hui à <%= weather.getCity() %>, <%= weather.getCountry() %></h2>
        <div class="container_info">
        	<div class="temperature-info">
            	<%= weather.getTemperature() %>°C
            	<div class="temp-res">
        			<p>Ressenti : <%= weather.getFeelsLike() %>°C </p>
        		</div>
        	</div>

        	<div class="temp-range">
            	Max : <%= weather.getTempMax() %>° / Min : <%= weather.getTempMin() %>°
        	</div>
		</div>
		
        <div class="weather-details">
            <div class="weather-item">Humidité : <%= weather.getHumidity() %>%</div>
            <div class="weather-item">Vent : <%= weather.getWindSpeed() %> km/h</div>
            <div class="weather-item">Pression : <%= weather.getPressure() %> mb</div>
            <div class="weather-item">Visibilité : <%= weather.getVisibility() / 1000.0 %> km</div>
        </div>

        <div class="sun-info">
            <p>🌅 Lever du soleil : <%= weather.getSunrise() %> </p>
            <p>🌇 Coucher du soleil : <%= weather.getSunset() %> </p>
        </div>
       
    </div>
    <% } else { %>
    <div class="chargment-content">
    </div>
    <% } %>
<% if (isConnected && favorisMeteo != null && !favorisMeteo.isEmpty()) { %>
    <section class="favoris-section">
        <h2>Vos villes favorites</h2>
        <ul class="favoris-list">
            <% for (WeatherData meteo : favorisMeteo) { %>
                <li>
                    <p class="city-name"><%= meteo.getCity() %>, <%= meteo.getCountry() %></p>
                    <p class="temp">Température actuelle : <%= meteo.getTemperature() %>°C</p>
                    <p class="temp">Température ressentie : <%= meteo.getFeelsLike() %>°C</p>
                    <p class="max-min">Max : <%= meteo.getTempMax() %>°C / Min : <%= meteo.getTempMin() %>°C</p>
                    
                    <form class="form-delete" action="home" method="post" onsubmit="return confirmDelete()">
                    	<input type="hidden" name="deleteCity" value="<%= meteo.getCity() %>">
                    	<button type="submit" class="delete-fav-btn">🗑 Supprimer</button>
                	</form>
                </li>
            <% } %>
        </ul>
    </section>
<% } else { %>
    <p>Aucun favori enregistré.</p>
<% } %>

    
</div>

<section class="cart-section">
<%@ include file="WeatherCart.jsp" %>
</section>


<script>
    function confirmDelete() {
        return confirm("❗ Voulez-vous vraiment supprimer ce favori ?");
    }
    
    function storeSearchCity() {
        let city = document.getElementById("search-city").value;
        sessionStorage.setItem("searched_city", city);
    }
</script>



</body>
</html>
