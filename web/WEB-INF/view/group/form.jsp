<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Group Details</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <!-- Favicon -->
    <link href="img/favicon.ico" rel="icon">
    
    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Icon Font Stylesheet -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
    
    <!-- Customized Bootstrap Stylesheet -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Template Stylesheet -->
    <link href="css/style.css" rel="stylesheet">
    
    <style>
        body {
            margin: 20px;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <div class="card shadow-sm">
            <div class="card-header">
                <h3>Group Details</h3>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/group/save" method="post">
                    <input type="hidden" name="id" value="${group != null ? group.id : ''}" />
                    
                    <!-- Code Field -->
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="code">Code</label>
                            <input type="text" class="form-control" id="code" name="code" value="${group.code}" required>
                        </div>
                    </div>
                    
                    <!-- Name and Type Fields -->
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="name">Name</label>
                            <input type="text" class="form-control" id="name" name="name" value="${group.name}" required>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="type">Type</label>
                            <select class="form-control" id="type" name="type">
                                <option value="Subject" ${group.type == 'Subject' ? 'selected' : ''}>Subject</option>
                                <option value="Department" ${group.type == 'Department' ? 'selected' : ''}>Department</option>
                            </select>
                        </div>
                    </div>
                    
                    <!-- Status Fields -->
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label>Status</label><br>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" id="active" name="status" value="Active" ${group.status == 1 ? 'checked' : ''}>
                                <label class="form-check-label" for="active">Active</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" id="inactive" name="status" value="Inactive" ${group.status == 0 ? 'checked' : ''}>
                                <label class="form-check-label" for="inactive">Inactive</label>
                            </div>
                        </div>
                    </div>

                    <!-- Detail Field -->
                    <div class="form-group">
                        <label for="detail">Details</label>
                        <textarea class="form-control" id="detail" name="detail" rows="3">${group.detail}</textarea>
                    </div>

                    <!-- Submit Button -->
                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    </div>

    <!-- JS and Bootstrap Scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.0.7/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
