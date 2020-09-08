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
<div class="title"><h2>Grade Management</h2></div>
<form action="${ctx}/deleteGrade.action" method="post" name="myform" id="myform">
<div class="table-operate ue-clear">
	<a href="#" class="add" onclick="addGrade()">Add</a>
    <a href="javascript:;" class="del" onclick="deleteGrade()"><font color='red'>Del</font></a>
</div>
<div class="table-box" id="myDiv">
	<table border="1" cellspacing="1">
    	<thead>
        	<tr>
        		<th class="num"></th>
        		<th class="name">No.</th>
                <th class="name">Name</th>
                <th class="process">Status</th>
                <th class="process">Curriculum</th>
                <th class="operate">Operation</th>
            </tr>
        </thead>
        <tbody align="center">
        	<c:forEach items="${dataList}" var="o">
				<tr align="center">
					<td><input type="checkbox" name="gradeId" value="${o.gradeId}"/></td>
					<td>${o.gradeId}</td>
					<td>${o.gradeName}</td>
					<td>
						<c:if test="${o.gradeStatus==0}">Deactivated</c:if>
						<c:if test="${o.gradeStatus==1}">Active</c:if>
					</td>
					<td>${o.courseId}</td>
					<td class="operate">
						<a href="${ctx}/toUpdGrade.action?gradeId=${o.gradeId}" class="edit"><font color='green'>Edit</font></a>
						<%-- <a href="${ctx}/toQryGrade.action?gradeId=${o.gradeId}" class="edit">Details</a> --%>
						<a href="${ctx}/deleteGrade.action?gradeId=${o.gradeId}" class="del"><font color='red'>Del</font></a>
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
			url:"${ctx}/qryAllGrade.action",
			method:"post",
			dataType: "json",
			data:{page:page+1},
			success: function(data){
				var html = "";
 				html += "<div class='table-box' id='myDiv'>";
				html += "<table border='1' cellspacing='1'>";
				html += "<thead>";
				html += "<th class='num'></th>";
				html += "<th class='name'>No.</th><th class='operate'>Name</th>";
				html += "<th class='time'>Curriculum</th><th class='operate'>Operation</th>";
				html += "</thead>";
				html += "<tbody align='center'>";
				
				for(dataList in data){
					html += "<tr align='center'>";
					html += "<td><input type='checkbox' name='gradeId' value='"+data[dataList].gradeId+"'/></td>";
					html += "<td>"+data[dataList].gradeId+"</td>";
					html += "<td>"+data[dataList].gradeName+"</td>";
					html += "<td>"+data[dataList].courseId+"</td>";
					html += "<td class='operate'><a href='${ctx}/toUpdGrade.action?gradeId="+data[dataList].gradeId+"' class='del'><font color='green'>Edit</font></a>&nbsp;";
					html += "<a href='${ctx}/deleteGrade.action?gradeId="+data[dataList].gradeId+"' class='del'><font color='red'>Del</font></a></td>";
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

function deleteGrade(){
	var del = confirm("Sure to delete?");
	if (!del){
		return;
	}
	var ids = "";
	$("input:checkbox[name='gradeId']:checked").each(function() {
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

function addGrade(){
	document.myform.attributes["action"].value = "${ctx}/toAddGrade.action"; 
	$("form").submit();
}

$("tbody").find("tr:odd").css("backgroundColor","#eff6fa");

showRemind('input[type=text], textarea','placeholder');
</script>
</html>