<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Movie Rental and Review Platform</title>
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous">
</head>
<body class="bg-light">
    <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

    <main class="container py-5">
        <section class="py-5 text-center">
            <h1 class="display-5 fw-bold">Movie Rental and Review Platform</h1>
            <p class="lead text-muted mt-3 mb-4">
                Rent movies, track your watchlist, and share reviews in one simple place.
            </p>
            <div class="d-flex justify-content-center gap-3">
                <a href="${pageContext.request.contextPath}/movies/tmdb/popular" class="btn btn-primary btn-lg">Browse Movies</a>
                <a href="${pageContext.request.contextPath}/users/register" class="btn btn-outline-secondary btn-lg">Register</a>
            </div>
        </section>

        <section class="row mt-5 g-4 text-center">
            <div class="col-md-4">
                <div class="card h-100 p-4 border-0 shadow-sm">
                    <h5 class="fw-bold">🎬 Browse Movies</h5>
                    <p class="text-muted">Explore thousands of movies powered by TMDB.</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 p-4 border-0 shadow-sm">
                    <h5 class="fw-bold">ðŸ“¦ Rent & Return</h5>
                    <p class="text-muted">Rent movies and manage your rental history easily.</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 p-4 border-0 shadow-sm">
                    <h5 class="fw-bold">⭐ Reviews</h5>
                    <p class="text-muted">Share your thoughts and read reviews from others.</p>
                </div>
            </div>
        </section>
    </main>

    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>


