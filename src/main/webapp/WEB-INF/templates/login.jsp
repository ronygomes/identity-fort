<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<head>
    <title>Identity Fort | Login</title>
    <link rel="stylesheet" href="webjars/bootstrap/4.5.0/css/bootstrap.css">
</head>
<body>

<c:choose>
    <c:when test="${param.error != null}">
        <div class="alert-danger">
            Invalid Email or Password
        </div>
    </c:when>

    <c:when test="${param.logout != null}">
        <div class="alert-success">
            You have successfully Logged out
        </div>
    </c:when>

    <c:when test="${not empty successRedirectMessage}">
        <div class="alert-success">
            <c:out value="${successRedirectMessage}"/>
        </div>
    </c:when>

    <c:when test="${not empty errorRedirectMessage}">
        <div class="alert-danger">
            <c:out value="${errorRedirectMessage}"/>
        </div>
    </c:when>
</c:choose>

<div class="container">
    <form class="form-signin" method="post" action="<c:url value="/login"/>">
        <h2 class="form-signin-heading">Identity Fort</h2>

        <div class="row col-4">
            <label for="email" class="sr-only">Email</label>
            <input type="text" id="email" name="email"
                   class="form-control" placeholder="Email" required autofocus>
        </div>

        <div class="row col-4">
            <label for="password" class="sr-only">Password</label>
            <input type="password" id="password" name="password"
                   class="form-control" placeholder="Password" required>
        </div>

        <div>
            <label>
                <input type="checkbox" name="remember-me"/> Remember Me
            </label>
        </div>

        <sec:csrfInput/>
        <button class="btn btn-lg btn-primary btn-block row offset-1 col-2"
                type="submit">Sign in</button>
    </form>
    <a href='<c:out value="/register"/>'>Register</a>
</div>


</body>

