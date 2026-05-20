package com.se1020.movierental.controller;

import com.se1020.movierental.model.ClassicMovie;
import com.se1020.movierental.model.Movie;
import com.se1020.movierental.model.NewRelease;
import com.se1020.movierental.model.User;
import com.se1020.movierental.service.MovieService;
import com.se1020.movierental.service.RentalService;
import com.se1020.movierental.service.TmdbService;
import com.se1020.movierental.util.FileUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final RentalService rentalService;
    private final TmdbService tmdbService;
    private final String movieFilePath;

    public MovieController(ServletContext application) {
        this.movieFilePath = application.getRealPath("/WEB-INF/") + "../../resources/data/movies.txt";
        String rentalFilePath = application.getRealPath("/WEB-INF/") + "../../resources/data/rentals.txt";
        this.movieService = new MovieService(movieFilePath);
        this.rentalService = new RentalService(rentalFilePath);
        this.tmdbService = new TmdbService(loadTmdbApiKey());
    }

    @GetMapping("/browse")
    public String browseMovies(Model model) {
        model.addAttribute("movies", movieService.getAvailableMovies());
        return "movie/browse-movies";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam String keyword, Model model) {
        model.addAttribute("movies", movieService.searchMovies(keyword));
        model.addAttribute("keyword", keyword);
        return "movie/search-results";
    }

    @GetMapping("/detail/{movieId}")
    public String movieDetail(@PathVariable String movieId, HttpSession session, Model model) {
        Movie movie = movieService.getMovieById(movieId);
        User user = (User) session.getAttribute("loggedInUser");
        boolean alreadyRented = false;

        if (user != null) {
            alreadyRented = rentalService.isMovieRentedByUser(user.getUserId(), movieId);
        }

        model.addAttribute("movie", movie);
        model.addAttribute("alreadyRented", alreadyRented);
        return "movie/movie-detail";
    }

    @GetMapping("/admin/list")
    public String adminMovieList(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        model.addAttribute("movies", movieService.getAllMovies());
        return "movie/admin/movie-list";
    }

    @GetMapping("/admin/add")
    public String showAddMovieForm(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        return "movie/admin/add-movie";
    }

    @PostMapping("/admin/add")
    public String addMovie(@RequestParam String type,
                           @RequestParam String title,
                           @RequestParam String genre,
                           @RequestParam String director,
                           @RequestParam int year,
                           @RequestParam boolean available,
                           @RequestParam(required = false) Double rentalPrice,
                           HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        String movieId = movieService.generateMovieId();
        Movie movie;

        if ("NEW".equalsIgnoreCase(type)) {
            movie = new NewRelease(movieId, title, genre, director, year, available,
                rentalPrice != null ? rentalPrice : 0.0);
        } else {
            movie = new ClassicMovie(movieId, title, genre, director, year, available);
        }

        movieService.addMovie(movie);
        return "redirect:/movies/admin/list";
    }

    @GetMapping("/admin/edit/{movieId}")
    public String showEditMovieForm(@PathVariable String movieId, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        model.addAttribute("movie", movieService.getMovieById(movieId));
        return "movie/admin/edit-movie";
    }

    @PostMapping("/admin/edit/{movieId}")
    public String editMovie(@PathVariable String movieId,
                            @RequestParam String type,
                            @RequestParam String title,
                            @RequestParam String genre,
                            @RequestParam String director,
                            @RequestParam int year,
                            @RequestParam boolean available,
                            @RequestParam(required = false) Double rentalPrice,
                            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        Movie movie;

        if ("NEW".equalsIgnoreCase(type)) {
            movie = new NewRelease(movieId, title, genre, director, year, available,
                rentalPrice != null ? rentalPrice : 0.0);
        } else {
            movie = new ClassicMovie(movieId, title, genre, director, year, available);
        }

        movieService.updateMovie(movie);
        return "redirect:/movies/admin/list";
    }

    @GetMapping("/admin/delete/{movieId}")
    public String deleteMovie(@PathVariable String movieId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        movieService.deleteMovie(movieId);
        return "redirect:/movies/admin/list";
    }

    @GetMapping("/tmdb/popular")
    public String browseTmdbPopularMovies(Model model) {
        model.addAttribute("tmdbMovies", tmdbService.getPopularMovies());
        return "movie/tmdb-browse";
    }

    @GetMapping("/tmdb/search")
    public String searchTmdbMovies(@RequestParam String query, Model model) {
        model.addAttribute("tmdbMovies", tmdbService.searchMovies(query));
        model.addAttribute("query", query);
        return "movie/tmdb-search-results";
    }

    @GetMapping("/tmdb/detail/{tmdbId}")
    public String tmdbMovieDetail(@PathVariable String tmdbId, Model model) {
        Map<String, String> tmdbMovie = tmdbService.getMovieDetails(tmdbId);
        if (tmdbMovie.isEmpty() || tmdbMovie.get("title") == null || tmdbMovie.get("title").trim().isEmpty()) {
            model.addAttribute("error", "Movie details could not be loaded. Please try another movie.");
        }
        model.addAttribute("tmdbMovie", tmdbMovie);
        return "movie/tmdb-detail";
    }

    @PostMapping("/tmdb/import/{tmdbId}")
    public String importTmdbMovie(@PathVariable String tmdbId,
                                  @RequestParam String type,
                                  @RequestParam(required = false) String rentalPrice,
                                  HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        Map<String, String> tmdbData = tmdbService.getMovieDetails(tmdbId);

        String movieId = "T" + tmdbData.get("id");
        String title = tmdbData.get("title");
        String genre = tmdbData.get("genres");
        String director = "TMDB";

        // Keep CSV column positions stable for MovieService parsing.
        if (title != null) {
            title = title.replace(",", " ");
        }
        if (genre != null) {
            genre = genre.replace(",", " /");
        }
        director = director.replace(",", " ");
        int year = 0;
        try {
            String releaseDate = tmdbData.get("release_date");
            if (releaseDate != null && releaseDate.length() >= 4) {
                year = Integer.parseInt(releaseDate.substring(0, 4));
            }
        } catch (NumberFormatException ignored) {
        }

        double price = 2.99;
        if (rentalPrice != null && !rentalPrice.isEmpty()) {
            try {
                price = Double.parseDouble(rentalPrice);
            } catch (NumberFormatException ignored) {
            }
        }

        Movie movie;
        if ("NEW".equals(type)) {
            movie = new NewRelease(movieId, title, genre, director, year, true, price);
        } else {
            movie = new ClassicMovie(movieId, title, genre, director, year, true);
        }

        FileUtils.appendLine(movieFilePath, movie.toString());
        return "redirect:/movies/admin/list";
    }

    private String loadTmdbApiKey() {
        Properties properties = new Properties();
        try (InputStream inputStream = new ClassPathResource("application.properties").getInputStream()) {
            properties.load(inputStream);
            return properties.getProperty("tmdb.api.key", "");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load TMDB API key", e);
        }
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        return user != null && "ADMIN".equals(user.getRole());
    }
}
