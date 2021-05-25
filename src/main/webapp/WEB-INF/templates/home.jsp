<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>Test</title>
    <link rel="stylesheet" href="webjars/bootstrap/4.5.0/css/bootstrap.css">
</head>
<body>

Hello World! Enter `name` request param

<h1><c:out value="${param.name}"/></h1>


<c:choose>
    <c:when test="${param.data == 2}">
        <h3>Data is 2</h3>
    </c:when>
    <c:otherwise>
        Data is not given
    </c:otherwise>
</c:choose>

</body>

