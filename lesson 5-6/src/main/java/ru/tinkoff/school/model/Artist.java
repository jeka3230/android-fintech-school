package ru.tinkoff.school.model;

import java.io.Serializable;
import java.util.List;

public class Artist implements Serializable {

    private long id;

    private String artist;

    private String genre;

    private String bio;

    private boolean faves;

    private String url;

    private List<String> songs;

    public long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getBio() {
        return bio;
    }

    public boolean isFaves() {
        return faves;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getSongs() {
        return songs;
    }
}
