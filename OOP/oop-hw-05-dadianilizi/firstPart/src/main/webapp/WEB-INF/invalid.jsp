<%--
  Created by IntelliJ IDEA.
  User: ttata
  Date: 6/29/2021
  Time: 6:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Information incorrect</title>
</head>
<body>

<h1>Please try again</h1>
<p> Either your user name or password  is incorrect .Please try again</p>
<form action="" method="Post">
    <label for="Username">Username</label>
    <input type="text" name="Username" id="Username" > <br/>
    <label for="Password">Password</label>
    <input type="text" id ="Password" name="Password">
    <button type="submit">Login</button> <br/>

    <a href="create">Create new Account </a>

</form>
</body>
</html>
