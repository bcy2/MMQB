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
<div class="title"><h2>Edit User</h2></div>
<form action="${ctx}/admin/updateUser.action" method="post" name="myform" id="myform">
<div class="main">
	<p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Username:</label>
        <input type="text" name="userId" id="userId" value="${user.userId }" readonly="readonly"/>
    </p>
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Nickname:</label>
        <input type="text" placeholder="Nickname" name="userName" id="userName" value="${user.userName }"/>
    </p>
    <div class="short-input select ue-clear">
    	<label><span style="color:red">*</span>Status:</label>
    	<input name="userState" type="radio" value="0" <c:if test="${user.userState == 0}">checked="checked"</c:if>/>To verify
    	<input name="userState" type="radio" value="1" <c:if test="${user.userState == 1}">checked="checked"</c:if>/>Active
    	<input name="userState" type="radio" value="2" <c:if test="${user.userState == 2}">checked="checked"</c:if>/>Deactivated
    </div>
    <div class="short-input select ue-clear">
    	<label><span style="color:red">*</span>Type:</label>
    	<input name="userType" type="radio" value="0" <c:if test="${user.userType == 0}">checked="checked"</c:if>/>Student
    	<input name="userType" type="radio" value="1" <c:if test="${user.userType == 1}">checked="checked"</c:if>/>Teacher
    	<input name="userType" type="radio" value="2" <c:if test="${user.userType == 2}">checked="checked"</c:if>/>Admin
    </div>
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Cur.:</label>
    	<select name="curriculum" id="curriculum">
            <option value="">Please choose...</option>
	        <c:forEach items="${course}" var="course">
				<option value="${course.courseId }" <c:if test="${course.courseId == user.curriculum}">selected</c:if>>${course.courseName }</option>
			</c:forEach>
		</select>
	</p>
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Grade:</label>
    	<select name="grade" id="grade">
	        <c:forEach items="${grade}" var="grade">
				<option data-parent="${grade.courseId }" value="${grade.gradeId }" <c:if test="${grade.gradeId == user.grade}">selected</c:if>>${grade.gradeName }</option>
			</c:forEach>
		</select>
    </p>
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Email:</label>
        <input type="text" onfocus="checkEmail()" id="email" name="email" value="${user.email }"/>
    </p>
    <p class="short-input ue-clear">
    	<label>Contact phone:</label>
        <input type="text" onfocus="checkPhone()" id="telephone" name="telephone" value="${user.telephone }"/>
    </p>
    <p class="short-input ue-clear">
    	<label>Parent name:</label>
        <input type="text" id="parentName" name="parentName" value="${user.parentName }"/>
    </p>
    <p class="long-input ue-clear">
    	<label>Reward Points:</label>
        <input type="text" id="rewardPoints" name="rewardPoints" value="${user.rewardPoints }"/>
    </p>
    <%-- <p class="long-input ue-clear">
    	<label>Address:</label>
        <input type="text" id="address" name="address" value="${user.address }"/>
    </p> --%>
    <p class="short-input ue-clear">
    	<label>Remarks:</label>
        <textarea placeholder="Remarks" id="remark" name="remark">${user.remark }</textarea>
    </p>
</div>
</form>
<div class="btn ue-clear">
	<a href="javascript:;" class="confirm" onclick="addUser()">Update</a>
	<a href="${ctx}/admin/getAllUser.action" class="clear">Cancel</a>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="${ctx}/js/WdatePicker.js"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
	// 获取子select的option
	let childOptions = $("select[name='grade']").find("option");
/* 	childOptions.each(function() {
		this.remove();
	}); */
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