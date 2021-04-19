package com.example.usc_film.search;

public class SearchData {
    private String id;
    private String imgUrl;
    private String type;
    private String title;
    private String year;
    private String rating;

    public SearchData (String id, String imgUrl, String type, String title, String  year, String rating) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.type = type;
        this.title = title;
        this.year = year;
        this.rating = rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
