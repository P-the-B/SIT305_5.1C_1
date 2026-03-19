package com.example.sportsapp.models;

import java.io.Serializable;

// model for featured matches
public class Match implements Serializable {

    private String title;
    private String category;
    private int image;

    // constructor
    public Match(String title, String category, int image) {
        this.title = title;
        this.category = category;
        this.image = image;
    }

    // ---- getters (REQUIRED for adapter)

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getImage() {
        return image;
    }
}