<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Movies</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/movies/admin/list">MovieRental Admin</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#adminNav"
                aria-controls="adminNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="adminNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/movies/admin/list">Movie List</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/movies/admin/add">Add Movie</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/">Home</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1 class="h3 mb-0">Manage Movies</h1>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/movies/admin/add">Add New Movie</a>
    </div>

    <div class="table-responsive bg-white border rounded">
        <table class="table table-striped table-hover mb-0">
            <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Genre</th>
                <th>Director</th>
                <th>Year</th>
                <th>Type</th>
                <th>Price</th>
                <th>Available</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${movies}" var="movie">
                <tr>
                    <td>${movie.movieId}</td>
                    <td>${movie.title}</td>
                    <td>${movie.genre}</td>
                    <td>${movie.director}</td>
                    <td>${movie.year}</td>
                    <td>${movie.class.simpleName}</td>
                    <td>${movie.rentalPrice}</td>
                    <td>${movie.available}</td>
                    <td>
                        <a class="btn btn-sm btn-outline-primary"
                           href="${pageContext.request.contextPath}/movies/admin/edit/${movie.movieId}">Edit</a>
                        <a class="btn btn-sm btn-danger"
                           href="${pageContext.request.contextPath}/movies/admin/delete/${movie.movieId}">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>
