<%--
  Created by IntelliJ IDEA.
  User: ttata
  Date: 6/29/2021
  Time: 7:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create new account</title>
</head>
<body>
 <h1> create new account</h1>
<h1>Please enter proposed name and password.</h1>
<p> Please log in.</p>
<form action="create" method="Post">
    <label for="Username">Username</label>
    <input type="text" name="Username" id="Username" > <br/>
    <label for="Password">Password</label>
    <input type="text" id ="Password" name="Password">
    <button type="submit">Login</button> <br/>

</form>
</body>
</html>
