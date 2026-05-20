<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit User</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app-ui.css">
</head>
<body class="bg-light">
<%@ include file="/WEB-INF/views/common/admin-navbar.jsp" %>

<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h1 class="h4 mb-4">Edit User</h1>
                    <c:if test="${not empty error}">
                        <div class="alert alert-warning">${error}</div>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/users/admin/edit/${user.userId}">
                        <div class="mb-3">
                            <label class="form-label">Username</label>
                            <input type="text" class="form-control" value="${user.username}" disabled>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="email" class="form-control" value="${user.email}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Phone</label>
                            <input type="text" name="phone" class="form-control" value="${user.phone}" required>
                        </div>

                        <c:if test="${user.role == 'USER'}">
                            <div class="mb-3">
                                <label class="form-label">Membership Type</label>
                                <select name="membershipType" class="form-select" required>
                                    <option value="BASIC" ${user.membershipType == 'BASIC' ? 'selected' : ''}>BASIC</option>
                                    <option value="PREMIUM" ${user.membershipType == 'PREMIUM' ? 'selected' : ''}>PREMIUM</option>
                                </select>
                            </div>
                        </c:if>

                        <c:if test="${user.role == 'ADMIN'}">
                            <div class="mb-3">
                                <label class="form-label">Admin Level</label>
                                <input type="number" name="adminLevel" min="1" class="form-control" value="${user.adminLevel}" required>
                            </div>
                        </c:if>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/users/admin/list">Cancel</a>
                        </div>
                    </form>
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
