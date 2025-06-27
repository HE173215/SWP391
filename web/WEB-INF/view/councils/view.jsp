<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
    .form-control[readonly],
    .form-select:disabled,
    .form-check-input:disabled {
        background-color: #ffffff;
        color: #212529; 
        opacity: 1; 
        border-color: #ced4da; 
        pointer-events: none; 
    }

    
    .form-check-input[disabled] {
        opacity: 1; 
        pointer-events: none;
    }

    .form-check-input:checked[disabled] {
        background-color: #0d6efd; 
        border-color: #0d6efd;
    }
</style>


<div class="container-fluid">
    <c:if test="${not empty council}">
        <input type="hidden" name="id" value="${council.id}">
    </c:if>

    <!-- Council Name Field -->
    <div class="mb-3">
        <label for="name" class="form-label">Council Name</label>
        <input type="text" class="form-control" id="name" name="name" 
               value="${council != null ? council.name : ''}" placeholder="" readonly>
    </div>

    <!-- Description Field-->
    <div class="mb-3">
        <label for="description" class="form-label">Description</label>
        <textarea class="form-control" id="description" name="description" 
                  rows="3" readonly>${council != null ? council.description : ''}</textarea>
    </div>

    <!-- Class Selection Field -->
    <div class="mb-3">
        <label for="class_id" class="form-label">Class</label>
        <select class="form-select" id="class_id" name="class_id" disabled>
            <option value="" disabled ${classes == null ? 'selected' : ''}>Select a class</option>
            <c:forEach items="${classes}" var="c">
                <option value="${c.classId}" 
                        ${council != null && c.classId == council.classId ? 'selected' : ''}>
                    ${c.className}
                </option>
            </c:forEach>
        </select>
    </div>

    <!-- Create Date Field -->
    <div class="mb-3">
        <label for="createDate" class="form-label">Date</label>
        <input type="date" class="form-control" id="createDate" name="createDate"
               value="${council != null ? council.createdDate : ''}" readonly>
    </div>

    <!-- Status Field -->
    <div class="col d-flex align-items-center">
        <label class="form-label me-4">Status:</label>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="active" name="status" value="1" 
                   ${council != null && council.status ? 'checked' : ''} disabled>
            <label class="form-check-label" for="active">Active</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="inactive" name="status" value="0" 
                   ${council != null && !council.status ? 'checked' : ''} disabled>
            <label class="form-check-label" for="inactive">Inactive</label>
        </div>
    </div>

</div>
