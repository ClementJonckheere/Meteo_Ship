package com.meteo.service;

import com.meteo.model.WeatherData;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {
    private static final String API_KEY = "6d2ebb790280b956b191f18d3278b3b5";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static WeatherData getWeatherByCity(String city) {
        String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=fr";
        return fetchWeather(urlString);
    }

    public static WeatherData getWeatherByCoordinates(double lat, double lon) {
        String urlString = BASE_URL + "?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY + "&units=metric&lang=fr";
        return fetchWeather(urlString);
    }

    private static WeatherData fetchWeather(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());
            return new WeatherData(
                json.getString("name"),
                json.getJSONObject("sys").getString("country"),
                json.getJSONObject("main").getDouble("temp"),
                json.getJSONObject("main").getDouble("feels_like"),
                json.getJSONObject("main").getDouble("temp_max"),
                json.getJSONObject("main").getDouble("temp_min"),
                json.getJSONObject("main").getInt("humidity"),
                json.getJSONObject("main").getInt("pressure"),
                json.getInt("visibility"),
                json.getJSONObject("wind").getDouble("speed"),
                json.getJSONArray("weather").getJSONObject(0).getString("description"),
                json.getJSONArray("weather").getJSONObject(0).getString("icon"),
                formatTime(json.getJSONObject("sys").getLong("sunrise")),
                formatTime(json.getJSONObject("sys").getLong("sunset"))
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String formatTime(long timestamp) {
        java.util.Date date = new java.util.Date(timestamp * 1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Paris"));
        return sdf.format(date);
    }
}
