<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app-ui.css"></head>
<body class="bg-light">
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h1 class="h4 mb-4">My Profile</h1>

                    <p class="mb-2"><strong>Username:</strong> ${user.username}</p>
                    <p class="mb-2"><strong>Email:</strong> ${user.email}</p>
                    <p class="mb-2"><strong>Phone:</strong> ${user.phone}</p>
                    <p class="mb-2"><strong>Membership Type:</strong> ${user.membershipType}</p>
                    <p class="mb-4"><strong>Role:</strong> ${user.role}</p>

                    <div class="d-flex gap-2">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/users/edit-profile">Edit Profile</a>
                        <a class="btn btn-danger" href="${pageContext.request.contextPath}/users/delete-account"
                           onclick="return confirm('Are you sure you want to delete your account?')">Delete Account</a>
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
