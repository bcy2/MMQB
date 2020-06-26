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
<!-- for-mobile-apps -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
		function hideURLbar(){ window.scrollTo(0,1); } </script>
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css" media="all" />
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
	function resetQuiz(quizId){
		var redo = confirm("Are you sure you want to reset this quiz?");
		if (redo){
			$.post("${ctx}/resetQuiz.action", 
					{
						"paperId": quizId
					},
				function(data){
					window.location.href = "${ctx}/toMyPaperPage.action?userId=${user.userId}";
				},"json");
		}
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
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toUserInfo.action?userId=${user.userId}">My info</a></li>
							<!-- <li><a class="hvr-overline-from-center button2" href="onlinecheck.html">在线考试</a></li> -->
							<li><a class="hvr-overline-from-center button2  active" href="${ctx}/toScoreQry.action?userId=${user.userId}">Review quizzes</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toMyBooksPage.action?userId=${user.userId}">Question record</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toMyPaperPage.action?userId=${user.userId}">Start working!</a></li>
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
<div class="about">
	<div class="container">
            <table class="table table-bordered">
              <thead>
                <tr>
                  <th width="20%">Quiz name</th>
                  <th width="15%">Curriculum</th>
				  <th width="20%">Start time</th>
				  <th width="20%">Finish time</th>
                  <th width="15%">Score</th>
<!--                   <th width="15%">Quiz Status</th> -->
                  <th width="10%">Operation</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${paper}" var="paper">
              	  <tr>
	                 <td><a href="${ctx}/qrypaper.action?paperId=${paper.paperId}&userId=${paper.userId}"><font color="blue">${paper.paperName}</font></a></td>
	                 <td>${paper.courseId}</td>
	                 <td>${paper.beginTime}</td>
				  	 <td>${paper.endTime}</td>
					 <td>${paper.score}</td>
<%-- 					 <td>
					 	<font color="green">
						 	<c:if test="${paper.paperState==0}">Available</c:if>
						 </font>
						 <font color="orange">
							<c:if test="${paper.paperState==1}">In progress</c:if>
						</font>
						 <font color="blue">
							<c:if test="${paper.paperState==2}">Done</c:if>
						</font>
					 </td> --%>
					 <td><button onclick="resetQuiz('${paper.paperId}')">Reset quiz</button></td>
	              </tr>
				</c:forEach>
              </tbody>
            </table>
	</div>
</div>
<script src="js/bootstrap.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$().UItoTop({ easingType: 'easeOutQuart' });
	});
</script>
<a href="#" id="toTop" style="display: block;"> <span id="toTopHover" style="opacity: 1;"> </span></a>

</body>
</html>
