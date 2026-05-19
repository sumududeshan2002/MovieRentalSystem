package com.se1020.movierental.model;

public class ClassicMovie extends Movie {

    private static final double RENTAL_PRICE = 2.99;

    public ClassicMovie(String movieId, String title, String genre, String director, int year, boolean available) {
        super(movieId, title, genre, director, year, available);
    }

    @Override
    public double getRentalPrice() {
        return RENTAL_PRICE;
    }

    @Override
    public String toString() {
        return super.toString() + ",CLASSIC," + RENTAL_PRICE;
    }

    public static ClassicMovie fromString(String line) {
        String[] parts = line.split(",");
        return new ClassicMovie(
            parts[0],
            parts[1],
            parts[2],
            parts[3],
            Integer.parseInt(parts[4]),
            Boolean.parseBoolean(parts[5])
        );
    }
}
