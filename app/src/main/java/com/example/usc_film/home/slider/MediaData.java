package com.example.usc_film.home.slider;

public class MediaData {
    private String id;
    private String title;
    private String imgUrl;
    private String type;

    public MediaData(String id, String title, String url, String type) {
        this.id = id;
        this.title = title;
        this.imgUrl = url;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String url) {
        this.imgUrl = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
