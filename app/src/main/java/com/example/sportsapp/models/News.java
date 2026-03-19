package com.example.sportsapp.models;

import java.io.Serializable;

// maps to the NewsAPI article object
// field names must match the JSON keys exactly so Gson maps them automatically
public class News implements Serializable {

    public String title;
    public String description;
    public String urlToImage;
    public String author;
    public String publishedAt;
    public String url;
    public String content;
    public Source source;

    // nested object — maps to "source": { "name": "ESPN" } in the JSON
    public static class Source implements Serializable {
        public String name;
    }

    // required empty constructor for Gson
    public News() {}

    // convenience constructor used for dummy data (matches, related stories)
    public News(String title, String description, String urlToImage) {
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
    }
}