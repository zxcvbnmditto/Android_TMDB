package com.example.usc_film.watchlist;

public class WatchlistData {
    private String imgUrl;
    private String type;
    private String title;
    private String id;

    public WatchlistData(String imgUrl, String type, String id, String title) {
        this.imgUrl = imgUrl;
        this.type = type;
        this.id = id;
        this.title = title;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
