package com.se1020.movierental.service;

import com.se1020.movierental.model.ClassicMovie;
import com.se1020.movierental.model.Movie;
import com.se1020.movierental.model.NewRelease;
import com.se1020.movierental.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private final String movieFilePath;

    public MovieService(String movieFilePath) {
        this.movieFilePath = movieFilePath;
    }

    public List<Movie> getAllMovies() {
        List<String> lines = FileUtils.readAllLines(movieFilePath);
        List<Movie> movies = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 6) {
                if ("NEW".equals(parts[6])) {
                    movies.add(NewRelease.fromString(line));
                } else if ("CLASSIC".equals(parts[6])) {
                    movies.add(ClassicMovie.fromString(line));
                }
            }
        }

        return movies;
    }

    public Movie getMovieById(String movieId) {
        for (Movie movie : getAllMovies()) {
            if (movie.getMovieId().equals(movieId)) {
                return movie;
            }
        }
        return null;
    }

    public void addMovie(Movie movie) {
        FileUtils.appendLine(movieFilePath, movie.toString());
    }

    public void updateMovie(Movie movie) {
        FileUtils.updateLine(movieFilePath, movie.getMovieId(), movie.toString());
    }

    public void deleteMovie(String movieId) {
        FileUtils.deleteLine(movieFilePath, movieId);
    }

    public List<Movie> searchMovies(String keyword) {
        List<Movie> matchingMovies = new ArrayList<>();
        String searchValue = keyword.toLowerCase();

        for (Movie movie : getAllMovies()) {
            if (movie.getTitle().toLowerCase().contains(searchValue)
                || movie.getGenre().toLowerCase().contains(searchValue)
                || movie.getDirector().toLowerCase().contains(searchValue)) {
                matchingMovies.add(movie);
            }
        }

        return matchingMovies;
    }

    public List<Movie> getAvailableMovies() {
        List<Movie> availableMovies = new ArrayList<>();

        for (Movie movie : getAllMovies()) {
            if (movie.isAvailable()) {
                availableMovies.add(movie);
            }
        }

        return availableMovies;
    }

    public String generateMovieId() {
        int nextId = getAllMovies().size() + 1;
        return String.format("M%03d", nextId);
    }
}
