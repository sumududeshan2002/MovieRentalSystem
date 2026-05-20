package com.se1020.movierental.controller;

import com.se1020.movierental.model.Movie;
import com.se1020.movierental.model.Rental;
import com.se1020.movierental.model.Review;
import com.se1020.movierental.model.User;
import com.se1020.movierental.service.MovieService;
import com.se1020.movierental.service.RentalService;
import com.se1020.movierental.service.ReviewService;
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
    private final ReviewService reviewService;

    public RentalController(ServletContext servletContext) {
        String basePath = servletContext.getRealPath("/WEB-INF/") + "../../resources/data/";
        this.rentalService = new RentalService(basePath + "rentals.txt");
        this.movieService = new MovieService(basePath + "movies.txt");
        this.reviewService = new ReviewService(basePath + "reviews.txt");
    }

    @GetMapping("/my-rentals")
    public String myRentals(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("rentals", rentalService.getRentalsByUserId(user.getUserId()));
        model.addAttribute("reviews", reviewService.getReviewsByUserId(user.getUserId()));
        return "rental/my-rentals";
    }

    @GetMapping("/detail/{rentalId}")
    public String rentalDetail(@PathVariable String rentalId, HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }

        if (safeTrim(rentalId).isEmpty()) {
            return "redirect:/rentals/my-rentals";
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
                            @RequestParam(required = false) Integer rating,
                            @RequestParam(required = false) String comment,
                            HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        movieId = safeTrim(movieId);
        movieTitle = safeTrim(movieTitle);
        comment = safeTrim(comment);
        if (movieId.isEmpty()) {
            return "redirect:/movies/browse";
        }
        if (dailyRate != null && dailyRate < 0) {
            dailyRate = DEFAULT_DAILY_RATE;
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
            return "redirect:/movies/tmdb/detail/" + movieId;
        }

        int validRentalDays = Math.max(MIN_RENTAL_DAYS, Math.min(MAX_RENTAL_DAYS, rentalDays));
        double resolvedDailyRate = movie != null
            ? movie.getRentalPrice()
            : (dailyRate != null && dailyRate > 0 ? dailyRate : DEFAULT_DAILY_RATE);

        rentalService.rentMovie(user.getUserId(), movieId, title, validRentalDays, resolvedDailyRate);
        saveFeedbackIfProvided(user, movieId, title, rating, comment);
        return "redirect:/rentals/my-rentals";
    }

    @PostMapping("/return/{rentalId}")
    public String returnMovie(@PathVariable String rentalId, HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }

        if (safeTrim(rentalId).isEmpty()) {
            return "redirect:/rentals/my-rentals";
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

    private void saveFeedbackIfProvided(User user, String movieId, String movieTitle, Integer rating, String comment) {
        if (rating == null || rating < 1 || rating > 5 || comment == null || comment.trim().isEmpty()) {
            return;
        }
        if (comment.length() > 500) {
            comment = comment.substring(0, 500);
        }

        Review existingReview = null;
        for (Review review : reviewService.getReviewsByUserId(user.getUserId())) {
            if (movieId.equals(review.getMovieId())) {
                existingReview = review;
                break;
            }
        }

        if (existingReview == null) {
            Review review = new Review(
                reviewService.generateReviewId(),
                user.getUserId(),
                user.getUsername(),
                movieId,
                movieTitle,
                rating,
                comment,
                "PENDING"
            );
            reviewService.addReview(review);
            return;
        }

        if ("PENDING".equals(existingReview.getStatus())) {
            Review updated = new Review(
                existingReview.getReviewId(),
                existingReview.getUserId(),
                existingReview.getUsername(),
                existingReview.getMovieId(),
                existingReview.getMovieTitle(),
                rating,
                comment,
                existingReview.getStatus()
            );
            reviewService.updateReview(updated);
        }
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}
