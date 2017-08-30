<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
  <head>
    <title>资产部门信息列表</title>
  </head>
  
  <body>
	<div id="zcbmListMain">
	    <div id="divZCBMToolbar">
  	 	 <form id="downLoadForm" method="post">
			<input type="hidden" name="randomInfo" value="<%=Math.random()%>"/>
		 </form>
	 	 <table> 
	 	 	<tr>
	 	 	<td style="padding-left:20px"><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-export"  onclick="objZCBM.ZCBMExcelExport()">导出</a></td>
	 	 	</tr>
	 	 </table>
  	 </div>
		<table id="zcbmList"></table>	<!-- 资产部门信息列表 -->
	</div>
	<script type="text/javascript">
	      var objZCBM = {
 				ZCBMExcelExport : function(){
				$.messager.confirm('导出', '确定要导出资产部门表格吗？', function(r){
					if (r){
						downloadZCBMExcel();
					}
				});
			},
 		};
	
		$(function(){
		$("#zcbmListMain").dialog({
			title: "资产部门列表",
			width: 1024,
			height: 600,
			closed: false,
			shadow: true,	//是否显示阴影
			modal: true,	//是否窗口化
			cache: false,	//是否缓存
			inline: true,	//设置是否在父容器中显示，不在会出现很多问题
			resizable: false	//是否可改变大小
		});
	
		$("#zcbmList").datagrid({
			url: getContextPath()+"/console/catalog/zcbmgl/zcbmList.do",
			
			onLoadSuccess: function(data){
				$(".editBtn").linkbutton({plain:true,height:23, iconCls:'icon-edit'});
			},
			
			method: "post",
			fit: true,
            fitColumns: true,
            striped: true, //斑马线效果
            loadMsg: "数据正在加载当中，请稍等...", //加载数据时的提示消息
            pagination: true, //分页工具栏
            rownumbers: true, //显示行列号
            singleSelect: true, //只允许选中一行
            pageSize: 15, //设置分页时的初始化的分页大小
            pageList: [15], //选择分页显示行数的列表里面的值
            nowrap: true, //是否在一行显示所有，自动换行
            emptyMsg : "没有找到符合条件的记录",
			toolbar: "#divZCBMToolbar",
			columns: [[
			     {
			    	  field: "deptName",
			    	  title: "部门名",
			    	  width: "56%",
			    	  halign:"center",
			    	  align: "left",
			    	  resizable: false
			      },
			      {
			    	  field: "fzrmc",
			    	  title: "易耗品负责人",
			    	  width: "16%",
			    	  align: "center",
			    	  resizable: false
			      },
			      {
			      	  field: "glrmc",
			    	  title: "固定资产管理员",
			    	  width: "15%",
			    	  align: "center",
			    	  resizable: false
			      },
			      {
			    	  field: "opt1",
			    	  title: "操作",
			    	  halign:"center",
			    	  width: "15%",
			    	  align: "left",
			    	  formatter: function(value,row,index){
			    		  if(value=="找不到数据"){
			    			  return "";
			    		  }
			    		  return "&nbsp;&nbsp;<a href='#' class='editBtn' title='编辑' onclick='edit("+row.id+","+index+");'></a>";
			    	  },
			    	  resizable: true
			      }
			 ]], 
		});
	});
	
	function edit(zcbmId,index){
		zcbmInfoDiv=$("<div id=\"zcbmInfoDiv\"></div>");
		zcbmInfoDiv.appendTo("body");
		$("#zcbmInfoDiv").dialog({
			title: "编辑资产部门",
			width: 512,
			height: 300,
			href: getContextPath()+"/console/catalog/zcbmgl/updateZCBMUI.do?id="+zcbmId,
			closed: false,
			cache: false,
			modal: true,
			onClose: function(){
				$("#zcbmList").datagrid("reload");
				$("#zcbmList").datagrid("selectRow",index);
				zcbmInfoDiv.remove();
			} 
		});
	};
	
		function downloadZCBMExcel(){
 			$("#downLoadForm").form('submit', {
			url : getContextPath() + "/console/catalog/zcbmgl/exportZCBMExcel.do",
			success: function(data){
				if(data != '"success"'){
					$.messager.alert('失败',data,'warning');
				}
			}
		});
	};
	</script>
  </body>
</html>
