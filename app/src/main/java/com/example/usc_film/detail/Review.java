package com.example.usc_film.detail;

public class Review {
    private String name;
    private String rating;
    private String context;
    private String date;

    public Review(String name, String rating, String context, String date) {
        this.name = name;
        this.rating = rating;
        this.context = context;
        this.date = date;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
