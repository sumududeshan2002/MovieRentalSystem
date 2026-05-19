package com.se1020.movierental.controller;

import com.se1020.movierental.model.ClassicMovie;
import com.se1020.movierental.model.Movie;
import com.se1020.movierental.model.NewRelease;
import com.se1020.movierental.service.MovieService;
import com.se1020.movierental.service.TmdbService;
import jakarta.servlet.ServletContext;
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
import java.util.Properties;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final TmdbService tmdbService;
    private final String movieFilePath;

    public MovieController(ServletContext application) {
        this.movieFilePath = application.getRealPath("/WEB-INF/") + "../../resources/data/movies.txt";
        this.movieService = new MovieService(movieFilePath);
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
    public String movieDetail(@PathVariable String movieId, Model model) {
        model.addAttribute("movie", movieService.getMovieById(movieId));
        return "movie/movie-detail";
    }

    @GetMapping("/admin/list")
    public String adminMovieList(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movie/admin/movie-list";
    }

    @GetMapping("/admin/add")
    public String showAddMovieForm() {
        return "movie/admin/add-movie";
    }

    @PostMapping("/admin/add")
    public String addMovie(@RequestParam String type,
                           @RequestParam String title,
                           @RequestParam String genre,
                           @RequestParam String director,
                           @RequestParam int year,
                           @RequestParam boolean available,
                           @RequestParam(required = false) Double rentalPrice) {
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
    public String showEditMovieForm(@PathVariable String movieId, Model model) {
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
                            @RequestParam(required = false) Double rentalPrice) {
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
    public String deleteMovie(@PathVariable String movieId) {
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
        model.addAttribute("tmdbMovie", tmdbService.getMovieDetails(tmdbId));
        return "movie/tmdb-detail";
    }

    @PostMapping("/tmdb/import/{tmdbId}")
    public String importTmdbMovie(@PathVariable String tmdbId, @RequestParam String type) {
        movieService.getAllMovies();
        tmdbService.importMovieToFile(tmdbService.getMovieDetails(tmdbId), movieFilePath, type);
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
}
