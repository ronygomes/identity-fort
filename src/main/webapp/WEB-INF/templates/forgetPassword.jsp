<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<head>
    <title>Identity Fort | Forget Password</title>
    <link rel="stylesheet" href="webjars/bootstrap/4.5.0/css/bootstrap.css">
</head>
<body>

<c:if test="${not empty errorMessage}">
    <div class="alert-danger">
        <c:out value="${errorMessage}"/>
    </div>
</c:if>

<div class="container">
    <form class="form-signin" method="post">
        <h2 class="form-signin-heading">Identity Fort</h2>

        <div class="row col-4">
            <label for="email" class="sr-only">Email</label>
            <input type="text" id="email" name="email"
                   class="form-control" placeholder="Email" required autofocus>
        </div>

        <sec:csrfInput/>
        <button class="btn btn-lg btn-primary btn-block row offset-1 col-2"
                type="submit">Reset Password
        </button>
    </form>
    <a href='<c:out value="/login"/>'>Go Back to Login Page</a>
</div>
</body>