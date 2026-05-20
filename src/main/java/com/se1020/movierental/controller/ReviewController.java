package com.se1020.movierental.controller;

import com.se1020.movierental.model.Movie;
import com.se1020.movierental.model.Review;
import com.se1020.movierental.model.User;
import com.se1020.movierental.service.MovieService;
import com.se1020.movierental.service.ReviewService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final MovieService movieService;

    public ReviewController(ServletContext application) {
        String reviewFilePath = application.getRealPath("/WEB-INF/") + "../../resources/data/reviews.txt";
        String movieFilePath = application.getRealPath("/WEB-INF/") + "../../resources/data/movies.txt";
        this.reviewService = new ReviewService(reviewFilePath);
        this.movieService = new MovieService(movieFilePath);
    }

    @GetMapping("/movie/{movieId}")
    public String movieReviews(@PathVariable String movieId, Model model) {
        model.addAttribute("reviews", reviewService.getApprovedReviewsByMovieId(movieId));
        model.addAttribute("movieId", movieId);
        return "review/movie-reviews";
    }

    @GetMapping("/add/{movieId}")
    public String showAddReviewForm(@PathVariable String movieId, HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        Movie movie = movieService.getMovieById(movieId);
        model.addAttribute("movieId", movieId);
        model.addAttribute("movieTitle", movie != null ? movie.getTitle() : movieId);
        return "review/add-review";
    }

    @PostMapping("/add/{movieId}")
    public String addReview(@PathVariable String movieId,
                            @RequestParam int rating,
                            @RequestParam String comment,
                            HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        if (reviewService.userAlreadyReviewed(user.getUserId(), movieId)) {
            return "redirect:/reviews/movie/" + movieId + "?error=already-reviewed";
        }

        Movie movie = movieService.getMovieById(movieId);
        String movieTitle = movie != null ? movie.getTitle() : movieId;
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
        return "redirect:/reviews/movie/" + movieId;
    }

    @GetMapping("/my-reviews")
    public String myReviews(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("reviews", reviewService.getReviewsByUserId(user.getUserId()));
        return "review/my-reviews";
    }

    @GetMapping("/edit/{reviewId}")
    public String showEditReviewForm(@PathVariable String reviewId, HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        Review review = reviewService.getReviewById(reviewId);
        if (review == null || !review.getUserId().equals(user.getUserId()) || !"PENDING".equals(review.getStatus())) {
            return "redirect:/reviews/my-reviews";
        }
        model.addAttribute("review", review);
        return "review/edit-review";
    }

    @PostMapping("/edit/{reviewId}")
    public String editReview(@PathVariable String reviewId,
                             @RequestParam int rating,
                             @RequestParam String comment,
                             HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        Review review = reviewService.getReviewById(reviewId);
        if (review == null || !review.getUserId().equals(user.getUserId()) || !"PENDING".equals(review.getStatus())) {
            return "redirect:/reviews/my-reviews";
        }

        Review updatedReview = new Review(
            review.getReviewId(),
            review.getUserId(),
            review.getUsername(),
            review.getMovieId(),
            review.getMovieTitle(),
            rating,
            comment,
            review.getStatus()
        );
        reviewService.updateReview(updatedReview);
        return "redirect:/reviews/my-reviews";
    }

    @GetMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable String reviewId, HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        Review review = reviewService.getReviewById(reviewId);
        if (review != null && review.getUserId().equals(user.getUserId())) {
            reviewService.deleteReview(reviewId);
        }
        return "redirect:/reviews/my-reviews";
    }

    @GetMapping("/admin/list")
    public String adminReviewList(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "review/admin/review-list";
    }

    @GetMapping("/admin/approve/{reviewId}")
    public String approveReview(@PathVariable String reviewId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        reviewService.approveReview(reviewId);
        return "redirect:/reviews/admin/list";
    }

    @GetMapping("/admin/delete/{reviewId}")
    public String adminDeleteReview(@PathVariable String reviewId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        reviewService.deleteReview(reviewId);
        return "redirect:/reviews/admin/list";
    }

    private User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    private boolean isAdmin(HttpSession session) {
        User user = getLoggedInUser(session);
        return user != null && "ADMIN".equals(user.getRole());
    }
}
