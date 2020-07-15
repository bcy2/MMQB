<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en" class="no-js">
<head>
<meta charset="utf-8">
<title>Major Maths Question Bank</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="stylesheet" href="${ctx}/css/reset.css">
<link rel="stylesheet" href="${ctx}/css/supersized.css">
<link rel="stylesheet" href="${ctx}/css/userlogin.css">
<style type="text/css">
	#myform label,select {
		margin-top: 10px;
		margin-bottom: 5px;
		height: 20px;
	}
	#myform select {
		padding: 0 10px;
		margin-left: 10px;
	}
</style>
<script src="${ctx}/js/jquery-1.8.2.min.js"></script>
<script src="${ctx}/js/supersized.3.2.7.min.js"></script>
<script src="${ctx}/js/supersized-init.js"></script>
<script src="${ctx}/js/scripts.js"></script>
<script src="${ctx}/js/reg.js"></script>
</head>
<body>
    <div class="page-container">
        <h1>Sign up for an account</h1>
        <form action="${ctx}/toIndex.action" method="post" name="myform" id="myform">
            <input type="text" name="userId" id="userId" class="username" placeholder="Username" onblur="checkUserId('${ctx}')"><br><span style="color: red" id="tipInfo">${message }</span><br>
            <input type="text" name="userFirstName" id="userFirstName" placeholder="Your first Name">
            <input type="text" name="userLastName" id="userLastName" placeholder="Your last name">
            <input type="text" name="parentName" id="parentName" placeholder="Parent/Guardian name">
            <input type="password" name="Pwd" id="Pwd" class="password" placeholder="Login password" onblur="checkPwd()">
            <input type="password" name="userPwd" id="userPwd" class="password" placeholder="Confirm password"><br/>
            
            <label for="curriculum">Curriculum:</label>
            <select name="curriculum" id="curriculum">
            	<option value="">Please choose...</option>
	            <c:forEach items="${curriculum}" var="curriculum">
					<option value="${curriculum.courseId }">${curriculum.courseName }</option>
				</c:forEach>
			</select>
			<br/>
			
			<label for="grade">Grade:</label>
			<select name="grade" id="grade">
	            <c:forEach items="${grade}" var="grade">
					<option data-parent="${grade.courseId }" value="${grade.gradeId }">${grade.gradeName }</option>
				</c:forEach>
			</select>
            <%-- <c:forEach items="${grade}" var="grade">
				<input type="radio" name="grade" checked="checked" value="${grade.gradeId }" class="radio" />${grade.gradeName } &nbsp;
			</c:forEach> --%>
            <input type="text" name="email" id="email" class="username" placeholder="Contact email: xxx@xxx.xxx">
            <input type="text" name="telephone" id="telephone" placeholder="8-digit Contact phone: xxxxxxxx">
            <!-- <input type="text" name="address" id="address" placeholder="Address"> -->
            <button type="button" onclick="regist('${ctx}')">Sign up</button>
            <div class="error"><span>${message }</span></div>
        </form>
        <div class="connect"></div>
    </div>
    <div align="center">I have an account, <a href="${ctx}/toLogin.action" target="_self">Log in</a>!</div>
</body>

</html>
