<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>环境信息列表</title>
    
 	<meta charset="UTF-8">
    
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/swh_admin.js"></script>

  </head>
  
  <body>
	<div id="envListMain">
		<table id="envList"></table>	<!-- 环境信息列表 -->
	</div>
	<script type="text/javascript">
		$(function(){
		$("#envListMain").dialog({
			title: "环境参数列表",
			width: 1024,
			height: 600,
			closed: false,
			shadow: true,	//是否显示阴影
			modal: true,	//是否窗口化
			cache: false,	//是否缓存
			inline: true,	//设置是否在父容器中显示，不在会出现很多问题
			resizable: false	//是否可改变大小
		});
	
		$("#envList").datagrid({
			width: "100%",
			height: "100%",
			method: "post",
			
			url: getContextPath()+"/console/sys/envList.do",
			
			onLoadSuccess: function(data){
				$(".editBtn").linkbutton({plain:true,height:23, iconCls:'icon-edit'});
			},
			
			pagination: true,	//是否显示分页工具栏
			pagePosition: "bottom",	//设置分页工具的位置
			pageSize: 15,		//一页记录数
			pageList: [15,20,30],	//页面大小选择列表
			striped:true,			//斑马线样式,
			nowrap : true,		//是否在一行显示所有，自动换行
			loadMsg:"努力加载中，请稍后。。。。",//加载信息
			rownumbers: true,		//显示行号
			singleSelect: true,		//只允许选择一行
			showHeader : true,//显示行头，默认true;
			emptyMsg: "no records found",
			
			
			columns: [[
			     {
			    	  field: "envKey",
			    	  title: "参数名",
			    	  width: "33%",
			    	  align: "center",
			    	  resizable: false
			      },
			      {
			    	  field: "envValue",
			    	  title: "参数值",
			    	  width: "25%",
			    	  align: "center",
			    	  resizable: false
			      },
			      {
			      	  field: "remark",
			    	  title: "备注",
			    	  width: "25%",
			    	  align: "center",
			    	  resizable: false
			      },
			      {
			    	  field: "opt1",
			    	  title: "操作",
			    	  width: "20%",
			    	  formatter: function(value,row,index){
			    		  if(value=="找不到数据"){
			    			  return "";
			    		  }
			    		  return "<a href='#' class='editBtn' onclick='edit("+row.id+","+index+");'>编辑</a>";
			    	  },
			    	  align: "center",
			    	  resizable: true
			      }
			 ]], 
		});
	});
	
	function edit(envId,index){
		envInfoDiv=$("<div id=\"envInfoDiv\"></div>");
		envInfoDiv.appendTo("body");
		$("#envInfoDiv").dialog({
			title: "编辑",
			width: 512,
			height: 300,
			href: getContextPath()+"/console/sys/updateEnvUI.do?id="+envId,
			closed: false,
			modal: true,
			onClose: function(){
				$("#envList").datagrid("reload");
				$("#envList").datagrid("selectRow",index);
				envInfoDiv.remove();
			} 
		});
	};
	</script>
  </body>
</html>
