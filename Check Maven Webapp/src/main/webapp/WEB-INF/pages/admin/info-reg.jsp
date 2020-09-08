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
<%-- <link rel="stylesheet" href="${ctx}/css/jquery.searchableSelect.css" /> --%>
<title>iframe</title>
</head>

<body>
<div class="title"><h2>Add User</h2></div>
<form action="${ctx}/admin/addUser.action" method="post" name="myform" id="myform">
<div class="main">
	<p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Username:</label>
        <input type="text" placeholder="Username" name="userId" id="userId" onblur="checkUserId()" value="${userId }"/>
        <span style="color: red" id="tipInfo">${message }</span>
    </p>
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Nickname:</label>
        <input type="text" placeholder="Nickname" name="userName" id="userName" maxlength="8"/>
    </p>
    <div class="long-input select ue-clear">
    	<label><span style="color:red">*</span>Password:</label>
        <input type="password" placeholder="Password(>=6 chars)" name="userPwd" id="userPwd"/>
    </div>
    <div class="long-input select ue-clear">
    	<label><span style="color:red">*</span>Curriculum:</label>
    	<select name="curriculum" id="curriculum">
            <option value="">Please choose...</option>
	        <c:forEach items="${course}" var="course">
				<option value="${course.courseId }" <c:if test="${course.courseId == user.curriculum}">selected</c:if>>${course.courseName }</option>
			</c:forEach>
		</select>
	</div>
   <div class="short-input select ue-clear">
    	<label><span style="color:red">*</span>Grade:</label>
    	<select name="grade" id="grade">
	        <c:forEach items="${grade}" var="grade">
				<option data-parent="${grade.courseId }" value="${grade.gradeId }" <c:if test="${grade.gradeId == user.grade}">selected</c:if>>${grade.gradeName }</option>
			</c:forEach>
		</select>
    </div>
    <div class="short-input select ue-clear">
    	<label><span style="color:red">*</span>Type:</label>
    	<input name="userType" type="radio" value="0" checked="checked"/>Student
    	<input name="userType" type="radio" value="1"/>Teacher
    	<input name="userType" type="radio" value="2"/>Admin
    </div>
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Email:</label>
        <input type="text" onblur="checkEmail()" id="email" name="email"/>
        <span style="color: red" id="emailInfo"></span>
    </p>
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Contact phone:</label>
        <input type="text" onfocus="checkPhone()" id="telephone" name="telephone"/>
    </p>
    <p class="short-input ue-clear">
    	<label>Parent name:</label>
        <input type="text" id="parentName" name="parentName"/>
    </p>
<!--     <p class="long-input ue-clear">
    	<label>Address:</label>
        <input type="text" id="address" name="address"/>
    </p> -->
    <p class="short-input ue-clear">
    	<label>Remarks:</label>
        <textarea placeholder="Remarks" id="remark" name="remark"></textarea>
    </p>
</div>
</form>
<div class="btn ue-clear">
	<a href="javascript:;" class="confirm" onclick="addUser()">Add</a>
    <a href="${ctx}/admin/getAllUser.action" class="clear" >Cancel</a>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="${ctx}/js/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<%-- <script type="text/javascript" src="${ctx}/js/jquery.searchableSelect.js"></script> --%>
<script type="text/javascript">
jQuery(document).ready(function() {
	// 获取子select的option
	let childOptions = $("select[name='grade']").find("option");
 	childOptions.each(function() {
		this.remove();
	});
	$("select[name='curriculum']").change(cascadeSelect);

	// 级联过滤方法
	function cascadeSelect(event) {
		// 获取选中index及value
		let index = event.target["selectedIndex"];
		let value = event.target[index].value;
		// 过滤方法1
		let options = childOptions.filter(function() {
			// return (this.value == "" || this.dataset.parent == value);
			return (this.dataset.parent == value);
		});
		// 过滤方法2
		// let options = Array.from(childOptions).filter(function
		// (option) {
		// return option.value == "" || option.dataset.parent == value
		// });
		// 清空子select,重新绑定，并设定默认选中项
		$("select[name='grade']").empty().append(options);
		// $("select[name='grade']").find("option[value='']").prop(
		// "selected", true);
	}
});

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

function checkUserId(){
	var userId = $("#userId").val();
	var tipInfo = $("#tipInfo").val();
	if(userId == ""){
		$("#tipInfo").html("Empty username.");
		$("#userId").focus();
		return;
	}
 	$.ajax({
        type: "post",
        url: "${ctx}/admin/userRegist.action",
        data: {userId:userId},
        dataType: "json",
        success: function(data){
        	$("#tipInfo").html(data.errorInfo);
        }
    });
}

function checkEmail(){
	var email = $("#email").val();
	if(email == ""){
		$("#emailInfo").html("Empty email.");
		return;
	}
	var req = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
	if(!req.test(email)){
		$("#emailInfo").html("Email format is wrong.");
		$("#email").focus();
		return;
	}
}

//注册
function addUser(){
	document.myform.attributes["action"].value = "${ctx}/admin/addUser.action"; 
	$("form").submit();
}

//情况所有
function clearAll(){
	alert("Clear all.");
}

showRemind('input[type=text], textarea','placeholder');
</script>
</html>