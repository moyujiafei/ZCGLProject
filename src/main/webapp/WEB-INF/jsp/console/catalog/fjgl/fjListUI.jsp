<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>房间管理</title>
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
  	<div id="fjListMain">
  	  	 <table id="fjListDatagrid"></table>
  	 <div id="divFjToolbar">
	 	 <table> 
	 	 	<tr>
		 	 	<td><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add"  onclick="objFJ.add()">新增</a></td>
		 	 	<td style="padding-left:20px"><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-import"  onClick="objFJ.fjExcelImport()">导入</a></td>
		 	 	<td style="padding-left:20px"><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-export"  onClick="objFJ.fjExcelExport()">导出</a></td>
		 	 	<td style="padding-left:530px;padding-right:10px">建筑物 : </td>
		 	 	<td><input id="jzwSearchBox" name="jzw" style="width:125px" onClick="doSearch()"/></td>
		 	 	<td style="padding-left:10px"><a href="#" class="easyui-linkbutton" id="fjSearchBtn">查询</a></td>
	 	 	</tr>
	 	 </table>
  	 </div> 
  	 <form id="downLoadForm" method="post">
		<input type="hidden" name="randomInfo" value="<%=Math.random()%>"/>
	 </form>
  	</div>
 <script type="text/javascript">
 		var objFJ = {
 				add : function(){
 					var insertFJ = $("<div id='insertFJUI'></div>");
 					insertFJ.appendTo("body");
 					$("#insertFJUI").dialog({
 						title : "新增房间",
 						height : 300,
 						width : 512,
 						modal:true,
 						inline:true,
 						closed : false,
 						onClosed : function(){
 							insertFJ.remove();
 						},
 					});
 					$("#insertFJUI").dialog('open').dialog('refresh',getContextPath()+"/console/catalog/fjgl/insertFJUI.do");
 				},
 				edit : function(){
 					var editFJ = $("<div id='updateFJUI'></div>");
 					editFJ.appendTo("body");
 					$("#updateFJUI").dialog({
 						title : "更新房间",
 						height : 300,
 						width : 512,
 						modal:true,
 						inline:true,
 						closed : false,
 						onClosed : function(){
 							editFJ.remove();
 						},
 					});
 					$("#updateFJUI").dialog('open').dialog('refresh',getContextPath()+"/console/catalog/fjgl/updateFJUI.do");
 				},
 				dele : function(id){
 					$.messager.confirm('确认','您确认要删除该条记录吗？',function(sure){
 						$.post(getContextPath()+"/console/catalog/fjgl/delFJ.do?id="+id+"&").success(function(){
 							$("#fjListDatagrid").datagrid("reload");
 							$("#fjListDatagrid").datagrid("clearSelections");
 						}
 						).error(function(){
 							$.messager.show({title:'操作提示',msg:'删除失败！',showType:'show'});
 						});
 					});
 				},
 				fjExcelImport : function(){
 					var importFJ = $("<div id='importFJUI'></div>");
 					importFJ.appendTo("body");
 					$("#importFJUI").dialog({
 						title : '房间批量导入',
 						width : 512,
 						height : 200,
 						close : false,
 						modal : true,
 						inline : true,
 						onClose:function(){
 							importFJ.remove();
 						}
 					});
 					$("#importFJUI").dialog('open').dialog('refresh',getContextPath()+"/console/catalog/fjgl/importFJExcelUI.do");
 				},
 				fjExcelExport : function(){
 					$.messager.confirm("导出","确定导出房间表格吗？",function(result){
 						if(result){
 							downloadFJExcel();
 						}
 					});
 				},
 		};
 		
 		$("#fjListMain").dialog({
 			title : '房间管理',
 			height : 600,
 			width : 1024,
 			closed : false,
 			cache : false,
 			shadow : true,
 			resizable : false,
 			modal : true,
 			inline : true,  //是否在父容器中，若不在会出现很多BUG
 		});
 	 	$("#fjListDatagrid").datagrid({
 	  		width : "100%",
 			height : "100%",
 			method : "post",
 			url : getContextPath() + "/console/catalog/fjgl/fjList.do",
 			onLoadSuccess : function(data) {
 				$(".editBtn").linkbutton({
 					plain : true,
 					height : 21,
 					iconCls : 'icon-edit'
 				});
 				$(".editBtn").tooltip({
 					content : '编辑',
 					position : 'top',
 				});
 				$(".deleteBtn").linkbutton({
 					plain : true,
 					height : 21,
 					iconCls : 'icon-no'
 				});
 				$(".deleteBtn").tooltip({
 					content : '删除',
 					position : 'top',
 				});
 			},
 		columns : [[
 				{
 					width : "19%",
 					field : "jzw",
 					title : "建筑物",
 					halign : "center",
 					align : "center",
 					resizable : true,
 				},
 				{
 					width : "17%",
 					field : "floor",
 					title : "楼层",
 					halign : "center",
 					align : "center",
 					resizable : true,
 				},
 				{
 					width : "17%",
 					field : "room",
 					title : "房间",
 					halign : "center",
 					align : "center",
 					resizable : true,
 				},
 				{
 					width : "17%",
 					field : "deptName",
 					title : "所属部门",
 					halign : "center",
 					align : "center",
 					resizable : true,
 				},
 				{
 					width : "17%",
 					field : "glrmc",
 					title : "资产管理人",
 					halign : "center",
 					align : "center",
 					resizable : true,
 				},
 				{
 					width : "15%",
 					field : "opt1",
 					title : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
 					align : "left",
 	                formatter: function(value, row, index) {
 	                	return '&nbsp;&nbsp;<a href="#" class="editBtn" onclick="objFJ.edit()"></a>&nbsp;&nbsp;<a href="#" class="deleteBtn" onclick="objFJ.dele('+row.fjId+')" ></a>';
 	                },
 					resizable : false,
 				}, ] ],
 			pagination : true, //是否有分页工具
 			pagePosition : "bottom", //分页工具位置
 			pageSize : 15, //分页默认大小
 			pageList : [ 15 ],
 			striped : true, //斑马线样式,
 			nowrap : true, //是否在一行显示所有，自动换行
 			loadMsg : "努力加载中，请稍后。。。。",//加载信息
 			rownumbers : true, //是否显示行号
 			singleSelect : true, //只能同时选择一行
 			showHeader : true,//显示行头，默认true;
 			toolbar : "#divFjToolbar",
 			emptyMsg : "没有找到符合条件的记录",
 		});
 	  	$("#jzwSearchBox").textbox({
 	  		prompt : '请输入建筑物名称',
 	  	});
 		$("#fjSearchBtn").linkbutton({
 			plain: true,
 			iconCls: 'icon-search',
 			onClick: function (){  
 				doSearch();
 			}
 		});
 	  	function doSearch(){
 	  		var jzw = $("#jzwSearchBox").textbox("getValue");
 	  		var param ={"jzw":jzw};
 			$("#fjListDatagrid").datagrid("reload",param);
 	  	};
 		function downloadFJExcel(){
 			$("#downLoadForm").form('submit', {
 				url : getContextPath() + "/console/catalog/fjgl/exportFJExcel.do",
 				success: function(data){
 					if(data != "success"){
 						$.messager.alert('失败',data,'warning');
 					}
 				}
 			});
 		};
 </script>
 </body>
</html>
