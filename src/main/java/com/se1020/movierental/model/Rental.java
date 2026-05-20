package com.se1020.movierental.model;

public class Rental {
    private String rentalId;
    private String userId;
    private String movieId;
    private String movieTitle;
    private String rentalDate;
    private String dueDate;
    private String returnDate;
    private String status;
    private int rentalDays;
    private double dailyRate;
    private double totalPrice;

    public Rental(String rentalId, String userId, String movieId, String movieTitle,
                  String rentalDate, String dueDate, String returnDate, String status,
                  int rentalDays, double dailyRate, double totalPrice) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.rentalDays = rentalDays;
        this.dailyRate = dailyRate;
        this.totalPrice = totalPrice;
    }

    public String getRentalId() {
        return rentalId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return rentalId + "," + userId + "," + movieId + "," + movieTitle + ","
            + rentalDate + "," + dueDate + "," + returnDate + "," + status + ","
            + rentalDays + "," + dailyRate + "," + totalPrice;
    }
}
