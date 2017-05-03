package com.example.chowi.goya;

/**
 * Created by chowi on 4/14/2017.
 */

public class EventItem {
    private String id, title, description;
    private double latitude, longitude, displayValue;
    private int goVotes, noVotes;


    public EventItem() {
    }

    public EventItem(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.goVotes = 0;
        this.noVotes = 0;
    }

    public EventItem(String title, String description, double latitude, double longitude, int goVotes, int noVotes) {
        super();
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.goVotes = goVotes;
        this.noVotes = noVotes;
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


    public double getDisplayValue() { return displayValue; }
    public void setDisplayValue(double displayValue) { this.displayValue = displayValue; }


    public int getGoVotes() { return goVotes; }
    public void setGoVotes(int goVotes) { this.goVotes = goVotes; }


    public int getNoVotes() { return noVotes; }
    public void setNoVotes(int noVotes) { this.noVotes = noVotes; }



}