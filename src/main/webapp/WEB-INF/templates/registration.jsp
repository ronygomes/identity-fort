<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <title>Identity Fort | Registration</title>
    <link rel="stylesheet" href="webjars/bootstrap/4.5.0/css/bootstrap.css">
</head>
<body>

<div class="container">
    <form:form class="form-signin" modelAttribute="user">
        <h2 class="form-signin-heading">Identity Fort</h2>

        <div class="row col-4">
            <label for="firstName" class="sr-only">First Name</label>
            <form:input path="firstName" cssClass="form-control" placeholder="First Name" required="required"/>
            <form:errors path="firstName"/>
        </div>

        <div class="row col-4">
            <label for="lastName" class="sr-only">Last Name</label>
            <input type="text" id="lastName" name="lastName"
            <form:input path="lastName" cssClass="form-control" placeholder="Last Name" required="required"/>
            <form:errors path="lastName"/>
        </div>

        <div class="row col-4">
            <label for="email" class="sr-only">Email</label>
            <form:input path="email" cssClass="form-control" placeholder="Email" required="required"/>
            <form:errors path="email"/>
        </div>

        <div class="row col-4">
            <label for="rawPassword" class="sr-only">Password</label>
            <form:password path="rawPassword" cssClass="form-control" placeholder="Password" required="required"/>
            <form:errors path="rawPassword"/>
        </div>

        <div class="row col-4">
            <label for="confirmPassword" class="sr-only">Confirm Password</label>
            <form:password path="confirmPassword" cssClass="form-control" placeholder="Confirm Password"
                           required="required"/>
            <form:errors path="confirmPassword"/>
        </div>

        <sec:csrfInput/>
        <button class="btn btn-lg btn-primary btn-block row offset-1 col-2"
                type="submit">Sign in
        </button>
    </form:form>
</div>


</body>

