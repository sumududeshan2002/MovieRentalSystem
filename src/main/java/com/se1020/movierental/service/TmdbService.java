package com.se1020.movierental.service;

import com.se1020.movierental.model.ClassicMovie;
import com.se1020.movierental.model.Movie;
import com.se1020.movierental.model.NewRelease;
import com.se1020.movierental.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.zip.GZIPInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        ApiResponse apiResponse = sendGetRequest(BASE_URL + "/movie/popular?api_key=" + apiKey + "&language=en-US&page=1");
        if (apiResponse.statusCode != 200) {
            System.err.println("TMDB popular API request failed with status: " + apiResponse.statusCode);
            return new ArrayList<>();
        }
        if (apiResponse.body == null || apiResponse.body.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return parseMovieList(apiResponse.body);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Map<String, String>> searchMovies(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        ApiResponse apiResponse = sendGetRequest(
            BASE_URL + "/search/movie?api_key=" + apiKey + "&query=" + encodedQuery + "&language=en-US"
        );
        if (apiResponse.statusCode != 200) {
            System.err.println("TMDB search API request failed with status: " + apiResponse.statusCode);
            return new ArrayList<>();
        }
        if (apiResponse.body == null || apiResponse.body.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return parseMovieList(apiResponse.body);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Map<String, String> getMovieDetails(String tmdbId) {
        ApiResponse apiResponse = sendGetRequest(BASE_URL + "/movie/" + tmdbId + "?api_key=" + apiKey + "&language=en-US");
        if (apiResponse.statusCode != 200) {
            System.err.println("TMDB details API request failed with status: " + apiResponse.statusCode);
            return new HashMap<>();
        }
        if (apiResponse.body == null || apiResponse.body.trim().isEmpty()) {
            return new HashMap<>();
        }

        try {
            JSONObject movieJson = new JSONObject(apiResponse.body);
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
            String title = movie.get("title");
            if (title == null || title.trim().isEmpty()) {
                movie.put("error", "Movie details not available");
            }

            return movie;
        } catch (JSONException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
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
        if (response == null || response.trim().isEmpty()) {
            return movies;
        }
        try {
            JSONObject jsonObject = new JSONObject(response.trim());
            JSONArray results = jsonObject.optJSONArray("results");
            if (results == null) {
                return movies;
            }
            for (int i = 0; i < results.length(); i++) {
                movies.add(parseMovieObject(results.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    private ApiResponse sendGetRequest(String urlString) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            int statusCode = connection.getResponseCode();

            InputStream inputStream;
            if (statusCode >= 200 && statusCode < 300) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            if (inputStream == null) {
                return new ApiResponse(statusCode, "");
            }

            String encoding = connection.getContentEncoding();
            if ("gzip".equalsIgnoreCase(encoding)) {
                inputStream = new GZIPInputStream(inputStream);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new ApiResponse(statusCode, response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse(-1, "");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static class ApiResponse {
        private final int statusCode;
        private final String body;

        private ApiResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
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



