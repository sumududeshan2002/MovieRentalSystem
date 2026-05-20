<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rental Detail</title>
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
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h1 class="h3 mb-4">Rental Detail</h1>

                    <div class="row g-3">
                        <div class="col-md-6"><strong>Rental ID:</strong> ${rental.rentalId}</div>
                        <div class="col-md-6"><strong>User ID:</strong> ${rental.userId}</div>
                        <div class="col-md-6"><strong>Movie ID:</strong> ${rental.movieId}</div>
                        <div class="col-md-6"><strong>Movie Title:</strong> ${rental.movieTitle}</div>
                        <div class="col-md-6"><strong>Rental Date:</strong> ${rental.rentalDate}</div>
                        <div class="col-md-6"><strong>Due Date:</strong> ${rental.dueDate}</div>
                        <div class="col-md-6"><strong>Return Date:</strong> ${rental.returnDate}</div>
                        <div class="col-md-6"><strong>Status:</strong> ${rental.status}</div>
                    </div>

                    <div class="mt-4">
                        <a href="${pageContext.request.contextPath}/rentals/my-rentals"
                           class="btn btn-outline-secondary">Back to My Rentals</a>
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
