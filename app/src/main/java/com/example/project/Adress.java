package com.example.project;

public class Adress {

    String latitude , longitude ;

    public Adress(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Adress() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
