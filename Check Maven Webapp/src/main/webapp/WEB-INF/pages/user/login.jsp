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

        <!-- CSS -->
        <link rel="stylesheet" href="${ctx}/css/reset.css">
        <link rel="stylesheet" href="${ctx}/css/supersized.css">
        <link rel="stylesheet" href="${ctx}/css/userlogin.css">

        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

    </head>

    <body>

        <div class="page-container">
            <h1>Student Login</h1>
            <form action="${ctx}/toIndex.action" method="post" name="myform" id="myform">
                <input type="text" name="userId" id="userId" class="username" placeholder="Username">
                <input type="password" name="userPwd" id="userPwd" class="password" placeholder="Password">
               <%--  <span>${message }</span> --%>
                <button type="button" onclick="login()">Log in</button>
                <div class="error"><span>${message }</span></div>
            </form>
            <div class="connect"></div>
            <div align="center">No account? <a href="${ctx}/toRegistPage.action" target="_self" title="mbzj">Register</a></div>
            <div class="connect"></div>
            <div align="center"><a href="${ctx}/" target="_self" title="mbzj">Select role</a></div>
        </div>

        <!-- Javascript -->
        <script src="${ctx}/js/jquery-1.8.2.min.js"></script>
        <script src="${ctx}/js/supersized.3.2.7.min.js"></script>
        <script src="${ctx}/js/supersized-init.js"></script>
        <script src="${ctx}/js/scripts.js"></script>
        <script type="text/javascript">
        	function login(){
        		var userId = $("#userId").val();
        		var userPwd = $("#userPwd").val();
        		$.post("${ctx}/checkPwd.action", { userId:userId, userPwd:userPwd },function(data){
    				if(data.errorNo != "0"){
    					alert(data.errorInfo);
    				}else{
    					document.myform.attributes["action"].value = "${ctx}/toIndex.action"; 
    					$("form").submit();
    				}
        		},"json");
        	}
        </script>

    </body>

</html>
