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
                    <div class="mt-4 d-flex gap-2 align-items-end">
                        <c:if test="${sessionScope.loggedInUser != null && !alreadyRented}">
                            <form method="post" action="${pageContext.request.contextPath}/rentals/rent/${movie.movieId}" class="m-0">
                                <input type="hidden" name="dailyRate" value="${movie.rentalPrice}">
                                <div class="d-flex gap-2 align-items-end">
                                    <div>
                                        <label for="rentalDays" class="form-label mb-1">Days</label>
                                        <input id="rentalDays" name="rentalDays" type="number" min="1" max="30" value="7"
                                               class="form-control" style="width: 110px;" required>
                                    </div>
                                    <div class="text-muted small mb-2">
                                        Daily: $${movie.rentalPrice}
                                    </div>
                                    <button type="submit" class="btn btn-primary">Rent This Movie</button>
                                </div>
                            </form>
                        </c:if>
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
