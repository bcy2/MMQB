<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="newLine" value="\\n"/>
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
<script src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/modernizr.custom.js"></script>
<script type="text/javascript" src="${ctx}/js/move-top.js"></script>
<script type="text/javascript" src="${ctx}/js/easing.js"></script>
<script>
MathJax = {
  tex: {
    inlineMath: [['$', '$'], ['\\(', '\\)']]
  },
  svg: {
    fontCache: 'global'
  }
};
</script>
<script id="MathJax-script" async src="https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js"></script>
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

	// to integrate
	function hideAll() {
	    var hideableSections = document.querySelectorAll('.questionItem');
	    /* document.querySelectorAll() will return a JavaScript object
	       containing all nodes which match the query. Since you are
	       passing '.hideable-section' the returned object will contain
	       all elements with that class. */
	    for (var i = 0; i < hideableSections.length; i++) {
	        hideableSections[i].style.display = 'none';
	    }
	}
	
	function showAll() {
	    var hideableSections = document.querySelectorAll('.questionItem');
	    /* document.querySelectorAll() will return a JavaScript object
	       containing all nodes which match the query. Since you are
	       passing '.hideable-section' the returned object will contain
	       all elements with that class. */
	    for (var i = 0; i < hideableSections.length; i++) {
	        hideableSections[i].style.display = 'block';
	    }
	}
	
	function showOneType(className) {
	    hideAll(); /* This will hide all of the sections with
	                         the class .hideable-section */
	    var hideableSections = document.querySelectorAll(className);
	    for (var i = 0; i < hideableSections.length; i++) {
	        hideableSections[i].style.display = 'block';
	    }
	}
	
	function type(typeId){
		showOneType("."+typeId);
	}
	function grade(gradeId){
		showOneType("."+gradeId);
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
					   	<h3 style="line-height: normal;"><span style="color: white;">Welcome, <a class="hvr-overline-from-center button2" href="${ctx}/toUserInfo.action?userId=${user.userId}" style="display: inline;vertical-align: bottom"><font color="#2FD828">${userName }</font></a>.</span></h3>
					</div>
					
					<div class="collapse navbar-collapse nav-wil" id="bs-example-navbar-collapse-1">
						<ul class="nav navbar-nav">
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toIndex.action">Home</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toUserStatistics.action?userId=${user.userId}">My statistics</a></li>
<!-- 							<li><a class="hvr-overline-from-center button2" href="onlinecheck.html">在线考试</a></li> -->
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toScoreQry.action?userId=${user.userId}">Review quizzes</a></li>
							<li><a class="hvr-overline-from-center button2  active" href="${ctx}/toMyBooksPage.action?userId=${user.userId}">Question record</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toMyPaperPage.action?userId=${user.userId}">Start working!</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toAbout.action">About</a></li>
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
<!-- <div class="typrography"> -->
	 <div class="container">
			<div class="grid_4 grid_5">
			  <h5 class="typ1 t-button">
				<span>Filter:</span>
				<a href="#"><span class="label label-success" onclick="showAll()">Show all</span></a>
			  </h5>
 			  <h5 class="typ1 t-button">
				<span>Grade:</span>
				<c:forEach items="${grade}" var="grade">
					<a href="#"><span class="label label-success" onclick="grade('grade${grade.gradeId}')">${grade.gradeName}</span></a>
				</c:forEach>
			  </h5>
<%--			  <h5 class="typ1 t-button">
				<span>Course:</span>
				<c:forEach items="${course}" var="course">
					<a href="#"><span class="label label-success" onclick="course(${course.courseId})">${course.courseName}</span></a>
				</c:forEach>
			  </h5> --%>
			  <h5 class="typ1 t-button">
				<span>Type:</span>
				<c:forEach items="${type}" var="type">
					<a href="#"><span class="label label-success" onclick="type('type${type.typeId}')">${type.typeName}</span></a>
				</c:forEach>
			  </h5>
			</div>
			
			<h2>Accuracy: 
				<span>
					<c:choose>
						<c:when test="${accuracy < 0}">
							N/A
						</c:when>
						<c:otherwise>
							${accuracy*100 }%
						</c:otherwise>
					</c:choose>
				</span>
			</h2>
			<br>
			<div class="progress">
				 <div class="progress-bar progress-bar-success" style="width: ${accuracy*100 }%"><span class="sr-only">correct</span></div>
				 <div class="progress-bar progress-bar-warning" style="width: ${(1-accuracy)*100 }%"><span class="sr-only">wrong</span></div>
				 <%-- <div class="progress-bar progress-bar-danger" style="width: ${(1-accuracy)*100 }%"><span class="sr-only">10% Complete (danger)</span></div> --%>
			</div>
			
			
			<c:forEach items="${requestScope.questionRecordList }" var="questionRecordList">
				<c:if test="${questionRecordList.question.typeId==1}">
					<div class="questionItem type${questionRecordList.question.typeId } grade${questionRecordList.question.gradeId }">
					<hr>
					<!-- 选择题 -->
						<p><h4 class="bars" align="left">${fn:replace(questionRecordList.question.quesName, newLine, "<br />")}</h4></p>
						<c:if test="${questionRecordList.question.attachmentId != 0}">
							<img src="data:image/png;base64,${questionRecordList.question.attachmentFile}">
						</c:if>
						<div class="input-group">
							A.&nbsp;<font size="4">${questionRecordList.question.optionA }</font></br>
							B.&nbsp;<font size="4">${questionRecordList.question.optionB }</font></br>
							C.&nbsp;<font size="4">${questionRecordList.question.optionC }</font></br>
							D.&nbsp;<font size="4">${questionRecordList.question.optionD }</font></br>
							<p><h4 class="bars">My answer:
								<c:choose>
									<c:when test="${questionRecordList.correctness}">
										<font color='green'>${questionRecordList.userAnswer }&nbsp;&check;</font>
									</c:when>
									<c:otherwise>
										<font color='red'>${questionRecordList.userAnswer }&nbsp;&cross;</font>
									</c:otherwise>
								</c:choose>
							</h4></p>
							<c:if test="${!questionRecordList.correctness}">
								<p><h4 class="bars">Correct answer: <font color="green">${questionRecordList.question.answer }</font></h4></p>
							</c:if>
							<c:if test="${questionRecordList.question.answerDetail}">
								<p><h4 class="bars">Explanation: ${questionRecordList.question.answerDetail }</h4></p>
							</c:if>
							<p><h4 class="bars">From quiz: <strong>${questionRecordList.quizName }</strong> / Question grade: <strong>${questionRecordList.gradeName }</strong></h4></p>
						</div>
					</div>
				</c:if>
				
				<c:if test="${questionRecordList.question.typeId==2}">
					<div class="questionItem type${questionRecordList.question.typeId } grade${questionRecordList.question.gradeId }">
					<hr>
					<!-- 填空题 -->
					<p><h4 class="bars" align="left">${fn:replace(questionRecordList.question.quesName, newLine, "<br />")}</h4></p>
						<c:if test="${questionRecordList.question.attachmentId != 0}">
							<img src="data:image/png;base64,${questionRecordList.question.attachmentFile}">
						</c:if>
						<div class="input-group">
							<p><h4 class="bars">My answer:
								<c:choose>
									<c:when test="${questionRecordList.correctness}">
										<font color='green'>${questionRecordList.userAnswer }&nbsp;&check;</font>
									</c:when>
									<c:otherwise>
										<font color='red'>${questionRecordList.userAnswer }&nbsp;&cross;</font>
									</c:otherwise>
								</c:choose>
							</h4></p>
							<c:if test="${!questionRecordList.correctness}">
								<p><h4 class="bars">Correct answer: <font color="green">${questionRecordList.question.answer }</font></h4></p>
							</c:if>
							<c:if test="${questionRecordList.question.answerDetail}">
								<p><h4 class="bars"><font color="red">Explanation:${questionRecordList.question.answerDetail }</font></h4></p>
							</c:if>
							<p><h4 class="bars">From quiz: <strong>${questionRecordList.quizName }</strong> / Question grade: <strong>${questionRecordList.gradeName }</strong></h4></p>
						</div>
					</div>
				</c:if>
				
				<%-- <c:if test="${questionRecordList.question.typeId==5}">
					<div class="questionItem type${questionRecordList.question.typeId } grade${questionRecordList.question.gradeId }">
					<hr>
					<!-- 多选题 -->
					<p><h4 class="bars" align="left">${questionRecordList.question.quesName }</h4></p>
						<div class="input-group">
							<p><h4 class="bars"><font color="blue">My answer：${questionRecordList.userAnswer } </font></h4></p>
							<p><h4 class="bars">Correct answer：${questionRecordList.question.answer }</h4></p>
							<p><h4 class="bars"><font color="red">Explanation:${questionRecordList.question.answerDetail }</font></h4></p>
						</div>
					</div>
				</c:if> --%>
				
			</c:forEach>
	</div>
<!-- </div> -->
<script src="${ctx}/js/bootstrap.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$().UItoTop({ easingType: 'easeOutQuart' });
	});
</script>
<a href="#" id="toTop" style="display: block;"> <span id="toTopHover" style="opacity: 1;"> </span></a>
</body>
</html>
