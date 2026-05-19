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
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">MovieRental</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#userNav"
                aria-controls="userNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="userNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/movies/browse">Browse Movies</a></li>
                <li class="nav-item"><a class="nav-link" href="#">My Rentals</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Reviews</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Login</a></li>
            </ul>
        </div>
    </div>
</nav>

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
                        <div class="col-md-6"><strong>Type:</strong> ${movie.class.simpleName}</div>
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
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/rentals/rent/${movie.movieId}">Rent This Movie</a>
                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/movies/browse">Back to Browse</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>
