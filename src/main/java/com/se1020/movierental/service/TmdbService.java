package com.se1020.movierental.service;

import com.se1020.movierental.model.ClassicMovie;
import com.se1020.movierental.model.Movie;
import com.se1020.movierental.model.NewRelease;
import com.se1020.movierental.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TmdbService {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private final String apiKey;

    public TmdbService(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Map<String, String>> getPopularMovies() {
        String response = sendGetRequest(BASE_URL + "/movie/popular?api_key=" + apiKey + "&language=en-US&page=1");
        return parseMovieList(response);
    }

    public List<Map<String, String>> searchMovies(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String response = sendGetRequest(
            BASE_URL + "/search/movie?api_key=" + apiKey + "&query=" + encodedQuery + "&language=en-US"
        );
        return parseMovieList(response);
    }

    public Map<String, String> getMovieDetails(String tmdbId) {
        String response = sendGetRequest(BASE_URL + "/movie/" + tmdbId + "?api_key=" + apiKey + "&language=en-US");
        if (response.isEmpty()) {
            return new HashMap<>();
        }

        JSONObject movieJson = new JSONObject(response);
        Map<String, String> movie = parseMovieObject(movieJson);
        movie.put("runtime", String.valueOf(movieJson.optInt("runtime", 0)));

        JSONArray genresArray = movieJson.optJSONArray("genres");
        List<String> genres = new ArrayList<>();
        if (genresArray != null) {
            for (int i = 0; i < genresArray.length(); i++) {
                genres.add(genresArray.getJSONObject(i).optString("name", ""));
            }
        }
        movie.put("genres", String.join(", ", genres));

        return movie;
    }

    public Movie importMovieToFile(Map<String, String> tmdbMovie, String filePath, String type) {
        String movieId = "T" + tmdbMovie.get("id");
        String title = tmdbMovie.getOrDefault("title", "");
        String genre = tmdbMovie.getOrDefault("genres", "Unknown");
        String director = "TMDB";
        String releaseDate = tmdbMovie.getOrDefault("release_date", "");
        int year = extractYear(releaseDate);
        boolean available = true;

        Movie movie;
        if ("NEW".equalsIgnoreCase(type)) {
            double rentalPrice = Double.parseDouble(tmdbMovie.getOrDefault("vote_average", "0")) / 2;
            movie = new NewRelease(movieId, title, genre, director, year, available, rentalPrice);
        } else {
            movie = new ClassicMovie(movieId, title, genre, director, year, available);
        }

        FileUtils.appendLine(filePath, movie.toString());
        return movie;
    }

    private List<Map<String, String>> parseMovieList(String response) {
        List<Map<String, String>> movies = new ArrayList<>();
        if (response.isEmpty()) {
            return movies;
        }

        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.optJSONArray("results");
        if (results == null) {
            return movies;
        }

        for (int i = 0; i < results.length(); i++) {
            movies.add(parseMovieObject(results.getJSONObject(i)));
        }

        return movies;
    }

    private Map<String, String> parseMovieObject(JSONObject movieJson) {
        Map<String, String> movie = new HashMap<>();
        movie.put("id", String.valueOf(movieJson.optInt("id", 0)));
        movie.put("title", movieJson.optString("title", ""));
        movie.put("overview", movieJson.optString("overview", ""));
        movie.put("release_date", movieJson.optString("release_date", ""));
        movie.put("vote_average", String.valueOf(movieJson.optDouble("vote_average", 0.0)));

        String posterPath = movieJson.optString("poster_path", "");
        if (!posterPath.isEmpty() && !"null".equals(posterPath)) {
            movie.put("poster_path", IMAGE_BASE_URL + posterPath);
        } else {
            movie.put("poster_path", "");
        }

        return movie;
    }

    private String sendGetRequest(String urlString) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private int extractYear(String releaseDate) {
        if (releaseDate != null && releaseDate.length() >= 4) {
            try {
                return Integer.parseInt(releaseDate.substring(0, 4));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
