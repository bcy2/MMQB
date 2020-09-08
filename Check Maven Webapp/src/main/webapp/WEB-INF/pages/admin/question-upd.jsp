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
<div class="title"><h2>Edit Question</h2></div>
<form action="${ctx}/updQuestion.action" method="post" name="myform" id="myform">
<div class="main">
	<p class="short-input ue-clear">
    	<label>No.:</label>
        <input type="text" name="questionId" id="questionId" value="${question.questionId}" readonly="readonly"/>
    </p>
    <p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Cur.:</label>
    	<select name="courseId" id="courseId">
            <option value="" disabled="disabled">Please choose...</option>
	        <c:forEach items="${course}" var="course">
				<option value="${course.courseId }" <c:if test="${course.courseId == question.courseId}">selected</c:if>>${course.courseName }</option>
			</c:forEach>
		</select>
	</p>
	<p class="short-input ue-clear">
    	<label><span style="color:red">*</span>Grade:</label>
    	<select name="gradeId" id="gradeId">
	        <c:forEach items="${grade}" var="grade">
				<option data-parent="${grade.courseId }" value="${grade.gradeId }" <c:if test="${grade.gradeId == question.gradeId}">selected</c:if>>${grade.gradeName }</option>
			</c:forEach>
		</select>
    </p>
    <div class="short-input select ue-clear">
    	<label><span style="color:red">*</span>Difficulty:</label>
    	<input name="difficulty" type="radio" value="0.33" <c:if test="${question.difficulty <= 0.33}">checked="checked"</c:if>/>Easy
    	<input name="difficulty" type="radio" value="0.66" <c:if test="${question.difficulty > 0.33 && question.difficulty < 0.67}">checked="checked"</c:if>/>Medium
    	<input name="difficulty" type="radio" value="0.99" <c:if test="${question.difficulty >= 0.67}">checked="checked"</c:if>/>Difficult
    </div>
    <div class="long-input select ue-clear">
    	<label><span style="color:red">*</span>Type:</label>
    	<c:forEach items="${type}" var="typeInfo">
    		<c:if test="${typeInfo.typeId eq question.typeId }">
				<input id="typeId" name="typeId" type="radio" onclick="typeOnclick()" value="${typeInfo.typeId}" checked="checked"/>${typeInfo.typeName}
			</c:if>
		</c:forEach>
    </div>
    <p class="long-input ue-clear">
    	<label>Raw Ques ID:</label>
        <input type="number" placeholder="Raw question ID, put 0 if inapplicable" name="rawQuestionId" id="rawQuestionId" value="${question.rawQuestionId}" readonly="readonly"/>
        <span style="color: red" id="tipInfo">${message }</span>
    </p>
    <p class="long-input ue-clear">
    	<label>Question:</label>
        <input type="text" placeholder="Type in the question" name="quesName" id="quesName" value="${question.quesName}"/>
        <span style="color: red" id="tipInfo">${message }</span>
    </p>
    <c:if test="${question.typeId == 1 || question.typeId == 3}">
	    <p class="long-input ue-clear" id="pa">
	    	<label>Option A:</label>
	        <input type="text" placeholder="Option A" name="optionA" id="optionA" value="${question.optionA}"/>
	        <span style="color: red" id="tipInfo">${message }</span>
	    </p>
	    <p class="long-input ue-clear" id="pb">
	    	<label>Option B:</label>
	        <input type="text" placeholder="Option B" name="optionB" id="optionB"  value="${question.optionB}"/>
	        <span style="color: red" id="tipInfo">${message }</span>
	    </p>
	    <p class="long-input ue-clear" id="pc">
	    	<label>Option C:</label>
	        <input type="text" placeholder="Option C" name="optionC" id="optionC"  value="${question.optionC}"/>
	        <span style="color: red" id="tipInfo">${message }</span>
	    </p>
	    <p class="long-input ue-clear" id="pd">
	    	<label>Option D:</label>
	        <input type="text" placeholder="Option D" name="optionD" id="optionD"  value="${question.optionD}"/>
	        <span style="color: red" id="tipInfo">${message }</span>
	    </p>
    </c:if>
    <p class="long-input ue-clear">
    	<label>Answer:</label>
        <input type="text" placeholder="Answer" id="answer" name="answer" value="${question.answer}"/>
    </p>
    <p class="long-input ue-clear">
    	<label>Attach. ID:</label>
        <input type="number" placeholder="Attachment ID" id="attachmentId" name="attachmentId" value="${question.attachmentId}" readonly="readonly"/>
    </p>
    <p class="long-input ue-clear">
    	<label>Explanation:</label>
        <input type="text"id="answerDetail" placeholder="Explanation" name="answerDetail" value="${question.answerDetail}"/>
    </p>
<%--     <p class="long-input ue-clear">
    	<label>Remarks:</label>
        <input type="text" id="remark" name="remark" placeholder="Remarks" value="${question.remark}"/>
    </p> --%>
    <div class="short-input select ue-clear">
    	<label>In past paper?</label>
    	<input name="pastPaper" type="radio" value="0" <c:if test="${!question.pastPaper}">checked="checked"</c:if>/>No
    	<input name="pastPaper" type="radio" value="1" <c:if test="${question.pastPaper}">checked="checked"</c:if>/>Yes
    </div>

</div>
</form>
<div class="btn ue-clear">
	<a href="javascript:;" class="confirm" onclick="updQuestion()">Update</a>
    <a href="${ctx}/toQuestionPage.action" class="clear">Cancel</a>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="${ctx}/js/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.searchableSelect.js"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
	// 获取子select的option
	let childOptions = $("select[name='gradeId']").find("option");
/* 	childOptions.each(function() {
		this.remove();
	}); */
	$("select[name='courseId']").change(cascadeSelect);

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
		$("select[name='gradeId']").empty().append(options);
		// $("select[name='grade']").find("option[value='']").prop(
		// "selected", true);
	}
});

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
		$("#tipInfo").html("Question is empty.");
		$("#questionId").focus();
		return;
	}else{
		$("#tipInfo").html("");
	}
}

//注册
function updQuestion(){
	document.myform.attributes["action"].value = "${ctx}/updQuestion.action"; 
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
	alert("Clear all!");
}

showRemind('input[type=text], textarea','placeholder');
</script>
</html>