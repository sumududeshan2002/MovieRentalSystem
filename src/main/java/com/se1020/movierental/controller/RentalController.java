package com.se1020.movierental.controller;

import com.se1020.movierental.model.Movie;
import com.se1020.movierental.model.Rental;
import com.se1020.movierental.model.User;
import com.se1020.movierental.service.MovieService;
import com.se1020.movierental.service.RentalService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rentals")
public class RentalController {

    private static final int MIN_RENTAL_DAYS = 1;
    private static final int MAX_RENTAL_DAYS = 30;
    private static final double DEFAULT_DAILY_RATE = 2.99;

    private final RentalService rentalService;
    private final MovieService movieService;

    public RentalController(ServletContext servletContext) {
        String basePath = servletContext.getRealPath("/WEB-INF/") + "../../resources/data/";
        this.rentalService = new RentalService(basePath + "rentals.txt");
        this.movieService = new MovieService(basePath + "movies.txt");
    }

    @GetMapping("/my-rentals")
    public String myRentals(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("rentals", rentalService.getRentalsByUserId(user.getUserId()));
        return "rental/my-rentals";
    }

    @GetMapping("/detail/{rentalId}")
    public String rentalDetail(@PathVariable String rentalId, HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }

        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null || !rental.getUserId().equals(user.getUserId())) {
            return "redirect:/rentals/my-rentals";
        }

        model.addAttribute("rental", rental);
        return "rental/rental-detail";
    }

    @PostMapping("/rent/{movieId}")
    public String rentMovie(@PathVariable String movieId,
                            @RequestParam(required = false) String movieTitle,
                            @RequestParam(defaultValue = "7") int rentalDays,
                            @RequestParam(required = false) Double dailyRate,
                            HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }

        Movie movie = movieService.getMovieById(movieId);
        if (rentalService.isMovieRentedByUser(user.getUserId(), movieId)) {
            if (movie != null) {
                return "redirect:/movies/detail/" + movieId + "?alreadyRented=true";
            }
            return "redirect:/movies/tmdb/detail/" + movieId + "?alreadyRented=true";
        }

        String title = movie != null ? movie.getTitle() : movieTitle;
        if (title == null || title.trim().isEmpty()) {
            return "redirect:/movies/detail/" + movieId;
        }

        int validRentalDays = Math.max(MIN_RENTAL_DAYS, Math.min(MAX_RENTAL_DAYS, rentalDays));
        double resolvedDailyRate = movie != null
            ? movie.getRentalPrice()
            : (dailyRate != null && dailyRate > 0 ? dailyRate : DEFAULT_DAILY_RATE);

        rentalService.rentMovie(user.getUserId(), movieId, title, validRentalDays, resolvedDailyRate);
        return "redirect:/rentals/my-rentals";
    }

    @PostMapping("/return/{rentalId}")
    public String returnMovie(@PathVariable String rentalId, HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }

        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null || !rental.getUserId().equals(user.getUserId())) {
            return "redirect:/rentals/my-rentals";
        }

        rentalService.returnMovie(rentalId);
        return "redirect:/rentals/my-rentals";
    }

    @GetMapping("/admin/list")
    public String adminRentalList(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/users/login";
        }

        model.addAttribute("rentals", rentalService.getAllRentals());
        return "rental/admin/rental-list";
    }

    private User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }
}
