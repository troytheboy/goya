package com.example.chowi.goya;

/**
 * Created by chowi on 4/14/2017.
 */

public class EventItem {
    public String id, title, description;
    public double latitude, longitude;


    public EventItem() {
    }

    public EventItem(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public EventItem(String id, String title, String description, double latitude, double longitude) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

}