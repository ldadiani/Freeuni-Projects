<%--
  Created by IntelliJ IDEA.
  User: ttata
  Date: 6/29/2021
  Time: 7:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Create new account</title>
</head>
<body>

<h1>The name <%= request.getAttribute("name")%> is already in use.</h1>
<p> Please enter another name and password.</p>
<form action="create" method="Post">
  <label for="Username">Username</label>
  <input type="text" name="Username" id="Username" > <br/>
  <label for="Password">Password</label>
  <input type="text" id ="Password" name="Password">
  <button type="submit">Login</button> <br/>
</form>
</body>
</html>
