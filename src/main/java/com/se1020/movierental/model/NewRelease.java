package com.se1020.movierental.model;

public class NewRelease extends Movie {

    private double rentalPrice;

    public NewRelease(String movieId, String title, String genre, String director, int year, boolean available,
                      double rentalPrice) {
        super(movieId, title, genre, director, year, available);
        this.rentalPrice = rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    @Override
    public double getRentalPrice() {
        return rentalPrice;
    }

    @Override
    public String toString() {
        return super.toString() + ",NEW," + rentalPrice;
    }

    public static NewRelease fromString(String line) {
        String[] parts = line.split(",");
        return new NewRelease(
            parts[0],
            parts[1],
            parts[2],
            parts[3],
            Integer.parseInt(parts[4]),
            Boolean.parseBoolean(parts[5]),
            Double.parseDouble(parts[7])
        );
    }
}
