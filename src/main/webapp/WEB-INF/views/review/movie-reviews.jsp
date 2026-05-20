<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Movie Reviews</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
</head>
<body class="bg-light">
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="container py-4">
    <h1 class="h3 mb-3">Reviews for this Movie</h1>

    <c:if test="${param.error == 'already-reviewed'}">
        <div class="alert alert-warning">You have already reviewed this movie.</div>
    </c:if>

    <c:choose>
        <c:when test="${not empty reviews}">
            <div class="row g-3">
                <c:forEach var="review" items="${reviews}">
                    <div class="col-12">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <h2 class="h6 mb-2">${review.username}</h2>
                                <p class="mb-2 text-warning">
                                    <c:forEach begin="1" end="${review.rating}" var="star">&#9733;</c:forEach>
                                </p>
                                <p class="mb-0">${review.comment}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info mb-0">No reviews yet. Be the first!</div>
        </c:otherwise>
    </c:choose>

    <div class="mt-4 d-flex gap-2">
        <c:if test="${sessionScope.loggedInUser != null}">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/reviews/add/${movieId}">Write a Review</a>
        </c:if>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/movies/browse">Back to Browse</a>
    </div>
</main>

<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>
