<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${tmdbMovie.title}</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app-ui.css"></head>
<body class="bg-light">
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="container py-4">
    <c:if test="${not empty error}">
        <div class="alert alert-warning">${error}</div>
    </c:if>

    <div class="row g-4">
        <div class="col-md-4">
            <c:if test="${not empty tmdbMovie.poster_path}">
                <img src="${tmdbMovie.poster_path}" class="img-fluid rounded shadow-sm" alt="${tmdbMovie.title}">
            </c:if>
        </div>
        <div class="col-md-8">
            <div class="bg-white border rounded p-4 h-100">
                <h1 class="h3 mb-3">${tmdbMovie.title}</h1>
                <p class="mb-2"><strong>Release Date:</strong> ${tmdbMovie.release_date}</p>
                <p class="mb-2"><strong>Rating:</strong> ${tmdbMovie.vote_average}/10</p>
                <p class="mb-2"><strong>Runtime:</strong> ${tmdbMovie.runtime} minutes</p>
                <p class="mb-2"><strong>Genres:</strong> ${tmdbMovie.genres}</p>
                <p class="mb-4"><strong>Overview:</strong> ${tmdbMovie.overview}</p>
                <c:if test="${alreadyRented}">
                    <div class="alert alert-info">You already have an active rental for this movie.</div>
                </c:if>

                <c:choose>
                    <c:when test="${sessionScope.loggedInUser != null && sessionScope.loggedInUser.role == 'ADMIN'}">
                        <form action="${pageContext.request.contextPath}/movies/tmdb/import/${tmdbMovie.id}" method="post">
                            <div class="mb-3">
                                <label for="rentalPrice" class="form-label fw-bold">Set Rental Price ($)</label>
                                <input type="number" id="rentalPrice" name="rentalPrice"
                                       class="form-control" step="0.01" min="0.01"
                                       placeholder="e.g. 3.99" required>
                            </div>
                            <div class="mb-3">
                                <label for="type" class="form-label fw-bold">Movie Type</label>
                                <select id="type" name="type" class="form-select" required>
                                    <option value="NEW">New Release</option>
                                    <option value="CLASSIC">Classic</option>
                                </select>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-success">Add to Catalog</button>
                                <a href="${pageContext.request.contextPath}/movies/tmdb/popular"
                                   class="btn btn-outline-secondary">Back to Browse</a>
                            </div>
                        </form>
                    </c:when>

                    <c:when test="${sessionScope.loggedInUser != null && sessionScope.loggedInUser.role == 'USER'}">
                        <div class="d-flex gap-2 align-items-end">
                            <c:if test="${!alreadyRented}">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/rentals/rent/${tmdbMovie.id}"
                                      class="m-0">
                                    <input type="hidden" name="dailyRate" value="2.99">
                                    <input type="hidden" name="movieTitle" value="${tmdbMovie.title}">
                                    <div class="d-flex gap-2 align-items-end">
                                        <div>
                                            <label for="rentalDaysTmdb" class="form-label mb-1">Days</label>
                                            <input id="rentalDaysTmdb" name="rentalDays" type="number" min="1" max="30" value="7"
                                                   class="form-control" style="width: 110px;" required>
                                        </div>
                                        <div class="text-muted small mb-2">
                                            Daily: $2.99
                                        </div>
                                        <button type="submit" class="btn btn-primary">Rent This Movie</button>
                                    </div>
                                </form>
                            </c:if>
                            <button type="button" class="btn btn-outline-success" data-bs-toggle="modal"
                                    data-bs-target="#tmdbReviewModal">
                                Write Review
                            </button>
                            <a href="${pageContext.request.contextPath}/movies/tmdb/popular"
                               class="btn btn-outline-secondary">Back to Browse</a>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div class="d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/users/login"
                               class="btn btn-warning">Login to Rent</a>
                            <a href="${pageContext.request.contextPath}/movies/tmdb/popular"
                               class="btn btn-outline-secondary">Back to Browse</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<c:if test="${sessionScope.loggedInUser != null && sessionScope.loggedInUser.role == 'USER'}">
    <div class="modal fade" id="tmdbReviewModal" tabindex="-1" aria-labelledby="tmdbReviewModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h2 class="modal-title fs-5" id="tmdbReviewModalLabel">Write a Review</h2>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/reviews/add/${tmdbMovie.id}">
                    <div class="modal-body">
                        <input type="hidden" name="movieTitle" value="${tmdbMovie.title}">
                        <div class="mb-3">
                            <label for="tmdbModalRating" class="form-label">Rating</label>
                            <select id="tmdbModalRating" name="rating" class="form-select" required>
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="tmdbModalComment" class="form-label">Comment</label>
                            <textarea id="tmdbModalComment" name="comment" class="form-control" rows="4" required></textarea>
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
