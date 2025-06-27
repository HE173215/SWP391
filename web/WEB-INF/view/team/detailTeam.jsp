<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Project Evaluation System - Team Details</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
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

                <!-- Main Content -->
                <div class="container-fluid pt-4 px-4">
                    <div class="bg-light rounded p-4">
                        <h2 class="text-center mb-4">Team Details</h2>

                        <!-- Display success or error messages -->
                        <c:if test="${not empty Message}">
                            <div class="alert alert-info" role="alert">
                                ${Message}
                            </div>
                            <c:set var="Message" value="" scope="session"/>
                        </c:if>

                        <!-- Update Team Information Form -->
                        <c:if test="${currentUser.role == 'Teacher' || currentUser.role == 'Admin'}">
                            <h4 class="mt-5">Update Team Information</h4>
                            <form action="${pageContext.request.contextPath}/teams/update" method="post" class="row g-3">
                                <input type="hidden" name="teamId" value="${team.id}" />
                                <div class="col-md-6">
                                    <label for="name" class="form-label">Name:</label>
                                    <input type="text" id="name" name="name" value="${team.name}" class="form-control" required />
                                </div>
                                <div class="col-md-6">
                                    <label for="detail" class="form-label">Detail:</label>
                                    <input type="text" id="detail" name="detail" value="${team.detail}" class="form-control" required />
                                </div>
                                <div class="col-md-6">
                                    <label for="topic" class="form-label">Topic:</label>
                                    <input type="text" id="topic" name="topic" value="${team.topic}" class="form-control" required />
                                </div>
                                <div class="col-md-6">
                                    <label for="className" class="form-label">Class:</label>
                                    <input type="text" class="form-control" value="${currentClassName}" readonly>
                                    <input type="hidden" id="classId" name="classId" value="${sessionScope.selectedClassID}">
                                </div>
                                <div class="col-12">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-pencil-alt"></i> Update
                                    </button>
                                </div>
                            </form>
                        </c:if>

                        <!-- Display Team Members -->
                        <h4 class="mb-4 text-center">Team Members</h4>
                        <c:choose>
                            <c:when test="${not empty teamMembers}">
                                <div class="table-responsive">
                                    <table class="table table-striped table-bordered text-center">
                                        <thead class="table-dark">
                                            <tr>
                                                <th>ID</th>
                                                <th>Username</th>
                                                <th>Full Name</th>
                                                <th>Email</th>
                                                <th>Mobile</th>
                                                <th>Role</th>
                                                    <c:if test="${currentUser.role == 'Teacher' || currentUser.role == 'Admin'}">
                                                    <th>Action</th>
                                                    </c:if>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="member" items="${teamMembers}">
                                                <tr>
                                                    <td>${member.id}</td>
                                                    <td>${member.userName}</td>
                                                    <td>${member.fullName}</td>
                                                    <td>${member.email}</td>
                                                    <td>${member.mobile}</td>
                                                    <td>${member.settingName}</td>
                                                    <c:if test="${currentUser.role == 'Teacher' || currentUser.role == 'Admin'}">
                                                        <td>
                                                            <form action="${pageContext.request.contextPath}/teams/removeMember" method="post" class="d-inline">
                                                                <input type="hidden" name="teamId" value="${team.id}" />
                                                                <input type="hidden" name="userEmail" value="${member.email}" />
                                                                <button type="submit" class="btn btn-danger btn-sm" title="Remove Member">
                                                                    <i class="fas fa-trash-alt"></i>
                                                                </button>
                                                            </form>
                                                            <form action="${pageContext.request.contextPath}/teams/setRole" method="post" class="d-inline">
                                                                <input type="hidden" name="teamId" value="${team.id}" />
                                                                <input type="hidden" name="userEmail" value="${member.email}" />
                                                                <button type="submit" class="btn btn-warning btn-sm" title="Promote to Leader">
                                                                    <i class="bi bi-star-fill"></i>
                                                                </button>
                                                            </form>
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info text-center" role="alert">
                                    No members found for this team.
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <!-- Pagination Controls -->
                        <c:if test="${totalPages > 1}">
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="?teamId=${team.id}&page=${currentPage - 1}" aria-label="Previous">
                                                <span aria-hidden="true">&laquo;</span>
                                            </a>
                                        </li>
                                    </c:if>
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item <c:if test='${i == currentPage}'>active</c:if>">
                                            <a class="page-link" href="?teamId=${team.id}&page=${i}">${i}</a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="?teamId=${team.id}&page=${currentPage + 1}" aria-label="Next">
                                                <span aria-hidden="true">&raquo;</span>
                                            </a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </c:if>


                        <!-- Add Members by Class Section -->
                        <h4 class="mt-5">Add Members</h4>
                        <c:choose>
                            <c:when test="${not empty classMembers}">
                                <form action="${pageContext.request.contextPath}/teams/addMember" method="post">
                                    <input type="hidden" name="teamId" value="${team.id}" />
                                    <table class="table table-striped table-bordered text-center">
                                        <thead class="table-dark">
                                            <tr>
                                                <th>Select</th>
                                                <th>ID</th>
                                                <th>Username</th>
                                                <th>Full Name</th>
                                                <th>Email</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="classMember" items="${classMembers}">
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" name="selectedMembers" value="${classMember.id}" />
                                                    </td>
                                                    <td>${classMember.id}</td>
                                                    <td>${classMember.userName}</td>
                                                    <td>${classMember.fullName}</td>
                                                    <td>${classMember.email}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                    <div class="col-12 mt-3">
                                        <button type="submit" class="btn btn-success">
                                            <i class="fas fa-user-plus"></i> Add Selected Members to Team
                                        </button>
                                    </div>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info" role="alert">
                                    No members found for this class.
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <p class="text-end mt-3">
                            <a href="${pageContext.request.contextPath}/class/configs?classID=${selectedClassID}" class="btn btn-secondary">
                                <i class="bi bi-arrow-left"></i> Back to Team List
                            </a>
                        </p>
                    </div>
                </div>

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
