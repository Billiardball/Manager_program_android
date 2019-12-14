package com.driver.porttrucker.Model;

import java.io.Serializable;

public class VideoModel implements Serializable {

    int id;
    String link = "", title = "", thumb = "";

    public VideoModel(int id, String link, String title, String thumb) {
        this.id = id;
        this.link = link;
        this.title = title;
        this.thumb = thumb;
    }

    public int getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getThumb() {
        return thumb;
    }
}
