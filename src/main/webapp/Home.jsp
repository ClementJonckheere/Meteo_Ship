<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meteo.model.WeatherData, com.meteo.model.Favori, java.util.List" %>
<%@ page import="jakarta.servlet.http.HttpSession, java.util.List, java.util.ArrayList" %>
<%@ page import="java.util.List, com.meteo.model.Favori" %>
<%@ page import="com.meteo.model.FavoriWidget, java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map, com.meteo.model.FavoriWidget" %>




<%
    HttpSession userSession = request.getSession(false);
    String login = (userSession != null && userSession.getAttribute("login") != null) ? (String) userSession.getAttribute("login") : "";
    Integer userId = (userSession != null) ? (Integer) userSession.getAttribute("userId") : null;
    boolean isConnected = (userSession != null && userId != null);

    WeatherData weather = (WeatherData) request.getAttribute("weather");
    List<Favori> favoris = (List<Favori>) request.getAttribute("favoris");
    List<WeatherData> favorisMeteo = (List<WeatherData>) request.getAttribute("favorisMeteo");


    System.out.println("userId en session = " + userId);
    System.out.println("Login en session = " + login);
    System.out.println("isConnected = " + isConnected);  
    Map<String, FavoriWidget> widgetPreferences = (Map<String, FavoriWidget>) request.getAttribute("widgetPreferences");
    if (widgetPreferences == null) {
        widgetPreferences = new HashMap<>(); 
    }
%>


<script>
document.addEventListener("DOMContentLoaded", function () {
    function fetchWeather(lat, lon) {
        console.log("Récupération de la météo pour la position : ", lat, lon);
        
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
            .catch(error => console.error("Erreur lors de la récupération de la météo :", error));
    }

    function checkAndUpdateWeather() {
        let storedCity = sessionStorage.getItem("searched_city");
        let storedLat = localStorage.getItem("user_lat");
        let storedLon = localStorage.getItem("user_lon");

        if (storedCity) {
            console.log(`Recherche détectée : ${storedCity}, on ne met PAS à jour avec la géolocalisation.`);
            return;
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
                        fetchWeather(lat, lon);

                    } catch (e) {
                        console.error("Erreur d'accès au stockage local:", e.message);
                    }
                },
                function (error) {
                    console.error("Erreur de géolocalisation:", error.message);
                    fetchWeather(48.8566, 2.3522);
                }
            );
        } else {
            console.log("La géolocalisation n'est pas supportée.");
            fetchWeather(48.8566, 2.3522);
        }
    }

    checkAndUpdateWeather();
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
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/favoris.css"> 
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/footer.css"> 
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
                <button type="submit" class="squishy squishy-classic" title="Ajouter aux favoris">❤
                </button>
            </form>
        <% } %>
       	<div class="location">
       		<div class="city-info">
            	<i class="fas fa-map-marker-alt"></i>
        		<span class="city"><%= weather.getCity() %>, <%= weather.getCountry() %></span>
        	</div>
        	<div class="date">Dim, 10 Mars</div>
        </div>
        	<div class="temperature-info">
        	    <img src="https://openweathermap.org/img/wn/<%= weather.getIcon() %>@2x.png" alt="Météo" />
        	    <div class="details-info-weather">
            		<p class="content-temperature"><%= weather.getTemperature() %>°C</p>
            		<div class="details-plus-info-weather">
        				<p>Ressenti : <%= weather.getFeelsLike() %>°C </p>
        				<p>Max : <%= weather.getTempMax() %>° / Min : <%= weather.getTempMin() %> </p>
        			</div>
        		</div>
        	</div>
        <div class="weather-details">
            <div class="weather-item"><p>Humidité :</p>
            <p> <%= weather.getHumidity() %>% </p></div>
            <div class="weather-item">
            	<p>Vent : </p>
            	<p><%= weather.getWindSpeed() %> km/h</p>
            </div>
            <div class="weather-item">
            	<p>Pression :</p>
            	<p><%= weather.getPressure() %> mb</p>
            </div>
            <div class="weather-item">
            	<p>Visibilité : </p>
            	<p><%= weather.getVisibility() / 1000.0 %> km</p>
           	</div>
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
    <section class="favoris-container">
        <ul>
<% 
    for (WeatherData meteo : favorisMeteo) { 
        FavoriWidget widget = (widgetPreferences != null && widgetPreferences.containsKey(meteo.getCity())) 
            ? widgetPreferences.get(meteo.getCity()) 
            : new FavoriWidget(userId, meteo.getCity(), true, true, true); // Valeurs par défaut
%>
        <li class="favori-card">
            <strong><%= meteo.getCity() %>, <%= meteo.getCountry() %></strong>
            
            <% if (widget.isShowTemp()) { %>
                <p>🌡 Température : <%= meteo.getTemperature() %>°C</p>
            <% } %>

            <% if (widget.isShowHumidity()) { %>
                <p>💧 Humidité : <%= meteo.getHumidity() %>%</p>
            <% } %>

            <% if (widget.isShowWind()) { %>
                <p>💨 Vent : <%= meteo.getWindSpeed() %> km/h</p>
            <% } %>

            <form action="home" method="post">
                <input type="hidden" name="city" value="<%= meteo.getCity() %>">
                
                <label>
                    <input type="checkbox" name="showTemp" <%= widget.isShowTemp() ? "checked" : "" %> >
                    Température
                </label>
                
                <label>
                    <input type="checkbox" name="showHumidity" <%= widget.isShowHumidity() ? "checked" : "" %> >
                    Humidité
                </label>
                
                <label>
                    <input type="checkbox" name="showWind" <%= widget.isShowWind() ? "checked" : "" %> >
                    Vent
                </label>

                <button type="submit" name="updatePreferences">Mettre à jour</button>
            </form>

            <!-- Formulaire pour supprimer un favori -->
            <form action="home" method="post">
                <input type="hidden" name="deleteCity" value="<%= meteo.getCity() %>">
                <button type="submit">🗑 Supprimer</button>
            </form>
        </li>
<% } %>
        </ul>
    </section>
<% } else { %>
    <p>Aucun favori enregistré.</p>
<% } %>

</div>
<jsp:include page="Footer.jsp" />




<script>
    function confirmDelete() {
        return confirm("Voulez-vous vraiment supprimer ce favori ?");
    }
    
    function storeSearchCity() {
        let city = document.getElementById("search-city").value;
        sessionStorage.setItem("searched_city", city);
    }
</script>



</body>
</html>
