<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Locale" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${movie.title}</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app-ui.css"></head>
<body class="bg-light">
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-7">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h1 class="h3 mb-4">${movie.title}</h1>
                    <div class="row g-3">
                        <div class="col-md-6"><strong>Title:</strong> ${movie.title}</div>
                        <div class="col-md-6"><strong>Genre:</strong> ${movie.genre}</div>
                        <div class="col-md-6"><strong>Director:</strong> ${movie.director}</div>
                        <div class="col-md-6"><strong>Year:</strong> ${movie.year}</div>
                        <div class="col-md-6"><strong>Type:</strong> ${movie.movieType}</div>
                        <div class="col-md-6">
                            <strong>Rental Price:</strong>
                            <%= String.format(Locale.US, "%.2f",
                                    ((com.se1020.movierental.model.Movie) request.getAttribute("movie")).getRentalPrice()) %>
                        </div>
                        <div class="col-md-6">
                            <strong>Availability:</strong>
                            <c:choose>
                                <c:when test="${movie.available}">
                                    <span class="badge bg-success">Available</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">Unavailable</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="mt-4 d-flex gap-2">
                        <c:if test="${sessionScope.loggedInUser != null && !alreadyRented}">
                            <form method="post" action="${pageContext.request.contextPath}/rentals/rent/${movie.movieId}" class="m-0">
                                <input type="hidden" name="dailyRate" value="${movie.rentalPrice}">
                                <div class="d-flex gap-2 align-items-end">
                                    <div>
                                        <label for="rentalDays" class="form-label mb-1">Days</label>
                                        <input id="rentalDays" name="rentalDays" type="number" min="1" max="30" value="7"
                                               class="form-control" style="width: 110px;" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Rent This Movie</button>
                                </div>
                            </form>
                        </c:if>
                        <c:if test="${sessionScope.loggedInUser != null}">
                            <button type="button" class="btn btn-outline-success" data-bs-toggle="modal"
                                    data-bs-target="#reviewModal">
                                Write Review
                            </button>
                        </c:if>
                        <a class="btn btn-outline-secondary"
                           href="${pageContext.request.contextPath}/movies/browse">Back to Browse</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<c:if test="${sessionScope.loggedInUser != null}">
    <div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h2 class="modal-title fs-5" id="reviewModalLabel">Write a Review</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/reviews/add/${movie.movieId}">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="modalRating" class="form-label">Rating</label>
                            <select id="modalRating" name="rating" class="form-select" required>
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="modalComment" class="form-label">Comment</label>
                            <textarea id="modalComment" name="comment" class="form-control" rows="4" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Submit Review</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</c:if>

<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>
