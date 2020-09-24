<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="${ctx}/css/base.css" />
	<link rel="stylesheet" href="${ctx}/css/login.css" />
	<title>MM Question Bank - Admin Login</title>
</head>
<body>
	<div id="container">
		<div id="bd">
			<div class="login">
				<form action="userLogin.action" method="post">
            	<div class="login-top"><!-- <h1 class="logo"></h1> --></div>
                <div class="login-input">
                	<p class="user ue-clear">
                    	<label>Username</label>
                        <input type="text" id="userId" name="userId" style="width:64%;"/>
                    </p>
                    <p class="password ue-clear">
                    	<label>Password</label>
                        <input type="password" id="userPwd" name="userPwd" style="width:64%;"/>
                    </p>
                    <div style="color: red" id="tipInfo" align="justify">${message }</div>
                </div>
                <div class="login-btn ue-clear">
                	<a class="btn" onclick="userLogin()">Log in</a>
                    <!-- <div class="remember ue-clear">
                    	<input type="checkbox" id="remember" />
                        <em></em>
                        <label for="remember">Remember me</label>
                    </div> -->
                </div>
                </form>
            </div>
            <div align="center"><a href="${ctx}/" target="_self" title="mbzj">Select role</a></div>
		</div>
	</div>
    <div id="ft">Copyright&nbsp;2020,&nbsp; Koding Kingdom (HK) Ltd.</div>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript">
var height = $(window).height();
$("#container").height(height);
$("#bd").css("padding-top",height/2 - $("#bd").height()/2);

$(window).resize(function(){
	var height = $(window).height();
	$("#bd").css("padding-top",$(window).height()/2 - $("#bd").height()/2);
	$("#container").height(height);
	
});

/* $('#remember').focus(function(){
   $(this).blur();
});

$('#remember').click(function(e) {
	checkRemember($(this));
});

function checkRemember($this){
	if(!-[1,]){
		 if($this.prop("checked")){
			$this.parent().addClass("checked");
		}else{
			$this.parent().removeClass("checked");
		}
	}
} */

function userLogin(){
	var userId = $("#userId").val();
	var userPwd = $("#userPwd").val();
	if(userId == "" || userPwd == ""){
		$("#tipInfo").html("Please input your username/password!");
		return;
	}
	$("form").submit();
}

</script>
</html>