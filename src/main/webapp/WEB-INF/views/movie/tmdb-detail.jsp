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
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">MovieRental</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#tmdbNav"
                aria-controls="tmdbNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="tmdbNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/movies/tmdb/popular">TMDB Browse</a></li>
                <li class="nav-item"><a class="nav-link" href="#">My Rentals</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Reviews</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Login</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-4">
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

                <div class="d-flex flex-wrap gap-2">
                    <form action="${pageContext.request.contextPath}/movies/tmdb/import/${tmdbMovie.id}" method="post" class="mb-0">
                        <input type="hidden" name="type" value="NEW">
                        <button type="submit" class="btn btn-primary">Import as New Release</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/movies/tmdb/import/${tmdbMovie.id}" method="post" class="mb-0">
                        <input type="hidden" name="type" value="CLASSIC">
                        <button type="submit" class="btn btn-outline-primary">Import as Classic</button>
                    </form>
                    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/movies/tmdb/popular">Back to TMDB Browse</a>
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
