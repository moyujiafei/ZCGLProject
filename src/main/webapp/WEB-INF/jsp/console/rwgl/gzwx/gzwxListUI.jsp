<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>故障维修任务管理页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  
  <body>
  	<div id="gzwxListUIDialog">
  	<table id="gzwxToolBar" width="100%">
  		<tr><td><a id="addGZWX" href="#" onclick="gzwxListOpt.add()">新建</a></td>
  			<td align="right">
  			<table>
  			<td>完成情况：</td><td><input id="gzwx_finishCombo" name="finish"></td>
  			<td width="5%"/>
  			<td>操作人：</td><td><input id="czr" userid=""/></td>
  			<td width="5%"/>
  			<td>验收人：</td><td><input id="ysr" userid=""/></td>
  			<td width="5%"/>
  			<td><a id="queryGZWX" href="#">筛选</a></td>
  			</table>
  			</td>
  		</tr>
  	</table>
	<table id="gzwxList" align="center"></table>
	</div>
	<script type="text/javascript">
		$("#gzwxListUIDialog").dialog({
			title: '故障维修任务列表',    
		    width: 1024,    
		    height: 600,    
		    closed: false,    
		    cache: false,    
		    modal: true,
		    inline: true   
		});
		
		var gzwxListOpt = {
            	newDialog: function(dialogId,url,title,param){
            		var dialogObj = $('<div id="' + dialogId + '"></div>');
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                    href: getContextPath() + url,
	                    title: title,
	                    top: 250,
	                    queryParams: param,
	                    width: 1024,
	                    height: 600,
	                    cache: false,
	                    modal: true,
	                    inline: true,
	                    onClose: function() {
	                        dialogObj.remove();// 关闭时remove对话框
	                    }
	                });
            	},
            	queryWXUser: function(dialogId,url,title,param,searchboxId){
            		dialogObj = $('<div id="' + dialogId + '"></div>');
            		wxuser=null;
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                    href: getContextPath() + url,
	                    title: title,
	                    queryParams: param,
	                    width: 512,
	                    height: 300,
	                    cache: false,
	                    modal: true,
	                    draggable: true,
	                    inline: true,
	                    onClose: function() {
	                        if(wxuser!=null){
	                        	$("#"+searchboxId).searchbox("setValue",wxuser.name);
	                        	$("#"+searchboxId).attr("userid",wxuser.userid);
	                        }
	                        dialogObj.remove();// 关闭时remove对话框
	                    }
	                });
            	},
            	queryGZWX: function(){
            		var czr=$("#czr").attr("userid");
            		var ysr=$("#ysr").attr("userid");
            		var finish=$("#gzwx_finishCombo").combobox('getValue');
            		var param={"czr":czr,"ysr":ysr,"finish":finish};
            		$("#gzwxList").datagrid('options').url=getContextPath()+"/console/rwgl/gzwx/queryGZWX.do";
            		$("#gzwxList").datagrid('options').queryParams=param;
            		$("#gzwxList").datagrid('reload');
            	},
            	add:  function(){
            		gzwxListOpt.newDialog("addGZWXDialog","/console/rwgl/gzwx/insertGZWXUI.do","新建故障维修任务",{});
            	},
            	edit: function(id){
            		gzwxListOpt.newDialog("updateGZWXDialog","/console/rwgl/gzwx/updateGZWXUI.do","编辑故障维修任务",{id: id});
            	},
            	del: function(id){
            		$.messager.confirm("确认对话框","确认删除这个任务吗？",function(r){
            			if(r){
            				$.messager.progress();
            				$.ajax({
            					url: getContextPath()+"/console/rwgl/gzwx/delGZWX.do",
            					data: {id: id},
            					cache: false,
            					async: false,
            					type: "post",
            					dataType: "text",
            					success: function(result){
            						if(result=="success"){
            							$.messager.progress('close');
            							$.messager.alert("提示信息","删除成功",'info');
            							$("#gzwxList").datagrid("reload");
            						}else{
            							$.messager.progress('close');
            							$.messager.alert("提示信息","删除失败",'info');
            							$("#gzwxList").datagrid("reload");
            						}
            					},
            					error: function(){
            						$.messager.progress('close');
            						$.messager.alert("提示信息","删除失败",'info');
            					}
            				});
            			}
            		});
            	}
          };
		
		$("#addGZWX").linkbutton({
			iconCls: 'icon-add',
			plain : true,
		});
		
		$("#gzwx_finishCombo").combobox({
			width:115,
			url:getContextPath() + '/console/rwgl/gzwx/getFinishCombo.do',    
	        valueField:'id',    
	    	textField:'text', 
	    	editable:false,
	    	onLoadSuccess:function(){
	    		var data=$("#gzwx_finishCombo").combobox('getData');
	    		$("#gzwx_finishCombo").combobox('select',data[1].id);
	    	},	
		});
		
		$("#czr").searchbox({
			width: 115,
			searcher: function(value,name){
				gzwxListOpt.queryWXUser("queryCzrDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找操作人", {},"czr");
			},
			editable: false,
			prompt: "请点击右侧图标"
		});
		
		$("#ysr").searchbox({
			width: 115,
			searcher:function(value,name){
				gzwxListOpt.queryWXUser("queryYsrDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找验收人", {},"ysr");
			},
			editable: false,
			prompt: "请点击右侧图标"
		});
		
		$("#gzwxList").datagrid({
			url: getContextPath()+"/console/rwgl/gzwx/gzwxList.do",
			height: "100%",
		/* 	onLoadError: function(){
				$(this).datagrid("appendRow",{
					ysrmc: "载入数据失败"
				});
			}, */
			onLoadSuccess: function(data){
			
					$(".editBtn").linkbutton({
						iconCls: 'icon-edit',
						plain: true,
						height: 21
						
					});
					
					$(".delBtn").linkbutton({
						iconCls: 'icon-no',
						plain: true,
						height: 21
					});
					
					$(".delBtn").tooltip({
	 					content : '删除',
	 					position : 'top',
	 					trackMouse: true,
 					});
 					
 					$(".editBtn").tooltip({
	 					content : '编辑',
	 					position : 'top',
	 					trackMouse: true
 					});
			},
			columns: [[
				{
					title: "开始时间",
					field: "kssj",
					width: '11%',
					align: "center",
					formatter: function(value,row,index){
						if(value==null){
							return '';
						}
						var newtime=new Date(value*1);
						var month=(newtime.getMonth()+1)+"";
						if(month.length<2){
							month="0"+month;
						}
						var day=newtime.getDate()+"";
						if(day.length<2){
							day="0"+day;
						}
						return newtime.getFullYear()+"-"+month+"-"+day;
					}
				},
				{
					title: "结束时间",
					field: "jssj",
					width: '11%',
					align: "center",
					formatter: function(value,row,index){
						if(value==null){
							return '';
						}
						var newtime=new Date(value*1);
						var month=(newtime.getMonth()+1)+"";
						if(month.length<2){
							month="0"+month;
						}
						var day=newtime.getDate()+"";
						if(day.length<2){
							day="0"+day;
						}
						return newtime.getFullYear()+"-"+month+"-"+day;
					}
				},
				{
					title: "操作人",
					field: "czrmc",
					width: '11%',
					align: "center"
				},
				{
					title: "验收人",
					field: "ysrmc",
					width: '11%',
					align: "center"
				},
				{
					title: "验收时间",
					field: "yssj",
					width: '11%',
					align: "center",
					formatter: function(value,row,index){
						if(value==null){
							return '';
						}
						var newtime=new Date(value*1);
						var month=(newtime.getMonth()+1)+"";
						if(month.length<2){
							month="0"+month;
						}
						var day=newtime.getDate()+"";
						if(day.length<2){
							day="0"+day;
						}
						return newtime.getFullYear()+"-"+month+"-"+day;
					}
				},
				{
					title: "进度",
					field: "finishCount",
					width: '10%',
					align: "center",
					formatter: function(value,row,index){
						if(value==null){
							return '';
						}
						return value+"/"+row.total;
					},
				},
				{
					title: "备注",
					field: "czRemark",
					width: '22.2%',
					align: "center",
				},
				{
					title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
					field: "opt",
					width: '15%',
					align: "left",
					formatter: function(value,row,index){
						if(row.ysrmc=="载入数据失败" || row.ysrmc=="数据为空"){
							return '';
						}else if(row.finishCount>0){
							return '&nbsp;&nbsp;<a href="#" class="editBtn" onclick="gzwxListOpt.edit('+row.id+')"></a>&nbsp;&nbsp;';
						}else{
							return '&nbsp;&nbsp;<a href="#" class="editBtn" onclick="gzwxListOpt.edit('+row.id+')"></a>&nbsp;&nbsp;'+
							'<a href="#" class="delBtn" onclick="gzwxListOpt.del('+row.id+')"></a>';
						}
					}
				},
			]],
			toolbar: "#gzwxToolBar",
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
			emptyMsg : "没有数据",
			errorMsg :"加载失败",
		});
		
		$("#queryGZWX").linkbutton({
			iconCls: 'icon-search',
			plain : true,
		});
		
		$("#queryGZWX").click(function(){
			gzwxListOpt.queryGZWX();
		});
	</script>
  </body>
</html>
