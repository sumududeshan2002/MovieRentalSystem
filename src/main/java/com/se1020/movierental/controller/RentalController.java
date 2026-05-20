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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rentals")
public class RentalController {

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
    public String rentMovie(@PathVariable String movieId, HttpSession session) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }

        Movie movie = movieService.getMovieById(movieId);
        if (movie == null || rentalService.isMovieRentedByUser(user.getUserId(), movieId)) {
            return "redirect:/movies/detail/" + movieId;
        }

        rentalService.rentMovie(user.getUserId(), movieId, movie.getTitle());
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
