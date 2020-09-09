<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="${ctx}/css/base.css" />
<link rel="stylesheet" href="${ctx}/css/info-reg.css" />
<title>iframe</title>
</head>

<body>
<div class="title"><h2>User Details</h2></div>
<form action="${ctx}/admin/updateUser.action" method="post" name="myform" id="myform">
<div class="main">
	<p class="short-input ue-clear newstyle">
    	<label>Username:</label>
        ${user.userId }
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Nickname:</label>
        ${user.userName }
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Status:</label>
    	<c:if test="${user.userState == 0}">To verify</c:if>
    	<c:if test="${user.userState == 1}">Active</c:if>
    	<c:if test="${user.userState == 2}">Deactivated</c:if>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Type:</label>
    	<c:if test="${user.userType == 0}">Student</c:if>
    	<c:if test="${user.userType == 1}">Teacher</c:if>
    	<c:if test="${user.userType == 2}">Admin</c:if>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Curriculum:</label>
	    <c:forEach items="${course}" var="course">
			<c:if test="${course.courseId == user.curriculum}">${course.courseName }</c:if>
		</c:forEach>
	</p>
    <p class="short-input ue-clear newstyle">
    	<label>Grade:</label>
	    <c:forEach items="${grade}" var="grade">
			<c:if test="${grade.gradeId == user.grade}">${grade.gradeName }</c:if>
		</c:forEach>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Email:</label>
        ${user.email }
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Contact phone:</label>
        ${user.telephone }
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Parent name:</label>
        ${user.parentName }
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Reward Points:</label>
        ${user.rewardPoints }
    </p>
    <%-- <p class="short-input ue-clear newstyle">
    	<label>Address:</label>
        ${user.address }
    </p> --%>
    <p class="short-input ue-clear newstyle">
    	<label>Remarks:</label>
        ${user.remark }
    </p>
</div>
</form>
<div class="btn ue-clear">
	<c:if test="${user.userType == 0}">
		<a href="${ctx}/admin/toUserStatistics.action?userId=${user.userId}" class="clear">Statistics</a>
	</c:if>
	<a href="${ctx}/admin/getAllUser.action" class="confirm">Back</a>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="${ctx}/js/WdatePicker.js"></script>
<script type="text/javascript">
$(".select-title").on("click",function(){
	$(".select-list").toggle();
	return false;
});
$(".select-list").on("click","li",function(){
	var txt = $(this).text();
	$(".select-title").find("span").text(txt);
});

function checkPhone(){
	
}

//注册
function addUser(){
	$("form").submit();
}

//情况所有
function clearAll(){
	alert("Clear all.");
}

showRemind('input[type=text], textarea','placeholder');
</script>
</html>