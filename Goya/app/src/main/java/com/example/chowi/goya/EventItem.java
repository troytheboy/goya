package com.example.chowi.goya;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chowi on 4/14/2017.
 */

public class EventItem implements Parcelable {
    private String id;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private int goVotes;
    private int noVotes;
    private String image;
    private String username;


    public EventItem() {
    }



    public EventItem(String id, String title, String description, double latitude, double longitude, int goVotes, int noVotes, String image, String username) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.goVotes = goVotes;
        this.noVotes = noVotes;
        this.image = image;
        this.username = username;
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


    public int getGoVotes() { return goVotes; }
    public void setGoVotes(int goVotes) { this.goVotes = goVotes; }


    public int getNoVotes() { return noVotes; }
    public void setNoVotes(int noVotes) { this.noVotes = noVotes; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    public String toString() {
        return "title: " + this.title + "  description: " + this.description + "  latitude: " + this.latitude
                + "  longitude: " + this.longitude + "  govotes: " + this.goVotes + "  novotes: " + this.noVotes
                + "  image: " + this.image;
    }

    protected EventItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        goVotes = in.readInt();
        noVotes = in.readInt();
        image = in.readString();
        username = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(goVotes);
        dest.writeInt(noVotes);
        dest.writeString(image);
        dest.writeString(username);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EventItem> CREATOR = new Parcelable.Creator<EventItem>() {
        @Override
        public EventItem createFromParcel(Parcel in) {
            return new EventItem(in);
        }

        @Override
        public EventItem[] newArray(int size) {
            return new EventItem[size];
        }
    };
}