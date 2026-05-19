<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TMDB Search Results</title>
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
    <h1 class="h3 mb-4">Search Results for: ${query}</h1>

    <form action="${pageContext.request.contextPath}/movies/tmdb/search" method="get" class="mb-4">
        <div class="input-group">
            <input type="text" name="query" class="form-control" value="${query}" placeholder="Search TMDB movies">
            <button type="submit" class="btn btn-primary">Search</button>
        </div>
    </form>

    <c:choose>
        <c:when test="${empty tmdbMovies}">
            <div class="alert alert-secondary">No results found.</div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <c:forEach items="${tmdbMovies}" var="tmdbMovie">
                    <div class="col-md-4">
                        <div class="card h-100 shadow-sm">
                            <c:if test="${not empty tmdbMovie.poster_path}">
                                <img src="${tmdbMovie.poster_path}" class="card-img-top" alt="${tmdbMovie.title}">
                            </c:if>
                            <div class="card-body d-flex flex-column">
                                <h2 class="h5 card-title">${tmdbMovie.title}</h2>
                                <p class="card-text mb-2"><strong>Release Date:</strong> ${tmdbMovie.release_date}</p>
                                <p class="card-text mb-4"><strong>Rating:</strong> ${tmdbMovie.vote_average}/10</p>
                                <a class="btn btn-outline-primary mt-auto"
                                   href="${pageContext.request.contextPath}/movies/tmdb/detail/${tmdbMovie.id}">View Details</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>
