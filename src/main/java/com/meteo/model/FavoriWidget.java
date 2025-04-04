package com.meteo.model;

public class FavoriWidget {
    private int id;
    private int userId;
    private String city;
    private boolean showTemp;
    private boolean showHumidity;
    private boolean showWind;

    public FavoriWidget(int userId, String city, boolean showTemp, boolean showHumidity, boolean showWind) {
        this.id = 0;
        this.userId = userId;
        this.city = city;
        this.showTemp = showTemp;
        this.showHumidity = showHumidity;
        this.showWind = showWind;
    }

    public FavoriWidget(int id, int userId, String city, boolean showTemp, boolean showHumidity, boolean showWind) {
        this.id = id;
        this.userId = userId;
        this.city = city;
        this.showTemp = showTemp;
        this.showHumidity = showHumidity;
        this.showWind = showWind;
    }

    // Getters et Setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getCity() { return city; }
    public boolean isShowTemp() { return showTemp; }
    public boolean isShowHumidity() { return showHumidity; }
    public boolean isShowWind() { return showWind; }

    public void setShowTemp(boolean showTemp) { this.showTemp = showTemp; }
    public void setShowHumidity(boolean showHumidity) { this.showHumidity = showHumidity; }
    public void setShowWind(boolean showWind) { this.showWind = showWind; }
}
