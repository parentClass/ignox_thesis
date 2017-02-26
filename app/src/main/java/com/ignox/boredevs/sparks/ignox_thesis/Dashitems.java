package com.ignox.boredevs.sparks.ignox_thesis;

/**
 * Created by daniel on 11/3/2016.
 */
public class Dashitems {
    private String name,subtxt;
    private int thumbnail;

    public Dashitems() {
    }

    public Dashitems(String name, String subtxt,int thumbnail) {
        this.name = name;
        this.subtxt = subtxt;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtxt(){ return subtxt; }

    public void setSubtxt(String subtxt) { this.subtxt = subtxt; }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

}