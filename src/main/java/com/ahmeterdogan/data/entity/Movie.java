package com.ahmeterdogan.data.entity;

import java.time.LocalDate;

public class Movie {
    private long id;
    private String name;
    private LocalDate releaseDate;
    private String director;

    public Movie(long id, String name, LocalDate releaseDate, String director) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.director = director;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}