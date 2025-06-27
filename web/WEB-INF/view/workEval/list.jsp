<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>WorkEval List</title>
        <link rel="icon" href="img/favicon.ico">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        <!-- Include Toastify CSS and JS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
        <script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
        <!-- Include SweetAlert CSS and JS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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

                <!-- Table Section -->
                <div class="container-fluid pt-4 px-4">
                    <div class="table-responsive px-4">
                        <h1 class="mb-4">WorkEval List</h1>
                        <table class="table text-start align-middle table-bordered table-hover mb-0">
                            <thead class="thead-dark">
                                <tr>
                                    <th>ID</th>
                                    <th>Requirement Name</th>
                                    <th>Milestone Name</th>
                                    <th>Complexity Name</th>
                                    <th>Complexity Value</th>
                                    <th>Quality Name</th>
                                    <th>Quality Value</th>
                                    <th>Submit</th>
                                    <th>Grade</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="workEval" items="${workEvalList}">
                                    <tr>
                                        <td>${workEval.id}</td>
                                        <td>${workEval.reqName}</td>
                                        <td>${workEval.milestoneName}</td>
                                        <td>${workEval.complexityName}</td>
                                        <td>${workEval.complexityValue}</td>
                                        <td>${workEval.quanlityName}</td>
                                        <td>${workEval.qualityValue}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${workEval.isFinal}">
                                                    Submit
                                                </c:when>
                                                <c:otherwise>
                                                    Unsubmit
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${workEval.grade}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/workEval/update?id=${workEval.id}" class="btn btn-info btn-sm btn-edit">
                                                Evaluated
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty workEvalList}">
                                    <tr><td colspan="10" class="text-center">No data found.</td></tr>
                                </c:if>
                            </tbody>
                        </table>

                        <!-- Pagination Controls -->
                        <nav aria-label="Page navigation">
                            <ul class="pagination justify-content-center mt-4">
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link" href="${pageContext.request.contextPath}/workEval?page=${currentPage - 1}&size=${pageSize}" aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                </c:if>
                                <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                    <li class="page-item ${pageNum == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="${pageContext.request.contextPath}/workEval?page=${pageNum}&size=${pageSize}">
                                            ${pageNum}
                                        </a>
                                    </li>
                                </c:forEach>
                                <c:if test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link" href="${pageContext.request.contextPath}/workEval?page=${currentPage + 1}&size=${pageSize}" aria-label="Next">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                        </nav>
                    </div>

                    <!-- Footer -->
                    <jsp:include page="/WEB-INF/view/includes/footer.jsp"/>
                </div>

                <!-- Back to Top -->
                <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
            </div>
        </div>
        <!-- Modal Structure -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Edit WorkEval</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" id="modalContent">
                        <!-- Content will be loaded here via AJAX -->
                    </div>
                </div>
            </div>
        </div>
        <!-- Success Mess -->
         <c:if test="${not empty sessionScope.successMessage}">
                <script>
                    console.log("Error Message: ${sessionScope.successMessage}");
                    Swal.fire({
                        icon: 'success',
                        text: '${sessionScope.successMessage}',
                        toast: true,
                        position: 'top-right',
                        showConfirmButton: false,
                        showCloseButton: true,
                        timer: 5000,
                        timerProgressBar: true,
                        customClass: {
                            popup: 'swal-toast-position-below-navbar swal-error'
                        },
                        didOpen: (toast) => {
                            toast.addEventListener('mouseenter', Swal.stopTimer);
                            toast.addEventListener('mouseleave', Swal.resumeTimer);
                        }
                    });
                </script>
                <c:set var="successMessage" value="" scope="session"/> 
            </c:if>
        <!-- Include jQuery and Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
        $(document).ready(function () {
            $('.btn-edit').click(function (e) {
                e.preventDefault();
                var url = $(this).attr('href');

                // Load form content into the modal body using AJAX
                $.get(url, function (data) {
                    $('#modalContent').html(data);
                    $('#editModal').modal('show');
                }).fail(function () {
                    alert('Failed to load the form. Please try again.');
                });
            });
        });
        </script>
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </body>
</html>
