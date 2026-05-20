<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app-ui.css"></head>
<body class="bg-light">
<%@ include file="/WEB-INF/views/common/admin-navbar.jsp" %>

<main class="container py-4">
    <h1 class="h3 mb-2">Admin Dashboard</h1>
    <p class="text-muted mb-4">Welcome, ${sessionScope.loggedInUser.username}</p>

    <div class="row g-4">
        <div class="col-md-3">
            <div class="card h-100 shadow rounded border-top border-4 border-primary">
                <div class="card-body d-flex flex-column">
                    <h2 class="h5">Manage Movies</h2>
                    <p class="text-muted flex-grow-1">Add, edit, and delete movies from the catalog</p>
                    <a class="btn btn-primary mt-auto" href="${pageContext.request.contextPath}/movies/admin/list">Go to Movies</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card h-100 shadow rounded border-top border-4 border-info">
                <div class="card-body d-flex flex-column">
                    <h2 class="h5">Add from TMDB</h2>
                    <p class="text-muted flex-grow-1">Browse TMDB and import movies to the catalog</p>
                    <a class="btn btn-info text-white mt-auto" href="${pageContext.request.contextPath}/movies/tmdb/popular">Browse TMDB</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card h-100 shadow rounded border-top border-4 border-success">
                <div class="card-body d-flex flex-column">
                    <h2 class="h5">Manage Users</h2>
                    <p class="text-muted flex-grow-1">View and manage registered users</p>
                    <a class="btn btn-success mt-auto" href="${pageContext.request.contextPath}/users/admin/list">Go to Users</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card h-100 shadow rounded border-top border-4 border-warning">
                <div class="card-body d-flex flex-column">
                    <h2 class="h5">Manage Rentals</h2>
                    <p class="text-muted flex-grow-1">View and manage all rentals</p>
                    <a class="btn btn-warning mt-auto" href="${pageContext.request.contextPath}/rentals/admin/list">Go to Rentals</a>
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

