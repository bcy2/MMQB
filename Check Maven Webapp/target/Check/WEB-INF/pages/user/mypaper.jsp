<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%
	response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate");
response.addHeader("Pragma", "no-cache");
response.addDateHeader("Expires", 0);
if (session.getAttribute("user") == null) {
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
<script type="application/x-javascript">
	addEventListener("load", function() { 
		setTimeout(hideURLbar, 0); 
	}, false);
	function hideURLbar(){ 
		window.scrollTo(0,1); 
	} 
	function exitSystem(){
		window.location.href = "${ctx}/user/exitSys.action"	;
	}
	function loadPaper(){
		$("body").append('<div id="supersized-loader"></div><ul id="supersized"></ul>');
	}
</script>
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css"
	media="all" />
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css"
	media="all" />
<link href="${ctx}/css/myPaperPage.css" rel="stylesheet" type="text/css"
	media="all" />
<script src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/modernizr.custom.js"></script>
<script type="text/javascript" src="${ctx}/js/move-top.js"></script>
<script type="text/javascript" src="${ctx}/js/easing.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function($) {
		$(".scroll").click(function(event) {
			event.preventDefault();
			$('html,body').animate({
				scrollTop : $(this.hash).offset().top
			}, 1000);
		});
	});
</script>

</head>
<body>
	<!-- header -->
	<div class="header">
		<div class="container">
			<div class="header-nav">
				<nav class="navbar navbar-default">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<h3 style="line-height: normal;">
						<span style="color: white;">Welcome, <a
							class="hvr-overline-from-center button2"
							href="${ctx}/toUserInfo.action?userId=${user.userId}"
							style="display: inline; vertical-align: bottom"><font
								color="#2FD828">${userName }</font></a>.
						</span>
					</h3>
				</div>

				<div class="collapse navbar-collapse nav-wil"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li><a class="hvr-overline-from-center button2"
							href="${ctx}/toIndex.action?userId=${user.userId}">Home</a></li>
						<li><a class="hvr-overline-from-center button2"
							href="${ctx}/toUserStatistics.action?userId=${user.userId}">My
								statistics</a></li>
						<!-- <li><a class="hvr-overline-from-center button2" href="onlinecheck.html">在线考试</a></li> -->
						<li><a class="hvr-overline-from-center button2"
							href="${ctx}/toScoreQry.action?userId=${user.userId}">Review
								quizzes</a></li>
						<li><a class="hvr-overline-from-center button2"
							href="${ctx}/toMyBooksPage.action?userId=${user.userId}">Question
								record</a></li>
						<li><a class="hvr-overline-from-center button2  active"
							href="${ctx}/toMyPaperPage.action?userId=${user.userId}">Start
								working!</a></li>
						<li><a class="hvr-overline-from-center button2"
							href="${ctx}/toAbout.action">About</a></li>
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
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#quiz">My
					quizzes</a></li>
			<li><a data-toggle="tab" href="#paper">My mock exam papers</a></li>
		</ul>
		<div class="tab-content">
			<div id="quiz" class="tab-pane fade in active">
				<h3 class="t-button" style="margin-top: 0; padding-top: 0;">
					<a href="${ctx}/toQuizGeneratePage.action?userId=${user.userId}"><span
						class="label label-info">+ New quiz</span></a>
					<a href="${ctx}/toAiPractice.action?userId=${user.userId}"><span
						class="label label-info">Practice with AI</span></a>
				</h3>
				<table class="table table-bordered">
					<caption>
						<font color="green">Available</font>
					</caption>
					<thead>
						<tr>
							<th width="20%">Quiz name</th>
							<th width="20%">Grade(s)</th>
							<th width="10%">No. of Qs</th>
							<th width="10%">Current Q</th>
							<th width="10%">Difficulty</th>
							<th width="15%">Create time</th>
							<th width="15%">Start time</th>
							<!--                   <th>Quiz Status</th> -->
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${paper}" var="paper">
							<tr>
								<td><a
									href="${ctx}/qryPaperDetail.action?paperId=${paper.paperId}&userId=${paper.userId}"><font
										color="blue">${paper.paperName}</font></a></td>
								<td>${paper.gradeId}</td>
								<td>${paper.questionId}</td>
								<c:choose>
									<c:when test="${paper.paperState==0}">
										<td>N/A</td>
									</c:when>
									<c:when test="${paper.paperState==1}">
										<td>${paper.currentQuestion}</td>
									</c:when>
									<c:when test="${paper.paperState==2}">
										<td>${paper.currentQuestion}</td>
									</c:when>
									<c:otherwise>
										<td></td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${paper.difficulty>=0.99}">
										<td>Difficult</td>
									</c:when>
									<c:when test="${paper.difficulty<=0.34}">
										<td>Easy</td>
									</c:when>
									<c:otherwise>
										<td>Medium</td>
									</c:otherwise>
								</c:choose>
								<td>${paper.createTime}</td>
								<td>${paper.beginTime}</td>
								<%-- <td>${paper.score}</td> --%>
								<%-- 					 <td>
					 	<font color="green">
						 	<c:if test="${paper.paperState==0}">Ready to start</c:if>
						 </font>
						 <font color="orange">
							<c:if test="${paper.paperState==1}">In progress</c:if>
						</font>
						 <font color="blue">
							<c:if test="${paper.paperState==2}">Submitted</c:if>
						</font>
					 </td> --%>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<hr>
				<table class="table table-bordered">
					<caption>
						<font color="orange">In Progress</font>
					</caption>
					<thead>
						<tr>
							<th width="20%">Quiz name</th>
							<th width="20%">Grade(s)</th>
							<th width="10%">No. of Qs</th>
							<th width="10%">Current Q</th>
							<th width="10%">Difficulty</th>
							<th width="15%">Create time</th>
							<th width="15%">Start time</th>
							<!--                   <th>Quiz Status</th> -->
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${paperInProgress}" var="paper">
							<tr>
								<td><a
									href="${ctx}/qryPaperDetail.action?paperId=${paper.paperId}&userId=${paper.userId}"><font
										color="blue">${paper.paperName}</font></a></td>
								<td>${paper.gradeId}</td>
								<td>${paper.questionId}</td>
								<c:choose>
									<c:when test="${paper.paperState==0}">
										<td>N/A</td>
									</c:when>
									<c:when test="${paper.paperState==1}">
										<td>${paper.currentQuestion}</td>
									</c:when>
									<c:when test="${paper.paperState==2}">
										<td>${paper.currentQuestion}</td>
									</c:when>
									<c:otherwise>
										<td></td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${paper.difficulty>=0.99}">
										<td>Difficult</td>
									</c:when>
									<c:when test="${paper.difficulty<=0.34}">
										<td>Easy</td>
									</c:when>
									<c:otherwise>
										<td>Medium</td>
									</c:otherwise>
								</c:choose>
								<td>${paper.createTime}</td>
								<td>${paper.beginTime}</td>
								<%-- <td>${paper.score}</td> --%>
								<%-- 					 <td>
					 	<font color="green">
						 	<c:if test="${paper.paperState==0}">Ready to start</c:if>
						 </font>
						 <font color="orange">
							<c:if test="${paper.paperState==1}">In progress</c:if>
						</font>
						 <font color="blue">
							<c:if test="${paper.paperState==2}">Submitted</c:if>
						</font>
					 </td> --%>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<div id="paper" class="tab-pane fade">
				<h3 class="t-button" style="margin-top: 0; padding-top: 0;">
					<a href="${ctx}/toPaperGeneratePage.action?userId=${user.userId}"><span
						class="label label-info">+ New mock exam paper</span></a>
					<%-- <a href="${ctx}/toBookRevSes.action?userId=${user.userId}"><span
						class="label label-info">+ New mock exam paper</span></a> --%>
				</h3>
				<table class="table table-bordered">
					<caption>
						<font color="green">My mock exam papers</font>
					</caption>
					<thead>
						<tr>
							<th width="20%">Paper name</th>
							<th width="20%">Grade(s)</th>
							<th width="10%">No. of Qs</th>
							<!-- <th width="10%">Current Q</th> -->
							<th width="10%">Difficulty</th>
							<th width="15%">Create time</th>
							<!-- <th width="15%">Start time</th> -->
							<th width="10%">Paper Status</th>
							<th width="15%">Operation</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${examPaper}" var="paper">
							<tr>
								<td><a
									href="${ctx}/qryExamPaperDetail.action?paperId=${paper.paperId}&userId=${paper.userId}" onclick="loadPaper(); return true;"><font
										color="blue">${paper.paperName}</font></a></td>
								<td>${paper.gradeId}</td>
								<td>${paper.questionId}</td>
								<%-- <c:choose>
									<c:when test="${paper.paperState==0}">
										<td>N/A</td>
									</c:when>
									<c:when test="${paper.paperState==1}">
										<td>${paper.currentQuestion}</td>
									</c:when>
									<c:when test="${paper.paperState==2}">
										<td>${paper.currentQuestion}</td>
									</c:when>
									<c:otherwise>
										<td></td>
									</c:otherwise>
								</c:choose> --%>
								<c:choose>
									<c:when test="${paper.difficulty>=0.99}">
										<td>Difficult</td>
									</c:when>
									<c:when test="${paper.difficulty<=0.34}">
										<td>Easy</td>
									</c:when>
									<c:otherwise>
										<td>Medium</td>
									</c:otherwise>
								</c:choose>
								<td>${paper.createTime}</td>
								<%-- <td>${paper.beginTime}</td> --%>
								<%-- <td>${paper.score}</td> --%>
								<td>
								 	<font color="green">
									 	<c:if test="${paper.paperState==0}">Available</c:if>
									 </font>
									 <font color="orange">
										<c:if test="${paper.paperState==1}">To review</c:if>
									</font>
									 <font color="blue">
										<c:if test="${paper.paperState==2}">Reviewed</c:if>
									</font>
								</td>
								<td>
									<button onclick="window.location.href = '${ctx}/toBookRevSes.action?userId=${user.userId}'">Book a review session</button>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<script src="${ctx}/js/bootstrap.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$().UItoTop({
				easingType : 'easeOutQuart'
			});
		});
	</script>
	<a href="#" id="toTop" style="display: block;"> <span
		id="toTopHover" style="opacity: 1;"> </span></a>
</body>
</html>