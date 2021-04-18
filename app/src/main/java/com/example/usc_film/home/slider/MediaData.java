package com.example.usc_film.home.slider;

public class MediaData {
    private String id;
    private String title;
    private String imgUrl;

    public MediaData(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.imgUrl = url;
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
}
