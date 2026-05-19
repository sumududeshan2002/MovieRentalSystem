package com.se1020.movierental.model;

public abstract class Movie {

    private String movieId;
    private String title;
    private String genre;
    private String director;
    private int year;
    private boolean available;

    public Movie(String movieId, String title, String genre, String director, int year, boolean available) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.year = year;
        this.available = available;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract double getRentalPrice();

    public String getMovieType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return movieId + "," + title + "," + genre + "," + director + "," + year + "," + available;
    }
}
