<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Group</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <form role="form" class="form-horizontal" action="/profession/add" method="post"> <!-- на end-point /profession/add Post-запросом прилетает -->
        <div class="form-group"><h3>New Profession</h3></div> <!-- название контейнера -->
        <div class="form-group"><input type="text" class="form-control" name="name" placeholder="Name"></div> <!-- вводим имя профессии -->
        <div class="form-group"><input type="submit" class="btn btn-primary" value="Add"></div> <!-- кнопкак для добавления с названием -->
    </form>
</div>
</body>
</html>