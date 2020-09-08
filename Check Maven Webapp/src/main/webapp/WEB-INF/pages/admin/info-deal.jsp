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
<link rel="stylesheet" href="${ctx}/css/info-mgt.css" />
<link rel="stylesheet" href="${ctx}/css/WdatePicker.css" />
<title>iframe</title>
</head>

<body>
<div class="title"><h2>Account Verification</h2></div>
<form action="${ctx}/admin/failinfo.action" method="post" name="myform" id="myform">
<div class="table-operate ue-clear">
	<a href="#" class="add" onclick="addUser()"><font color="green">Verify</font></a>
    <a href="javascript:;" class="del" onclick="deleteUser()">close</a>
</div>
<div class="table-box" id="myDiv">
	<table border="1" cellspacing="1">
    	<thead>
        	<tr>
        		<th class="num"></th>
        		<th class="name">Username</th>
                <th class="name">Nickname</th>
                <th class="name">Type</th>
                <th class="process">Grade</th>
                <th class="process">Curriculum</th>
                <th class="name">Status</th>
                <th class="node">Email</th>
                <th class="time">Contact Phone</th>
                <th class="operate">Operation</th>
            </tr>
        </thead>
        <tbody align="center">
        	<c:forEach items="${dataList}" var="o">
				<tr align="center">
					<td><input type="checkbox" name="userId" value="${o.userId}"/></td>
					<td>${o.userId}</td>
					<td>${o.userName}</td>
					<td>
						<c:if test="${o.userType==0}">Student</c:if>
						<c:if test="${o.userType==1}">Teacher</c:if>
						<c:if test="${o.userType==2}">Admin</c:if>
					</td>
					<td>
						<c:forEach items="${grade}" var="grade">
					  		<c:if test="${grade.gradeId == o.grade}">
								${grade.gradeName }
							</c:if>
						</c:forEach>
					</td>
					<td>
						<c:forEach items="${course}" var="course">
						  	<c:if test="${course.courseId == o.curriculum}">
						  		${course.courseName }
						  	</c:if>
						</c:forEach>
					</td>
					<td>
						<c:if test="${o.userState==0}"><font color="blue">To verify</font></c:if>
						<c:if test="${o.userState==1}">Active</c:if>
						<c:if test="${o.userState==2}"><font color="red">Deactivated</font></c:if>
						<c:if test="${o.userState==3}"><font color="red">Disqualified</font></c:if>
					</td>
					<td>${o.email}</td>
					<td>${o.telephone}</td>
					<td class="operate">
						<a href="javascript:;" class="del" onclick="addUser()"><font color='green'>Verify</font></a>
						<a href="javascript:;" class="count" onclick="showDetail('+${o.userId}+')">Details</a>
						<a href="javascript:;" class="edit" onclick="deleteUser()"><font color='red'>Deactivate</font></a>
					</td>
				</tr>
			</c:forEach>
        </tbody>
    </table>
</div>
<div class="pagination ue-clear"></div>
</form>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="${ctx}/js/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.pagination.js"></script>
<script type="text/javascript">
$(".select-title").on("click",function(){
	$(".select-list").hide();
	$(this).siblings($(".select-list")).show();
	return false;
})
$(".select-list").on("click","li",function(){
	var txt = $(this).text();
	$(this).parent($(".select-list")).siblings($(".select-title")).find("span").text(txt);
})

$('.pagination').pagination(${pageInfo.total},{
	callback: function(page){
		$.ajax({
			url:"${ctx}/admin/qryFindPending.action",
			method:"post",
			dataType: "json",
			data:{page:page+1},
			success: function(data){
				var html = "";
				html += "<div class='table-box' id='myDiv'>";
				html += "<table border='1' cellspacing='1'>";
				html += "<thead>";
				html += "<th class='num'></th>";
				html += "<th class='name'>Username</th><th class='operate'>Nickname</th>";
				html += "<th class='process'>Type</th><th class='process'>Status</th><th class='node'>Email</th>";
				html += "<th class='time'>Contact Phone</th><th class='operate'>Operation</th>";
				html += "</thead>";
				html += "<tbody align='center'>";
				for(dataList in data){
					html += "<tr align='center'>";
					html += "<td><input type='checkbox' name='userId' value='"+data[dataList].userId+"'/></td>";
					html += "<td>"+data[dataList].userId+"</td>";
					html += "<td>"+data[dataList].userName+"</td>";
					if(data[dataList].userType == 0){
						html += "<td>Student</td>";
					}else if(data[dataList].userType == 1){
						html += "<td>Teacher</td>";
					}else{
						html += "<td>Admin</td>";
					}
					if(data[dataList].userState == 0){
						html += "<td><font color='blue'>To verify</font></td>";
					}else if(data[dataList].userState == 1){
						html += "<td>Active</td>";
					}else if(data[dataList].userState == 2){
						html += "<font color='red'>Deactivated</font>";
					}else{
						html += "<font color='red'>Disqualified</font>";
					}
					html += "<td>"+data[dataList].email+"</td>";
					html += "<td>"+data[dataList].telephone+"</td>";
					html += "<td class='operate'><a href='${ctx}/admin/passinfo.action?userId="+data[dataList].userId+"' class='del'><font color='green'>Verify</font></a>&nbsp;";
					html += "<a href='${ctx}/admin/toQryUser.action?userId="+data[dataList].userId+"' class='del'>Details</a>&nbsp;";
					html += "<a href='${ctx}/admin/failinfo.action?userId="+data[dataList].userId+"' class='del'><font color='red'>Deactivate</font></a></td>";
					html += "</tr>";
				}
				html += "</tbody>"; 
				html += "</table>";
				html += "</div>";
		        $("#myDiv").html("");
		        $("#myDiv").html(html);
		        $("tbody").find("tr:odd").css("backgroundColor","#eff6fa");
		    }
		});
	},
	display_msg: true,
	setPageNo: true
});

//不通过审核,即注销用户信息
function deleteUser(){
	var del = confirm("Sure to delete?");
	if (!del){
		return;
	}
	var ids = "";
	$("input:checkbox[name='userId']:checked").each(function() {
		ids += $(this).val() + ",";
    });
	//判断最后一个字符是否为逗号，若是截取
	var id = ids.substring(ids.length -1, ids.length);
	if(id == ","){
		ids = ids.substring(0, ids.length-1);
	}
	if(ids == ""){
		alert("Select at least one entry.");
		return;
	}
	$("form").submit();
}
//通过审核
function addUser(){
	var del = confirm("Sure to add?");
	if (!del){
		return;
	}
	var ids = "";
	$("input:checkbox[name='userId']:checked").each(function() {
		ids += $(this).val() + ",";
    });
	//判断最后一个字符是否为逗号，若是截取
	var id = ids.substring(ids.length -1, ids.length);
	if(id == ","){
		ids = ids.substring(0, ids.length-1);
	}
	if(ids == ""){
		alert("Select at least one entry.");
		return;
	}
	//  以下三行，随便哪一行都行         
/*	$("#myform").action="${ctx}/admin/toAddUser.action";
 	document.myform.action=‘new_url’;*/
	document.myform.attributes["action"].value = "${ctx}/admin/passinfo.action"; 
	$("form").submit();
}

function showDetail(id){
	document.myform.attributes["action"].value = "${ctx}/admin/toQryUser.action?userId="+id; 
	$("form").submit();
}

$("tbody").find("tr:odd").css("backgroundColor","#eff6fa");

showRemind('input[type=text], textarea','placeholder');
</script>
</html>