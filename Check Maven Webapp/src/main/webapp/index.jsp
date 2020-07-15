<%@ page language="java" contentType="text/html; charset=utf-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- <%@ page contentType="text/html; charset=utf-8"%> --%>
<html>
<head>
	<meta charset="utf-8">
    <title>Major Maths Question Bank</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/css/reset.css">
    <link rel="stylesheet" href="${ctx}/css/supersized.css">
    <link rel="stylesheet" href="${ctx}/css/userlogin.css">
	<script type="text/javascript">
		console.log("MMQB");
	</script>
</head>
<body>
	<div class="page-container">
		<h1>Hello!</h1>
		<br />
		<p>I am a...</p>
		<button onclick = "window.location.href = 'toLogin.action';">Student</button>
		<button onclick = "window.location.href = 'admin/login.action';">Admin</button>
	</div>
	
	<script src="${ctx}/js/jquery-1.8.2.min.js"></script>
	<script src="${ctx}/js/supersized.3.2.7.min.js"></script>
    <script src="${ctx}/js/supersized-init.js"></script>
</body>
</html>
