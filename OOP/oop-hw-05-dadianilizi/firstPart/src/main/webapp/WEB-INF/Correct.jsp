<%--
  Created by IntelliJ IDEA.
  User: ttata
  Date: 6/29/2021
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<%--      <title>Welcome ${name}</title>--%>
      <title> Welcome <%= request.getAttribute("name")%></title>
</head>
<body>
<h1>Welcome <%= request.getAttribute("name")%></h1>
</body>
</html>
