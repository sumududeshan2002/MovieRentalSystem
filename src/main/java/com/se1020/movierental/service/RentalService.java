package com.se1020.movierental.service;

import com.se1020.movierental.model.Rental;
import com.se1020.movierental.util.FileUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RentalService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_RENTAL_DAYS = 30;

    private final String rentalFilePath;

    public RentalService(String rentalFilePath) {
        this.rentalFilePath = rentalFilePath;
    }

    public List<Rental> getAllRentals() {
        List<String> lines = FileUtils.readAllLines(rentalFilePath);
        List<Rental> rentals = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 8) {
                int rentalDays = 7;
                double dailyRate = 0.0;
                double totalPrice = 0.0;

                if (parts.length >= 11) {
                    rentalDays = parseIntOrDefault(parts[8], 7);
                    dailyRate = parseDoubleOrDefault(parts[9], 0.0);
                    totalPrice = parseDoubleOrDefault(parts[10], dailyRate * rentalDays);
                }

                rentals.add(new Rental(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6],
                    parts[7],
                    rentalDays,
                    dailyRate,
                    totalPrice
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

    public Rental rentMovie(String userId, String movieId, String movieTitle, int rentalDays, double dailyRate) {
        int validRentalDays = Math.max(1, Math.min(MAX_RENTAL_DAYS, rentalDays));
        double validDailyRate = Math.max(0.0, dailyRate);
        LocalDate today = LocalDate.now();
        double totalPrice = validDailyRate * validRentalDays;
        Rental rental = new Rental(
            generateRentalId(),
            userId,
            movieId,
            sanitizeCsv(movieTitle),
            today.format(DATE_FORMAT),
            today.plusDays(validRentalDays).format(DATE_FORMAT),
            "N/A",
            "ACTIVE",
            validRentalDays,
            validDailyRate,
            totalPrice
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

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
