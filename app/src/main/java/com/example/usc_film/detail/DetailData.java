package com.example.usc_film.detail;

import com.example.usc_film.home.MediaData;

import java.util.ArrayList;

public class DetailData {
    private String id;
    private String type;
    private String video_key;
    private String backdrop_path;
    private String imgUrl;
    private String title;
    private String overview;
    private String genres;
    private String year;
    private ArrayList<Cast> casts;
    private ArrayList<Review> reviews;
    private ArrayList<MediaData> recommends;

    public DetailData(String id, String type) {
        this.id = id;
        this.type = type;
        casts = new ArrayList<>();
        reviews = new ArrayList<>();
        recommends = new ArrayList<>();
    }

    public String getVideo_key() {
        return video_key;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Cast> getCasts() {
        return casts;
    }

    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<MediaData> getRecommends() {
        return recommends;
    }

    public void setRecommends(ArrayList<MediaData> recommends) {
        this.recommends = recommends;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
