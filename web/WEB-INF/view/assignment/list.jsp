<%-- 
    Document   : configs
    Created on : Oct 9, 2024, 7:04:37 PM
    Author     : Do Duan
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
    <!--Eval Asignment -->
    <div class="tab-pane fade ${param.tab == 'EvalAssignment' ? 'show active' : ''}" id="evalassignment" role="tabpanel">
            <div class="d-flex me-3" style="padding: 10px">
                <div class="col-md-4" style="margin-left: 40px; ">
                    <div class="input-group">
                        <h4>Eval Assignment</h4>
                    </div>
                </div>
                <!-- Add Button -->
                <div class="col-md-3 text-md-end" style="margin-left:35%">
                    <button type="button" class="btn btn-primary" onclick="window.location.href = '${pageContext.request.contextPath}/assignment/add'">
                        <i class="fas fa-plus"></i>
                    </button>
                </div>
            </div>
            <c:if test="${not empty assignmentList}"> 
            <!-- Assigment List Table -->
            <div class="table-responsive mt-3" style="margin-left: 30px; margin-right: 30px">
                <table class="table table-striped table-hover align-middle table-bordered mb-0">
                    <thead>
                        <tr class="text-dark">
                            <th scope="col">ID</th>
                            <th scope="col">Name</th>                    
                            <th scope="col">Weight</th>
                            <th scope="col">Final Assignment </th>
                            <th scope="col">Code</th>
                            <th scope="col">Status</th>
                            <th scope="col">Action</th>
                        </tr>

                    </thead>
                    <tbody>
                        <c:forEach var="assignment" items="${assignmentList}">
                            <tr>
                                <td>${assignment.id}</td>
                                <td>${assignment.name}</td>
                                <td>${assignment.weight}</td>
                                <td>${assignment.final_assignment ? 'Yes' : 'No'}</td>
                                <td>${assignment.subject_code}</td>
                                <td><c:out value="${assignment.status ? 'Active' : 'Inactive'}" /></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/assignment/edit?id=${assignment.id}">
                                        <button type="button" class="btn btn-link"><i class="fas fa-edit"></i></button>
                                    </a>
                                    <c:choose>
                                        <c:when test="${assignment.status}">
                                            <button type="button" class="btn btn-link text-warning" onclick="confirmAction('${pageContext.request.contextPath}/assignment/status?id=${assignment.id}&status=false&subject_id=${assignment.subject_id}', 'Deactivate')">
                                               <i class="fas fa-ban"></i>
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button" class="btn btn-link text-success" onclick="confirmAction('${pageContext.request.contextPath}/assignment/status?id=${assignment.id}&status=true&subject_id=${assignment.subject_id}', 'Activate')">
                                                <i class="fas fa-check"></i>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                        <!-- Error and Success Alerts -->
                        <c:if test="${not empty sessionScope.ErrorMessage}">
                        <script>
                            console.log("Error Message: ${sessionScope.ErrorMessage}");
                            Swal.fire({
                                icon: 'error',
                                text: '${sessionScope.ErrorMessage}',
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
                        <c:set var="ErrorMessage" value="" scope="session"/> 
                    </c:if>

                    <c:if test="${not empty sessionScope.Message}">
                        <script>
                            Swal.fire({
                                icon: 'success',
                                text: '${sessionScope.Message}',
                                toast: true,
                                position: 'top-right',
                                showConfirmButton: false,
                                showCloseButton: true,
                                timer: 5000,
                                timerProgressBar: true,
                                customClass: {
                                    popup: 'swal-toast-position-below-navbar swal-success'
                                },
                                didOpen: (toast) => {
                                    toast.addEventListener('mouseenter', Swal.stopTimer);
                                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                                }
                            });
                            ${sessionScope.Message = null}
                        </script>
                    </c:if>
                    <script>
                        function confirmAction(url, action) {
                            Swal.fire({
                                title: 'Are you sure?',
                                text: 'Do you want to ' + action + ' this user?',
                                icon: 'question',
                                showCancelButton: true,
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                confirmButtonText: 'Yes, ' + action + ' it!',
                                cancelButtonText: 'No'
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    window.location.href = url;
                                }
                            });
                        }

                    </script>
                    </tbody>
                </table>

            </div>
        </c:if>
        <c:if test="${empty assignmentList}">
            <p style="padding: 10px; margin-left: 30px">No assignment found.</p>
        </c:if>

    </div>

</html>
