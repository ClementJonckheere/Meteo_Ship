package com.meteo.model;

public class WeatherData {
    private String city;
    private String country;
    private double temperature;
    private double feelsLike;
    private double tempMax;
    private double tempMin;
    private int humidity;
    private int pressure;
    private int visibility;
    private double windSpeed;
    private String weather;
    private String icon;
    private String sunrise;
    private String sunset;

    public WeatherData(String city, String country, double temperature, double feelsLike, double tempMax, double tempMin, 
                        int humidity, int pressure, int visibility, double windSpeed, String weather, String icon, 
                        String sunrise, String sunset) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.weather = weather;
        this.icon = icon;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getCity() { return city; }
    public String getCountry() { return country; }
    public double getTemperature() { return temperature; }
    public double getFeelsLike() { return feelsLike; }
    public double getTempMax() { return tempMax; }
    public double getTempMin() { return tempMin; }
    public int getHumidity() { return humidity; }
    public int getPressure() { return pressure; }
    public int getVisibility() { return visibility; }
    public double getWindSpeed() { return windSpeed; }
    public String getWeather() { return weather; }
    public String getIcon() { return icon; }
    public String getSunrise() { return sunrise; }
    public String getSunset() { return sunset; }
}
