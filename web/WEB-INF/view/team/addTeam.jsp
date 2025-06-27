<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Project Evaluation System - Add New Team</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Favicon -->
        <link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

        <!-- Google Web Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

        <!-- Icon Font Stylesheet -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Customized Bootstrap Stylesheet -->
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    </head>

    <body>
        <div class="container-fluid position-relative bg-white d-flex p-0">
            <!-- Spinner Start -->
            <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
                <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>
            <!-- Spinner End -->

            <!-- Sidebar Start -->
            <jsp:include page="/WEB-INF/view/includes/sidebar.jsp" />
            <!-- Sidebar End -->

            <!-- Content Start -->
            <div class="content">
                <!-- Navbar Start -->
                <jsp:include page="/WEB-INF/view/includes/navbar.jsp" />
                <!-- Navbar End -->

                <!-- Main Content Start -->
                <div class="container-fluid pt-4 px-4">
                    <div class="bg-light rounded p-4">
                        <h2 class="text-center mb-4">Add New Team</h2>

                        <!-- Display error messages, if any -->
                        <c:if test="${not empty Message}">
                            <div class="alert alert-info" role="alert">
                                ${Message}
                            </div>
                        </c:if>

                        <!-- Add Team Form -->
                        <form action="${pageContext.request.contextPath}/teams/add" method="post" class="row g-3">
                            <div class="col-md-6">
                                <label for="name" class="form-label">Team Name:</label>
                                <input type="text" id="name" name="name" class="form-control" required maxlength="45">
                            </div>

                            <div class="col-md-12">
                                <label for="detail" class="form-label">Detail:</label>
                                <textarea id="detail" name="detail" class="form-control" maxlength="255"></textarea>
                            </div>

                            <div class="col-md-6">
                                <label for="topic" class="form-label">Topic:</label>
                                <input type="text" id="topic" name="topic" class="form-control" maxlength="45">
                            </div>

                            <!-- Auto-select the current class -->
                            <div class="col-md-6">
                                <label for="className" class="form-label">Class:</label>
                                <!-- Hiển thị tên lớp mà không cần dropdown -->
                                <input type="text" class="form-control" value="${currentClassName}" readonly>
                                <!-- Trường ẩn để gửi classId -->
                                <input type="hidden" id="classId" name="classId" value="${sessionScope.selectedClassID}">
                            </div>

                            <div class="col-12">
                                <button type="submit" class="btn btn-primary">Add Team</button>
                            </div>
                        </form>

                        <p class="text-end mt-3">
                            <a href="${pageContext.request.contextPath}/class/configs?classID=${selectedClassID}" class="btn btn-secondary">Back to Team List</a>
                        </p>

                    </div>
                </div>
                <!-- Main Content End -->

                <!-- Footer Start -->
                <jsp:include page="/WEB-INF/view/includes/footer.jsp" />
                <!-- Footer End -->
            </div>
            <!-- Content End -->

            <!-- Back to Top -->
            <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
        </div>

        <!-- JavaScript Libraries -->
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </body>

</html>
