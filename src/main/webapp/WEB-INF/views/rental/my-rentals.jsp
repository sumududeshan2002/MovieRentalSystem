<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Rentals</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
</head>
<body class="bg-light">
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="container py-4">
    <h1 class="h3 mb-3">My Rentals</h1>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped table-hover mb-0 align-middle">
                    <thead class="table-dark">
                    <tr>
                        <th>Movie Title</th>
                        <th>Rental Date</th>
                        <th>Due Date</th>
                        <th>Days</th>
                        <th>Daily Rate</th>
                        <th>Total Price</th>
                        <th>Return Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty rentals}">
                            <c:forEach var="rental" items="${rentals}">
                                <tr>
                                    <td>${rental.movieTitle}</td>
                                    <td>${rental.rentalDate}</td>
                                    <td>${rental.dueDate}</td>
                                    <td>${rental.rentalDays}</td>
                                    <td>$${rental.dailyRate}</td>
                                    <td>$${rental.totalPrice}</td>
                                    <td>${rental.returnDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${rental.status == 'RETURNED'}">
                                                <span class="badge bg-success">RETURNED</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge text-dark bg-warning">ACTIVE</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="d-flex gap-2">
                                        <a class="btn btn-sm btn-outline-primary"
                                           href="${pageContext.request.contextPath}/rentals/detail/${rental.rentalId}">
                                            Details
                                        </a>
                                        <c:if test="${rental.status == 'ACTIVE'}">
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/rentals/return/${rental.rentalId}"
                                                  class="m-0">
                                                <button type="submit" class="btn btn-sm btn-danger">Return</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="9" class="text-center py-4">No rentals found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
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
