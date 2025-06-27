<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Final Evaluation Details</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger mt-3">${errorMessage}</div>
    </c:if>

    <!-- Team Overview -->
    <div class="card mt-4 shadow-sm">
        <div class="card-body">
            <h5 class="card-title">Final Evaluation for Team: ${finalEvaluated.teamName}</h5>
            <table class="table table-bordered">
                <tr>
                    <th>Team Name</th>
                    <td>${finalEvaluated.teamName}</td>
                </tr>
                <tr>
                    <th>Milestone Name</th>
                    <td>${finalEvaluated.milestoneName}</td>
                </tr>
                <tr>
                    <th>Requirement ID</th>
                    <td>${finalEvaluated.requirementId}</td>
                </tr>
                <tr>
                    <th>Complexity Value</th>
                    <td>${finalEvaluated.complexityValue}</td>
                </tr>
                <tr>
                    <th>Quality Value</th>
                    <td>${finalEvaluated.qualityValue}%</td>
                </tr>
                <tr>
                    <th>Milestone Weight</th>
                    <td>${finalEvaluated.milestoneWeight}%</td>
                </tr>
            </table>
        </div>
    </div>

    <!-- Back Button -->
    <div class="mt-4">
        <a href="${pageContext.request.contextPath}/finalEvaluated" class="btn btn-primary">Back to List</a>
    </div>
</div>

<!-- JS and Bootstrap Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.0.7/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
