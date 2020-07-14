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
	 java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	 java.util.Date currentTime = new java.util.Date();//得到当前系统时间  
	 String str_date1 = formatter.format(currentTime); //将日期时间格式化  
	 String str_date2 = currentTime.toString(); //将Date型日期时间转换成字符串形式  
	 request.setAttribute("starttime ", str_date1);
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
<link href="${ctx}/css/time.css" rel="stylesheet" type="text/css" media="all" />
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
	var beginTime ="";
	var startFalg = true;
	var intDiff = parseInt(60*60*2);//倒计时总秒数量
	function timer(intDiff){
		window.setInterval(function(){
		var day=0,
			hour=1,
			minute=30,
			second=0;//时间默认值		
		if(intDiff > 0){
			startFalg =false;
			day = Math.floor(intDiff / (60 * 60 * 24));
			hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
			minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
			second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
		}else{
			startFalg = true;
		}
		if (minute <= 9) minute = '0' + minute;
		if (second <= 9) second = '0' + second;
		$('#day_show').html(day+"天");
		$('#hour_show').html('<s id="h"></s>'+hour+'时');
		$('#minute_show').html('<s></s>'+minute+'分');
		$('#second_show').html('<s></s>'+second+'秒');
		intDiff--;
		}, 1000);
	} 
	function doStart(){
		beginTime= getNowFormatDate();
		$("#sp_start").html("考试中.....");
		if(startFalg)
			timer(intDiff);
	}	
	
	function exitSystem(){
		window.location.href = "${ctx}/user/exitSys.action"	;
	}
	
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	    return currentdate;
	}
	
	function submitPaper(){
		/* var beginTime = getNowFormatDate(); */
		var paperName = $("#paperName").val();
		var paperId = $("#paperId").val();
		var score = $('#myForm').serialize();
		$.post("${ctx}/dealPaper.action", 
				{
					"beginTime":beginTime,
					"paperName":paperName,
					"paperId":paperId,
					"score":score,
				},
		function(data){
			alert(data.errorInfo);
			document.myForm.attributes["action"].value = "${ctx}/toScoreQry.action?userId=${user.userId}"; 
			$("form").submit(); 
		},"json");
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
							<li><a class="hvr-overline-from-center button2 active" href="${ctx}/toScoreQry.action?userId=${user.userId}">Review quizzes</a></li>
							<li><a class="hvr-overline-from-center button2" href="${ctx}/toMyBooksPage.action?userId=${user.userId}">Question record</a></li>
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

<!-- 	<div class="time-item">
		<table class="table">
		  <thead>
			<tr>
				<th>
					<span id="day_show">0天</span>
					<strong id="hour_show">0时</strong>
					<strong id="minute_show">0分</strong>
					<strong id="second_show">0秒</strong>
				</th>
				<th>
					<a href="#" onclick="doStart()"><span class="label label-primary" id="sp_start">开始考试</span></a>
				</th>
			</tr>
		  </thead>
		</table>
	</div> -->
	 <form action="${ctx}/dealPaper.action" method="post" id="myForm" name="myForm">
<!-- <div class="typrography"> -->
	 <div class="container">
	 		<!-- 试卷名称 -->
	 		<br />
			<h2 class="bars" align="center"><font color="blue">${paper.paperName }</font></h2>
			<h4 class="bars" align="center">Expected time: <font color="green">${paper.allowTime }min</font></h4>
			<h4 class="bars" align="center">Actual time: <font color="green">${paper.allowTime }min</font></h4>
				<%-- <input type="hidden" name="currentQ" id="currentQ" value="${currentQuestion}"/> --%>
				<input type="hidden" name="paperId" id="paperId" value="${paper.paperId }"/>
				<div class="question-group">
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
							<%-- <p><h4 class="bars">From quiz: <strong>${questionRecordList.quizName }</strong> / Question grade: <strong>${questionRecordList.gradeName }</strong></h4></p> --%>
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
							<%-- <p><h4 class="bars">From quiz: <strong>${questionRecordList.quizName }</strong> / Question grade: <strong>${questionRecordList.gradeName }</strong></h4></p> --%>
						</div>
					</div>
				</c:if>
				
			</c:forEach>
				<%-- <c:forEach items="${questionList}" var="quesType" varStatus="loop">
				<p><h4 class="bars" align="left">${loop.index+1}.</h4></p>
					<c:if test="${quesType.typeId == 1}">
						<p><h4 class="bars" align="left">${fn:replace(quesType.quesName, newLine, "<br />")}</h4></p>
						<c:if test="${quesType.attachmentId != 0}">
							<img src="data:image/png;base64,${quesType.attachmentFile}">
						</c:if>
						<div class="input-group">
							<input name="${quesType.questionId }" type="radio" value="A"/>&nbsp;<font size="4">${quesType.optionA }</font></br>
							<input name="${quesType.questionId }" type="radio" value="B"/>&nbsp;<font size="4">${quesType.optionB }</font></br>
							<input name="${quesType.questionId }" type="radio" value="C"/>&nbsp;<font size="4">${quesType.optionC }</font></br>
							<input name="${quesType.questionId }" type="radio" value="D"/>&nbsp;<font size="4">${quesType.optionD }</font></br>
						</div>
					</c:if>
					
					<c:if test="${quesType.typeId == 2}">
						<p><h4 class="bars" align="left">${fn:replace(quesType.quesName, newLine, "<br />")}</h4></p>
						<c:if test="${quesType.attachmentId != 0}">
							<img src="data:image/png;base64,${quesType.attachmentFile}">
						</c:if>
						<div class="input-group">
						  <span class="input-group-addon" id="sizing-addon2">Answer:</span>
						  <input type="text" name="${quesType.questionId }" id="${quesType.questionId }" 
						  		class="form-control" placeholder="Type in your answer here">
						  <br />
						</div>
					</c:if>
					
					<c:if test="${quesType.typeId == 5}">
						<p><h4 class="bars" align="left">${fn:replace(quesType.quesName, newLine, "<br />")}</h4></p>
						<div class="input-group">
						  <span class="input-group-addon" id="sizing-addon2">Answer:</span>
						  <input type="text" name="${quesType.questionId }" id="${quesType.questionId }" 
						  		class="form-control" placeholder="Type in your answer here">
						  <br />
						</div>
					</c:if>
					<hr>
				</c:forEach> --%>
			
				</div>
		<div class="grid_3 grid_5" align="center">
		  <h3 class="t-button">
			<!-- <a href="javascript:;" onclick="submitPaper()"><span class="label label-success">提交试卷</span></a>&nbsp;&nbsp; -->
			<a href="${ctx}/toScoreQry.action?userId=${user.userId}"><span class="label label-info">Back to quiz review</span></a>
		  </h3>
      </div>
	</div>
</form>	  
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
