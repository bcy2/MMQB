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
<div class="title"><h2>Quiz Details</h2></div>
<form action="${ctx}/addQuesInfo.action" method="post" name="myform" id="myform">
<div class="main">
	<p class="short-input ue-clear newstyle">
    	<label>No.:</label><span style="color:blue" size="3">${paper.paperId }</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Creator:</label><span style="color:blue" size="3">${paper.userId }</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>QuizName:</label><span style="color:blue" size="3">${paper.paperName }</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Allow Time:</label><span style="color:blue" size="3">${paper.allowTime } min</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Curriculum:</label><span style="color:blue" size="3">${paper.courseId }</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Grade:</label>
    	<span style="color:blue" size="3">
    		<c:forEach items="${grade}" var="grade">
				<c:if test="${grade.gradeId == paper.gradeId}">
					${grade.gradeName }
				</c:if>
			</c:forEach>
    	</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Question IDs:</label><span style="color:blue;max-width: 75%;overflow-wrap: break-word;" size="3">${paper.questionId }</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Current Ques:</label><span style="color:blue;max-width: 75%;overflow-wrap: break-word;" size="3">${paper.currentQuestion }</span>
    </p>
<%--     <p class="short-input ue-clear newstyle">
    	<label>选择题数量:</label><span style="color:blue" size="3">${paper.score }</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>填空题数量:</label><span style="color:blue" size="3">${paper.beginTime }</span>
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>简答题数量:</label><span style="color:blue" size="3">${paper.endTime }</span>
    </p> --%>
</div>
</form>
<div class="btn ue-clear">
    <a href="${ctx}/toPaperPage.action" class="confirm">Back</a>
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

function checkUserId(){
	var questionId = $("#questionId").val();
	if(questionId == ""){
		$("#tipInfo").html("Question ID empty.");
		$("#questionId").focus();
		return;
	}else{
		$("#tipInfo").html("");
	}
}

//注册
function addQuestion(){
	document.myform.attributes["action"].value = "${ctx}/addPaper.action"; 
	$("form").submit();
}

function typeOnclick(){
	var typeId = $("input[name='typeId']:checked").val();
	if(typeId == "1" || typeId == "2"){//选择题就显示
		showOp();
	}else{
		hideOp();
	}
}

//隐藏选择题选项
function hideOp(){
	$("#pa").hide();
	$("#pb").hide();
	$("#pc").hide();
	$("#pd").hide();
}

//显示选择题选项
function showOp(){
	$("#pa").show();
	$("#pb").show();
	$("#pc").show();
	$("#pd").show();
}

//情况所有
function clearAll(){
}

showRemind('input[type=text], textarea','placeholder');
</script>
</html>