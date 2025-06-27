<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit WorkEval</title>
    <!-- Include Bootstrap for styling -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <form action="${pageContext.request.contextPath}/workEval/update" method="post" class="p-4 border rounded shadow-sm bg-light">
            <input type="hidden" name="id" value="${workEval.id}" />

            <!-- Req Name -->
            <div class="mb-3">
                <label for="reqId" class="form-label">Req:</label>
                <input type="text" id="reqName" value="${workEval.reqName}" class="form-control" readonly />
                <input type="hidden" id="reqId" name="reqId" value="${workEval.reqId}" />
            </div>

            <!-- Milestone (disabled) -->
            <div class="mb-3">
                <label for="milestoneId" class="form-label">Milestone:</label>
                <select id="milestoneId" name="milestoneId" class="form-select" disabled>
                    <c:forEach var="milestone" items="${milestones}">
                        <option value="${milestone.id}" ${milestone.id == workEval.milestoneId ? 'selected' : ''}>
                            ${milestone.name}
                        </option>
                    </c:forEach>
                </select>
                <input type="hidden" name="milestoneId" value="${workEval.milestoneId}" />
            </div>

            <!-- Complexity -->
            <div class="mb-3">
                <label for="complexityId" class="form-label">Complexity:</label>
                <select id="complexityId" name="complexityId" class="form-select" required>
                    <c:forEach var="complexity" items="${complexities}">
                        <option value="${complexity.id}" ${complexity.id == workEval.complexityId ? 'selected' : ''}>
                            ${complexity.name}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <!-- Quality -->
            <div class="mb-3">
                <label for="qualityId" class="form-label">Quality:</label>
                <select id="qualityId" name="qualityId" class="form-select" required>
                    <c:forEach var="quality" items="${qualities}">
                        <option value="${quality.id}" ${quality.id == workEval.qualityId ? 'selected' : ''}>
                            ${quality.name}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <!-- Is Final -->
            <div class="mb-3 form-check">
                <input type="checkbox" id="isFinal" name="isFinal" class="form-check-input" value="true" ${workEval.isFinal ? 'checked' : ''} />
                <label for="isFinal" class="form-check-label">Submit</label>
            </div>

            <!-- Buttons -->
            <div class="d-flex justify-content-between">
                <button type="submit" class="btn btn-primary">Update</button>
                <a href="${pageContext.request.contextPath}/workEval" class="btn btn-secondary">Back to list</a>
            </div>
        </form>
    </div>

    <!-- Optional Bootstrap JS and Popper.js (if needed) -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"></script>
</body>
</html>
