package com.se1020.movierental.service;

import com.se1020.movierental.model.Rental;
import com.se1020.movierental.util.FileUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RentalService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String rentalFilePath;

    public RentalService(String rentalFilePath) {
        this.rentalFilePath = rentalFilePath;
    }

    public List<Rental> getAllRentals() {
        List<String> lines = FileUtils.readAllLines(rentalFilePath);
        List<Rental> rentals = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 8) {
                rentals.add(new Rental(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6],
                    parts[7]
                ));
            }
        }

        return rentals;
    }

    public List<Rental> getRentalsByUserId(String userId) {
        List<Rental> userRentals = new ArrayList<>();

        for (Rental rental : getAllRentals()) {
            if (rental.getUserId().equals(userId)) {
                userRentals.add(rental);
            }
        }

        return userRentals;
    }

    public Rental getRentalById(String rentalId) {
        for (Rental rental : getAllRentals()) {
            if (rental.getRentalId().equals(rentalId)) {
                return rental;
            }
        }
        return null;
    }

    public boolean isMovieRentedByUser(String userId, String movieId) {
        for (Rental rental : getAllRentals()) {
            if (rental.getUserId().equals(userId)
                && rental.getMovieId().equals(movieId)
                && "ACTIVE".equals(rental.getStatus())) {
                return true;
            }
        }
        return false;
    }

    public Rental rentMovie(String userId, String movieId, String movieTitle) {
        LocalDate today = LocalDate.now();
        Rental rental = new Rental(
            generateRentalId(),
            userId,
            movieId,
            sanitizeCsv(movieTitle),
            today.format(DATE_FORMAT),
            today.plusDays(7).format(DATE_FORMAT),
            "N/A",
            "ACTIVE"
        );
        FileUtils.appendLine(rentalFilePath, rental.toString());
        return rental;
    }

    public boolean returnMovie(String rentalId) {
        Rental rental = getRentalById(rentalId);
        if (rental == null || !"ACTIVE".equals(rental.getStatus())) {
            return false;
        }

        rental.setReturnDate(LocalDate.now().format(DATE_FORMAT));
        rental.setStatus("RETURNED");
        FileUtils.updateLine(rentalFilePath, rentalId, rental.toString());
        return true;
    }

    public String generateRentalId() {
        int nextId = getAllRentals().size() + 1;
        return String.format("R%03d", nextId);
    }

    private String sanitizeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", " ");
    }
}
