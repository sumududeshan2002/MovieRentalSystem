package com.se1020.movierental.model;

public class Review {

    private String reviewId;
    private String userId;
    private String username;
    private String movieId;
    private String movieTitle;
    private int rating;
    private String comment;
    private String status;

    public Review(String reviewId, String userId, String username, String movieId, String movieTitle, int rating,
                  String comment, String status) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.username = username;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.rating = rating;
        this.comment = comment;
        this.status = status;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return reviewId + "," + userId + "," + username + "," + movieId + "," + movieTitle + "," + rating + ","
            + comment + "," + status;
    }
}
