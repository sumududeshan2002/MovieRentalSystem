package com.se1020.movierental.service;

import com.se1020.movierental.model.Review;
import com.se1020.movierental.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class ReviewService {

    private final String reviewFilePath;

    public ReviewService(String reviewFilePath) {
        this.reviewFilePath = reviewFilePath;
    }

    public List<Review> getAllReviews() {
        List<String> lines = FileUtils.readAllLines(reviewFilePath);
        List<Review> reviews = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",", 8);
            if (parts.length == 8) {
                int rating = 0;
                try {
                    rating = Integer.parseInt(parts[5]);
                } catch (NumberFormatException ignored) {
                }
                reviews.add(new Review(parts[0], parts[1], parts[2], parts[3], parts[4], rating, parts[6], parts[7]));
            }
        }

        return reviews;
    }

    public List<Review> getReviewsByMovieId(String movieId) {
        List<Review> reviews = new ArrayList<>();
        for (Review review : getAllReviews()) {
            if (review.getMovieId().equals(movieId)) {
                reviews.add(review);
            }
        }
        return reviews;
    }

    public List<Review> getReviewsByUserId(String userId) {
        List<Review> reviews = new ArrayList<>();
        for (Review review : getAllReviews()) {
            if (review.getUserId().equals(userId)) {
                reviews.add(review);
            }
        }
        return reviews;
    }

    public List<Review> getApprovedReviewsByMovieId(String movieId) {
        List<Review> reviews = new ArrayList<>();
        for (Review review : getReviewsByMovieId(movieId)) {
            if ("APPROVED".equals(review.getStatus())) {
                reviews.add(review);
            }
        }
        return reviews;
    }

    public Review getReviewById(String reviewId) {
        for (Review review : getAllReviews()) {
            if (review.getReviewId().equals(reviewId)) {
                return review;
            }
        }
        return null;
    }

    public void addReview(Review review) {
        FileUtils.appendLine(reviewFilePath, sanitizeReview(review).toString());
    }

    public void updateReview(Review review) {
        FileUtils.updateLine(reviewFilePath, review.getReviewId(), sanitizeReview(review).toString());
    }

    public void deleteReview(String reviewId) {
        FileUtils.deleteLine(reviewFilePath, reviewId);
    }

    public void approveReview(String reviewId) {
        Review review = getReviewById(reviewId);
        if (review != null) {
            review.setStatus("APPROVED");
            FileUtils.updateLine(reviewFilePath, reviewId, review.toString());
        }
    }

    public String generateReviewId() {
        int max = 0;
        for (Review review : getAllReviews()) {
            String id = review.getReviewId();
            if (id != null && id.startsWith("RV")) {
                try {
                    int value = Integer.parseInt(id.substring(2));
                    if (value > max) {
                        max = value;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("RV%03d", max + 1);
    }

    public boolean userAlreadyReviewed(String userId, String movieId) {
        for (Review review : getAllReviews()) {
            if (review.getUserId().equals(userId) && review.getMovieId().equals(movieId)) {
                return true;
            }
        }
        return false;
    }

    private Review sanitizeReview(Review review) {
        String safeComment = review.getComment() == null ? "" : review.getComment().replace(",", " ").trim();
        return new Review(
            review.getReviewId(),
            review.getUserId(),
            review.getUsername(),
            review.getMovieId(),
            review.getMovieTitle(),
            review.getRating(),
            safeComment,
            review.getStatus()
        );
    }
}
