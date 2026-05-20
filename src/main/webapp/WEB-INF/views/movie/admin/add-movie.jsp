<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Movie</title>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app-ui.css"></head>
<body class="bg-light">
<%@ include file="/WEB-INF/views/common/admin-navbar.jsp" %>

<main class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="bg-white border rounded p-4">
                <h1 class="h3 mb-4">Add New Movie</h1>
                <form action="${pageContext.request.contextPath}/movies/admin/add" method="post">
                    <div class="mb-3">
                        <label for="type" class="form-label">Movie Type</label>
                        <select id="type" name="type" class="form-select" onchange="toggleRentalPrice()" required>
                            <option value="NEW">NEW</option>
                            <option value="CLASSIC">CLASSIC</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <input id="title" type="text" name="title" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="genre" class="form-label">Genre</label>
                        <input id="genre" type="text" name="genre" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="director" class="form-label">Director</label>
                        <input id="director" type="text" name="director" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="year" class="form-label">Year</label>
                        <input id="year" type="number" name="year" class="form-control" required>
                    </div>
                    <div class="form-check mb-3">
                        <input id="available" type="checkbox" name="available" value="true" class="form-check-input">
                        <label for="available" class="form-check-label">Available</label>
                    </div>
                    <div class="mb-4" id="rentalPriceGroup">
                        <label for="rentalPrice" class="form-label">Rental Price</label>
                        <input id="rentalPrice" type="number" name="rentalPrice" step="0.01" class="form-control">
                    </div>
                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">Add Movie</button>
                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/movies/admin/list">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script>
    function toggleRentalPrice() {
        const typeSelect = document.getElementById('type');
        const rentalPriceGroup = document.getElementById('rentalPriceGroup');
        const rentalPriceInput = document.getElementById('rentalPrice');
        const isNew = typeSelect.value === 'NEW';

        rentalPriceGroup.style.display = isNew ? 'block' : 'none';
        rentalPriceInput.required = isNew;
        if (!isNew) {
            rentalPriceInput.value = '';
        }
    }

    toggleRentalPrice();
</script>
</body>
</html>
