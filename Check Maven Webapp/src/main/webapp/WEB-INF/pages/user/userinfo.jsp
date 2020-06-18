<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 <%
   response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate"); 
   response.addHeader("Pragma", "no-cache"); 
   response.addDateHeader ("Expires", 0);
   if (session.getAttribute("user")==null) {
       response.sendRedirect("user/exitSys.action");
   }
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Major Maths Question Bank</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="application/x-javascript"> 
	addEventListener("load", function() { 
		setTimeout(hideURLbar, 0); 
	}, false);
	function hideURLbar(){ 
		window.scrollTo(0,1); 
	} 
</script>
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css" media="all" />
<style type="text/css">
	.input-group .input-group-addon{
  		width:50%;
  		text-align: left;
	}
	.input-group .form-control{
  		width:100%;
	}
</style>
<script src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/modernizr.custom.js"></script>
<script type="text/javascript" src="${ctx}/js/move-top.js"></script>
<script type="text/javascript" src="${ctx}/js/easing.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function($) {
		$(".scroll").click(function(event){		
			event.preventDefault();
			$('html,body').animate({scrollTop:$(this.hash).offset().top},1000);
		});
	});
	function exitSystem(){
		window.location.href = "${ctx}/user/exitSys.action"	;
	}
</script>
</head>
<body>
<div class="header">
		<div class="container">
			<div class="header-nav">
				<nav class="navbar navbar-default">
					<div class="navbar-header">
						<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
							<span class="sr-only">Toggle navigation</span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
					   	<h3><span style="color: white;">Welcome, <font color="blue">${userName }</font>.</span></h3>
					</div>
					
					<div class="collapse navbar-collapse nav-wil" id="bs-example-navbar-collapse-1">
						<ul class="nav navbar-nav">
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toIndex.action?userId=${user.userId}">Home</a></li>
							<li><a class="hvr-overline-from-center button2  active" href="${ctx}/toUserInfo.action?userId=${user.userId}">My Info</a></li>
							<!-- <li><a class="hvr-overline-from-center button2" href="onlinecheck.html">在线考试</a></li> -->
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toScoreQry.action?userId=${user.userId}">Review Quizzes</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toMyBooksPage.action?userId=${user.userId}">Review Mistakes</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toMyPaperPage.action?userId=${user.userId}">Start Working!</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toAbout.action">About MMQB</a></li>
						</ul>
						<div class="search-box">
							<div id="sb-search" class="sb-search">
								<span class="sb-icon-search" onclick="exitSystem()"></span>
							</div>
						</div>
					</div>
				</nav>
			</div>
		</div>
</div>
<div class="container">
	<form action="${ctx}/updateUserInfo.action" method="post" name="myform" id="myform">
	<div class="bs-example bs-example-tabs" role="tabpanel" data-example-id="togglable-tabs">
		<ul id="myTab" class="nav nav-tabs" role="tablist">
		  <li role="presentation" class="active"><a href="#home" id="home-tab" role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true" onclick="userInfo()"> Personal Profile</a></li>
		  <li role="presentation"><a href="#profile" role="tab" id="profile-tab" data-toggle="tab" aria-controls="profile" onclick="updatePwd()">Change Password</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
		  <div role="tabpanel" class="tab-pane fade in active" id="home" aria-labelledby="home-tab">
		  	<input type="hidden" id="userState" name="userState" value="${user.userState }"/>
		  	<input type="hidden" id="userType" name="userType" value="${user.userType }"/>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">Username:</span>
			  <input type="text" class="form-control" 
			  		id="userId" name="userId" value="${user.userId }" readonly="readonly">
			</div>
		
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">User nickname:</span>
			  <input type="text" class="form-control" 
			  		id="userName" name="userName" value="${user.userName }"/>
			</div>
		
			<div class="input-group">
			  <c:forEach items="${course}" var="course">
			  	<c:if test="${course.courseId == user.curriculum}">
			  		<span class="input-group-addon" id="basic-addon1">Curriculum:</span>
			  		<input type="text" class="form-control" 
			  		id="Curriculum" name="Curriculum" value="${course.courseName }" readonly="readonly">
			  	</c:if>
			  </c:forEach>
			</div>
			
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">Grade:</span>
			  <select id="grade" name="grade" class="form-control">
			  	<c:forEach items="${grade}" var="grade">
					<option value="${grade.gradeId }" <c:if test="${grade.gradeId == user.grade}">selected</c:if>>${grade.gradeName }</option>
				</c:forEach>
			  </select>
			  <%-- <input type="text" class="form-control" 
			  		id="grade" name="grade" value="${grade[user.grade].gradeName }"> --%>
			</div>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">Contact email:</span>
			  <input type="text" class="form-control"
			  		id="email" name="email" value="${user.email }">
			</div>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">Contact phone:</span>
			  <input type="text" class="form-control" 
			  		id="telephone" name="telephone" value="${user.telephone }">
			</div>
		
			<div class="input-group">
			  <span class="input-group-addon" id="sizing-addon3">Reward points:</span>
			  <input type="text" class="form-control"
			  		id="rewardPoints" name="rewardPoints" value="${user.rewardPoints }" readonly="readonly">
			</div>
		  </div>
		  <div role="tabpanel" class="tab-pane fade" id="profile" aria-labelledby="profile-tab">
		  <c:if test="${'hide!!!'}">
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">Original password:</span>
			  <input type="password" class="form-control" name="userPwd" id="userPwd"
			  		value="${user.userPwd }" readonly="readonly">
			</div>
			</c:if>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">New password:</span>
			  <input type="password" class="form-control"
			  		id="pwd" name="pwd" value="">
			</div>
			<div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">Repeat password:</span>
			  <input type="password" class="form-control"
			  		id="newPwd" name="newPwd" value="">
			</div>
		</div>
	 </div>
	</form>
	<h3 class="t-button">
		<a href="javascript:;"><span class="label label-success" onclick="update()">Update</span></a>
		<a href="${ctx}/toIndex.action"><span class="label label-info">Cancel</span></a>
	</h3>
</div>
<script src="${ctx}/js/bootstrap.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	$().UItoTop({ easingType: 'easeOutQuart' });
	});
	
	function update(){
		var pwd = $("#pwd").val();
		var newPwd = $("#newPwd").val();
		if(pwd!= newPwd){
			alert("Passwords don't match, please check.");
			return;
		}
		if(pwd && pwd.length<6){
			alert("Password has to consist of at least 6 characters.");
			return;
		}
		document.myform.attributes["action"].value = "${ctx}/updateUserInfo.action"; 
		$("form").submit();
	}
	
	//userInfo tab
	function userInfo(){
		document.getElementById("myform").reset(); 
	}
	
	//updatePwd tab
	function updatePwd(){
		document.getElementById("myform").reset(); 
	}
	
</script>
<a href="#" id="toTop" style="display: block;"> <span id="toTopHover" style="opacity: 1;"> </span></a>

</body>
</html>