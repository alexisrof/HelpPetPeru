package com.example.helppetperu.Class;

public class Location {

    double latitude;
    double langitude;

    public Location(){}

    public Location(double latitude, double langitude) {
        this.latitude = latitude;
        this.langitude = langitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLangitude() {
        return langitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }
}
