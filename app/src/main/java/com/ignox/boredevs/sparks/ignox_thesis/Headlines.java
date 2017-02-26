package com.ignox.boredevs.sparks.ignox_thesis;

import android.graphics.drawable.Drawable;
import android.os.StrictMode;

/**
 * Created by daniel on 11/12/2016.
 */
public class Headlines {
    private String title,author,desc,url,thumbnail;
    //private int thumbnail;

    public Headlines() {
    }

    public Headlines(String title,String desc, String url, String thumbnail) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.desc = desc;
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getArticleTitle(){ return title; }

    public void setArticleTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author){ this.author = author; }

    public String getDesc() { return desc; }

    public void setDesc(String desc){ this.desc = desc; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }
}
