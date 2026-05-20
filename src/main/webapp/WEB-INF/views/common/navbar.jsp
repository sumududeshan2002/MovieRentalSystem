<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">MovieRental</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav"
                aria-controls="mainNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/movies/tmdb/popular">Movies</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/rentals/my-rentals">My Rentals</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/reviews/my-reviews">Reviews</a></li>
                <c:choose>
                    <c:when test="${sessionScope.loggedInUser != null && sessionScope.loggedInUser.role == 'ADMIN'}">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users/logout">Logout</a></li>
                    </c:when>
                    <c:when test="${sessionScope.loggedInUser != null && sessionScope.loggedInUser.role == 'USER'}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/users/profile">
                                Profile
                            </a>
                        </li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users/logout">Logout</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users/login">Login</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/users/register">Register</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
