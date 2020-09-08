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
<div class="title"><h2>Question Details</h2></div>
<div class="main">
	<p class="short-input ue-clear newstyle">
    	<label>No.:</label>${question.questionId}
    </p>
	<p class="long-input ue-clear newstyle">
    	<label>Question:</label>${question.quesName}
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Curriculum:</label>${question.courseId}
    </p>
	<p class="short-input ue-clear newstyle">
    	<label>Grade:</label>${question.gradeId}
    </p>
    <p class="long-input ue-clear newstyle">
    	<label>Difficulty:</label>
    	<c:if test="${question.difficulty <= 0.33}">Easy</c:if>
    	<c:if test="${question.difficulty > 0.33 && question.difficulty < 0.67}">Medium</c:if>
    	<c:if test="${question.difficulty >= 0.67}">Difficult</c:if>
    </p>
    <p class="long-input ue-clear newstyle">
    	<label>Type:</label>${question.typeId}
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Option A:</label>${question.optionA}
    </p>   
    <p class="short-input ue-clear newstyle">
    	<label>Option B:</label>${question.optionB}
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Option C:</label>${question.optionC}
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Option D:</label>${question.optionD}
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Answer:</label>${question.answer}
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Attachment:</label>${question.attachmentId}
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>Explanation:</label>${question.answerDetail}
    </p>
    <p class="short-input ue-clear newstyle">
    	<label>In past paper?</label>
    	<c:if test="${question.pastPaper}">Yes</c:if>
    	<c:if test="${!question.pastPaper}">No</c:if>
    </p>
<%--     <p class="short-input ue-clear newstyle">
    	<label>Remarks:</label>${question.answerDetail}
    </p> --%>
</div>
<div class="btn ue-clear">
	<a href="${ctx}/toQuestionPage.action" class="confirm">Back</a>
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

showRemind('input[type=text], textarea','placeholder');
</script>
</html>