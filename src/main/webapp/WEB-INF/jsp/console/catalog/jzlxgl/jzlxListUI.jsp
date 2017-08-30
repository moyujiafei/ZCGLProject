<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>建筑类型列表</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/swh_admin.js"></script>
	<style type="text/css">
	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
</style>
  </head>
  <body>
  <script type="text/javascript">
  
 
  	$("#dlgJzlxList").dialog({
  		title: '建筑类型列表',    
	    width: 1024,    
	    height: 600,    
	    closed: false,
	    cache: false,    
	    shadow:true,		//显示阴影
	    resizable:false,	//不允许改变大小
	    modal: true,   	//是否窗口化
	    inline:true,			//是否在父容器中，若不在会出现很多BUG
	    left:10,    
		top:100,   
  	});
  	$("#dlgJzlxList").window('center');
  	$("#tblJzlxListDataGrid").datagrid({
  		width:"100%",
		height:"100%",
		method:"post",
		url:getContextPath()+"/console/catalog/jzlxgl/jzlxList.do",
		//表单项
		columns : [[
			
			{	
				width:"29%",
				field:"mc",
				title:"建筑类型",
				halign:"center",
				align:"center",
				resizable:false,
			},
			
			{	
				width:"73%",
				field:"remark",
				title:"备注",
				halign:"center",
				align:"left", 
				resizable:false,
			}
		]],
		
		
		pagination:true,					//是否有分页工具
		pagePosition:"bottom",			//分页工具位置
		pageSize:15,					//分页默认大小
		pageList:[15],
		striped:true,			//斑马线样式,
		nowrap : true,		//是否在一行显示所有，自动换行
		loadMsg:"努力加载中，请稍后。。。。",//加载信息
		rownumbers:true,	//是否显示行号
		singleSelect:true,	//只能同时选择一行
		showHeader : true,//显示行头，默认true;
		emptyMsg: "no records found",
		
  	});
  </script>
<div id="dlgJzlxList">
	<table id ="tblJzlxListDataGrid"></table>
	</div>
</div>
  </body>
</html>

