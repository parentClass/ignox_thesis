package com.ignox.boredevs.sparks.ignox_thesis;

/**
 * Created by daniel on 11/5/2016.
 */
public class SearchResult {
    private String title, link;

    public SearchResult() {
    }

    public SearchResult(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
