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
<link rel="stylesheet" href="${ctx}/css/jquery.searchableSelect.css" />
<title>iframe</title>
</head>

<body>
<div class="title"><h2>Add Curriculum</h2></div>
<form action="${ctx}/addCourse.action" method="post" name="myform" id="myform">
<div class="main">
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Name:</label>
        <input type="text" name="courseName" id="courseName" maxlength="10"/>
    </p>
    <div class="short-input select ue-clear">
    	<label><span style="color:red">*</span>Status:</label>
    	<input name="courseState" type="radio" value="0"/>Deactivated
    	<input name="courseState" type="radio" value="1" checked="checked"/>Active
    </div>
</div>
</form>
<div class="btn ue-clear">
	<a href="javascript:;" class="confirm" onclick="addCourse()">Add</a>
    <a href="${ctx}/toCoursePage.action" class="clear">Cancel</a>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="${ctx}/js/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.searchableSelect.js"></script>
<script type="text/javascript">
$(function(){
	$("#grade").searchableSelect();
});
$(".select-title").on("click",function(){
	$(".select-list").toggle();
	return false;
});
$(".select-list").on("click","li",function(){
	var txt = $(this).text();
	$(".select-title").find("span").text(txt);
});

//注册
function addCourse(){
	document.myform.attributes["action"].value = "${ctx}/addCourse.action"; 
	$("form").submit();
}

/* //情况所有
function returnList(){
	document.myform.attributes["action"].value = "${ctx}/toTypePage.action"; 
	$("form").submit();
} */

showRemind('input[type=text], textarea','placeholder');
</script>
</html>