<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Final Evaluation List of Teams</title>
        <link rel="icon" href="img/favicon.ico">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    </head>
    <body>
        <div class="container-fluid position-relative bg-white d-flex p-0">
            <!-- Spinner -->
            <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
                <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>
            <!-- Sidebar -->
            <jsp:include page="/WEB-INF/view/includes/sidebar.jsp" />

            <!-- Content -->
            <div class="content">
                <!-- Navbar -->
                <jsp:include page="/WEB-INF/view/includes/navbar.jsp" />

                <!-- Search Form -->
                <div class="container-fluid pt-4 px-4">
                    <form action="${pageContext.request.contextPath}/finalEvaluated/search" method="get" class="row g-2 align-items-center mb-4">
                        <div class="table-responsive px-4">
                            <input type="text" name="teamName" class="form-control" placeholder="Search by team name" required />
                        </div>
                        <div class="table-responsive px-4">
                            <button type="submit" class="btn btn-primary mb-3">Search</button>
                        </div>
                    </form>
                </div>

                <div class="container-fluid pt-4 px-4">
                    <!-- Table -->
                    <div class="table-responsive px-4">
                        <table class="table text-start align-middle table-bordered table-hover mb-0">
                            <thead class="thead-dark">
                                <tr>
                                    <th>Team Name</th>
                                    <th>Milestone Name</th>
                                    <th>Requirement ID</th>
                                    <th>Complexity Value</th>
                                    <th>Quality Value</th>
                                    <th>Milestone Weight</th>
                                    <th>Details</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="evaluation" items="${finalEvaluatedList}">
                                    <tr>
                                        <td>${evaluation.teamName}</td>
                                        <td>${evaluation.milestoneName}</td>
                                        <td>${evaluation.requirementId}</td>
                                        <td>${evaluation.complexityValue}</td>
                                        <td>${evaluation.qualityValue}%</td>
                                        <td>${evaluation.milestoneWeight}%</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/finalEvaluated/view?id=${evaluation.teamId}" class="btn btn-info btn-sm">View Details</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty finalEvaluatedList}">
                                    <tr><td colspan="7" class="text-center">No teams found.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <!-- Footer -->
                    <jsp:include page="/WEB-INF/view/includes/footer.jsp"/>
                </div>

                <!-- Back to Top -->
                <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
            </div>
        </div>

        <!-- Scripts -->
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </body>
</html>
