<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Detail</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">MovieRental Admin</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#adminNav"
                aria-controls="adminNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="adminNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/movies/admin/list">Movie List</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/users/admin/list">User List</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/">Home</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h1 class="h4 mb-4">User Detail</h1>

                    <p class="mb-2"><strong>User ID:</strong> ${user.userId}</p>
                    <p class="mb-2"><strong>Username:</strong> ${user.username}</p>
                    <p class="mb-2"><strong>Email:</strong> ${user.email}</p>
                    <p class="mb-2"><strong>Phone:</strong> ${user.phone}</p>
                    <p class="mb-2"><strong>Role:</strong> ${user.role}</p>
                    <c:if test="${user.role == 'USER'}">
                        <p class="mb-2"><strong>Membership Type:</strong> ${user.membershipType}</p>
                    </c:if>
                    <c:if test="${user.role == 'ADMIN'}">
                        <p class="mb-2"><strong>Admin Level:</strong> ${user.adminLevel}</p>
                    </c:if>

                    <div class="d-flex gap-2 mt-3">
                        <a class="btn btn-danger"
                           href="${pageContext.request.contextPath}/users/admin/delete/${user.userId}">Delete User</a>
                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/users/admin/list">Back to List</a>
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
