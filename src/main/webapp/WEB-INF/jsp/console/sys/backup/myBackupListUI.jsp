<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>我的备份列表</title>
  </head>
  
  <body>
	<div id="backupListMain">
	    <div id="divBACKUPToolbar">
  	 	 <form id="newForm" method="post">
			<input type="hidden" name="randomInfo" value="<%=Math.random()%>"/>
		 </form>
	 	 <table> 
	 	 	<tr>
	 	 	<td style="padding-left:20px"><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="objBackup.newBackup()">新建</a></td>
	 	 	</tr>
	 	 </table>
  	    </div>
		<table id="backupList"></table>	<!-- 我的备份列表 -->
	</div>
	<div style="display: none;">
		<form style="display: none;" id="downLoadForm" method="post">
		</form>
	</div>
	<script type="text/javascript">

	 var objBackup = {
			    newBackup : function(){
			    	$.ajax({  
			    		url: getContextPath()+"/console/sys/backup/insertBackup.do",
			  	       	type:"post",
			  	       	cache:false,
			  	       	async:false,
			  	       	dataType:"text", 		  	       	
			  	       	success: function(result) {		
			  	       		if(result=='success'){
			  	       		   $("#backupList").datagrid("reload");
			  	       		}else{
			  	       		  $.messager.alert('失败',result,'warning');	
			  	       		}
			  	       	},
			  	       	error: function() {		  	 
			  	          	$.messager.alert('失败','新建失败','warning');
			  	       	  } 
			  	       });
			    }
	    }; 

		$(function(){
		$("#backupListMain").dialog({
			title: "我的备份列表",
			width: 1024,
			height: 600,
			closed: false,
			shadow: true,	//是否显示阴影
			modal: true,	//是否窗口化
			cache: false,	//是否缓存
			inline: true,	//设置是否在父容器中显示，不在会出现很多问题
			resizable: false	//是否可改变大小
		});
	
		function checkTime(i){
			if(i<10){
				return "0"+i;
			}
			return i;
		}
		
		$("#backupList").datagrid({
			url: getContextPath()+"/console/sys/backup/myBackupList.do",
			
			onLoadSuccess: function(data){
				$(".deleteBtn").linkbutton({plain:true,height:23, iconCls:'icon-feiwu'});
			},
			
			method: "post",
			fit: true,
            fitColumns: true,
			pagination: true,	//是否显示分页工具栏
			pagePosition: "bottom",	//设置分页工具的位置
			pageSize: 15,		//一页记录数
			pageList: [15],	//页面大小选择列表
			striped:true,			//斑马线样式,
			nowrap : true,		//是否在一行显示所有，自动换行
			loadMsg:"努力加载中，请稍后。。。。",//加载信息
			rownumbers: true,		//显示行号
			singleSelect: true,		//只允许选择一行
			showHeader : true,//显示行头，默认true;
			emptyMsg: "没有数据",
			toolbar: "#divBACKUPToolbar",
			columns: [[
			     {
			    	  field: "bfsj",
			    	  title: "备份时间",
			    	  width: "26%",
			    	  align: "center",
			    	  resizable: false,
			    	  formatter:function(value){ 		    		  
			    		  var date=new Date(value);
			    		  var year=date.getFullYear();
			    		  var month = date.getMonth() + 1;
			    		  var day=date.getDate();
			    		  var hour=date.getHours();
			    		  var min=date.getMinutes();
			    		  var second=date.getSeconds();
			    		  month=checkTime(month);
			    		  day=checkTime(day);
			    		  hour=checkTime(hour);
			    		  min=checkTime(min);
			    		  second=checkTime(second);
			    		  var finalDate = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + second;
		    		      return finalDate;
			    	  }
			      },
			      {
			    	  field: "czrmc",
			    	  title: "操作人",
			    	  width: "18%",
			    	  align: "center",
			    	  resizable: false
			      },
			      {
			    	  field: "backuptype",
			    	  title: "备份类型",
			    	  width: "18%",
			    	  align: "center",
			    	  resizable: false
			      },
			      {
			      	  field: "backupset",
			    	  title: "备份文件",
			    	  width: "25%",
			    	  align: "center",
			    	  resizable: false,
			    	  formatter: function(value,row,index){	
			    		  return "<a  href='javascript:void(0);' onclick='downLoad("+row.id+")'>"+value+"</a>";
			    	  }
			      },
			      {
			    	  field: "opt1",
			    	  title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
			    	  width: "15%",
			    	  align: "left",
			    	  formatter: function(value,row,index){
			    		  if(value=="找不到数据"){
			    			  return "";
			    		  }
			    		  return "&nbsp;&nbsp;<a href='#' class='deleteBtn' title='删除' onclick='deleteById("+row.id+","+index+");'></a>";
			    	  },
			    	  resizable: true
			      }
			 ]], 
		});
			
	});	
		
		function deleteById(id,index){
			$.messager.confirm('提示','您确定要删除该备份吗？',function(result){
				if(result){
					$.ajax({  
			    		url: getContextPath()+"/console/sys/backup/deleteBackup.do?id="+id,
			  	       	type:"post",
			  	       	cache:false,
			  	       	async:false,
			  	       	dataType:"json",    	
			  	       	success:function (param) {				  	       		
			  	       		if(param){
			  	       		   $("#backupList").datagrid("reload");
			  	       		}
			  	       	},
			  	       	error: function () {
			  	       		alert("Aajx error!");
			  	        	}
			  	       });
				}
			 }	
			);
		};
		
		function downLoad(id){
	        $("#downLoadForm").form("submit", {
                url: getContextPath() + "/console/sys/backup/downLoad.do",
                onSubmit: function(param) {
                	param.id = id;
                },
                success: function(data) {               	
                }     
            });
		};
		
	</script>
  </body>
</html>
