package com.example.chowi.goya;

import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.description;

/**
 * Created by chowi on 5/31/2017.
 */

public class Comment {

    private String id;
    private String text;
    private String author;


    public Comment() {
    }


    public Comment(String id, String text, String author) {
        super();
        this.id = id;
        this.text = text;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}