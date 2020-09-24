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
<div class="title"><h2>Type Management</h2></div>
<form action="${ctx}/deleteType.action" method="post" name="myform" id="myform">
<div class="table-operate ue-clear">
	<a href="#" class="add" onclick="addType()">Add</a>
    <a href="javascript:;" class="del" onclick="deleteUser()"><font color='red'>Del</font></a>
</div>
<div class="table-box" id="myDiv">
	<table border="1" cellspacing="1">
    	<thead>
        	<tr>
        		<th class="num"></th>
        		<th class="name">No.</th>
                <th class="name">Type</th>
                <th class="process">Scores</th>
                <th class="process">Remarks</th>
                <th class="operate">Operation</th>
            </tr>
        </thead>
        <tbody align="center">
        	<c:forEach items="${dataList}" var="o">
				<tr align="center">
					<td><input type="checkbox" name="typeId" value="${o.typeId}"/></td>
					<td>${o.typeId}</td>
					<td>${o.typeName}</td>
					<td>${o.score}</td>
					<td>${o.remark}</td>
					<td class="operate">
						<a href="${ctx}/toUpdType.action?typeId=${o.typeId}" class="edit"><font color='green'>Edit</font></a>
						<a href="${ctx}/toQryType.action?typeId=${o.typeId}" class="count">Details</a>
						<a href="${ctx}/delType.action?typeId=${o.typeId}" class="del"><font color='red'>Del</font></a>
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
			url:"${ctx}/qryTypePage.action",
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
				html += "<th class='time'>Scores</th><th class='time'>Remarks</th><th class='operate'>Operation</th>";
				html += "</thead>";
				html += "<tbody align='center'>";
				
				for(dataList in data){
					html += "<tr align='center'>";
					html += "<td><input type='checkbox' name='typeId' value='"+data[dataList].typeId+"'/></td>";
					html += "<td>"+data[dataList].typeId+"</td>";
					html += "<td>"+data[dataList].typeName+"</td>";
					html += "<td>"+data[dataList].score+"</td>";
					html += "<td>"+data[dataList].remark+"</td>";
					html += "<td class='operate'><a href='${ctx}/toUpdType.action?typeId="+data[dataList].typeId+"' class='del'><font color='green'>Edit</font></a>&nbsp;";
					html += "<a href='${ctx}/toQryType.action?typeId="+data[dataList].typeId+"' class='del'>Details</a>&nbsp;";
					html += "<a href='${ctx}/delType.action?typeId="+data[dataList].typeId+"' class='del'><font color='red'>Del</font></a></td>";
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

function deleteUser(){
	var del = confirm("Sure to delete?");
	if (!del){
		return;
	}
	var ids = "";
	$("input:checkbox[name='typeId']:checked").each(function() {
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

function addType(){
	document.myform.attributes["action"].value = "${ctx}/toAddType.action"; 
	$("form").submit();
}

$("tbody").find("tr:odd").css("backgroundColor","#eff6fa");

showRemind('input[type=text], textarea','placeholder');
</script>
</html>