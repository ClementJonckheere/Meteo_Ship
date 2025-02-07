package com.meteo.model;

public class Favori {
    private int id;
    private int userId;
    private String city;
    private String country;

    public Favori(int id, int userId, String city, String country) {
        this.id = id;
        this.userId = userId;
        this.city = city;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
