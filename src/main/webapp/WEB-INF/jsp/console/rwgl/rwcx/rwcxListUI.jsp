<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务列表</title>
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
	<div id="rwListMain">  
	<table align="right" id="rwcxListToolbar" width="100%">
			<tr>
			<td width="250"></td>
				<td style="padding-left:20px;padding-right:10px">任务类型 : </td>
				<td><input id="selectlx"  name="lx"  style="width:125px"></td>
				<td style="padding-left:20px;padding-right:10px"">操作人 : </td>
				<td><input id="selectCzr"  name="czr" userid="" style="width:125px"></td>
				<td style="padding-left:20px;padding-right:10px"">验收人 : </td>
				<td><input id="selectYsr"  name="ysr" userid=""  style="width:125px"/></td>
				<td style="padding-left:10px"><a href="#"  id="searchRWBtn">筛选</a></td>
			</tr>
	 </table>
	 <table id ="tbRwDateGrid"></table>
	 
	</div>
<script type="text/javascript">
var rwXqManageOpt = {
		newMaxDialog: function(dialogId,url,title,param){
			// 弹出新的对话框
			var dialogObj = $('<div id="' + dialogId + '"></div>');
			dialogObj.appendTo("body");
			$("#" + dialogId).dialog({
                href: getContextPath() + url,
                title: title,
                top: 220,
                queryParams: param,
                width: 1024,
                height: 600,
                shadow : true, //显示阴影
        		resizable : false, //不允许改变大小
                modal: true,
                draggable: true,
                inline: true,
                onClose: function() {
                    dialogObj.remove();// 关闭时remove对话框
                }
            });
		},
		/*newMinDialog: function(dialogId,url,title){
			// 弹出新的对话框
			var dialogObj = $('<div id="' + dialogId + '"></div>');
			dialogObj.appendTo("body");
			$("#" + dialogId).dialog({
                href: getContextPath() + url,
                title: title,
                top: 220,
                width: 512,
                height: 300,
                shadow : true, //显示阴影
        		resizable : false, //不允许改变大小
                modal: true,
                draggable: true,
                inline: true,
                onClose: function() {
                    dialogObj.remove();// 关闭时remove对话框
                }
            });
		},*/
		rwxq: function(index){
			var rowInfo = $("#tbRwDateGrid").datagrid("getRows")[index];
			var param = {"id": rowInfo.id};
			rwXqManageOpt.newMaxDialog("rwXqDialog","/console/rwgl/rwcx/rwcxInfoUI.do","任务详情",param);
		},
	
		queryWXUser: function(dialogId,url,title,param,searchboxId){
    		dialogObj = $('<div id="' + dialogId + '"></div>');
    		wxuser=null;
			dialogObj.appendTo("body");
			$("#" + dialogId).dialog({
                href: getContextPath() + url,
                title: title,
                width: 512,
                height: 300,
                queryParams: param,
                cache: false,
                collapsible: true,
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
    	}

};

  	$("#rwListMain").dialog({
  		title : '任务查询',
  		height:600,
  		width:1024,
  		closed : false,
		cache : false,
		shadow : true, //显示阴影
		resizable : false, //不允许改变大小
		modal : true, //是否窗口化
		inline : true, //是否在父容器中，若不在会出现很多BUG
  	});	
  	$("#selectlx").combobox({    
        required:true,  
     	url: getContextPath()+"/console/rwgl/rwcx/getRWLXComboWithAll.do",
        valueField: 'id',
		textField: 'text',
		editable: false,
		panelHeight: 80,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		}
    });
  	$("#selectCzr").searchbox({    
  		width: 120,
     	height: 25,
     	editable:false,
     	searcher:function(value,name){
     		rwXqManageOpt.queryWXUser("rwCzrDialog","/console/wxgl/wxuser/queryWXUserUI.do","操作人列表",{},"selectCzr");
     	}
    });
  	$("#selectYsr").searchbox({    
  		width: 120,
     	height: 25,
     	editable: false,
     	searcher: function(value,name){
     		rwXqManageOpt.queryWXUser("rwYsrDialog","/console/wxgl/wxuser/queryWXUserUI.do","验收人列表",{},"selectYsr");
     	}
    });
  	
  	$("#tbRwDateGrid").datagrid({
  		width : "100%",
		height : "100%",
		method : "post",
		toolbar:"#rwcxListToolbar",
		url : getContextPath() + "/console/rwgl/rwcx/rwcxList.do",
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
		
		onLoadSuccess : function(data) {
			//查询详情使用
			$(".searchBtn").linkbutton({
				plain : true,
				height : 21,
				iconCls : 'icon-detail',
			});
			
			$(".searchBtn").tooltip({
	 			content : '详情',
	 			position : 'top',
	 			trackMouse: true,
 			});
		},	
		
		
	columns : [[
			{
				width : "12.9%",
				field : "lx",
				title : "任务类型",
				halign : "center",
				align : "center",
				resizable : true,
				formatter : function(value){
                    if(value==0)
                    	return "日常巡检";
                    else if(value==1)
                    	return "故障维修";
                    else if(value==null||value.length<1)
                    	return "";
                   
	              }
			},
			{
				width : "13%",
				field : "kssj",
				title : "开始时间",
				halign : "center",
				align : "center",
				resizable : true,
				formatter : function(value){
                    var date = new Date(value);
                    var y = date.getFullYear();
                    var m = date.getMonth() + 1;
                    var d = date.getDate();
                    return y + '-' +m + '-' + d;
	              }
			},
			{
				width : "13%",
				field : "jssj",
				title : "结束时间",
				halign : "center",
				align : "center",
				resizable : true,
				 formatter : function(value){
                     var date = new Date(value);
                     var y = date.getFullYear();
                     var m = date.getMonth() + 1;
                     var d = date.getDate();
                     return y + '-' +m + '-' + d;
                 }
			},
			{
				width : "13%",
				field : "czrmc",
				title : "操作人",
				halign : "center",
				align : "center",
				resizable : true,
			},
			{
				width : "13%",
				field : "ysrmc",
				title : "验收人",
				halign : "center",
				align : "center",
				resizable : true,
			},
			{
				width : "13%",
				field : "yssj",
				title : " 验收时间",
				halign : "center",
				align : "center",
				resizable : true,
				 formatter : function(value){
                     if(value==null){
                    	 return '';
                     }else{
                    	 var date = new Date(value);
                         var y = date.getFullYear();
                         var m = date.getMonth() + 1;
                         var d = date.getDate();
                         return y + '-' +m + '-' + d;
                     }
                 }
			},
			{
				width : "9.5%",
				field : "jd",
				title : "进度",
				halign : "center",
				align : "center",
				resizable : true,
				sortable: false,
				formatter: function(value, row,index) {
          			return row.finishCount + " / " + row.total;
          		}
			},
			{
				width : '15%',
				field : "opt",
				title : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
				align : "left",
				//实现详情操作，还未完成
                formatter: function(value, row, index) {
                	
                	return '&nbsp;&nbsp;<a href="#" class="searchBtn" onclick="rwXqManageOpt.rwxq(' + index + ')"></a>';
                },
				resizable : false,
			} ] ],
	});
  	
	$("#searchRWBtn").linkbutton({
		iconCls: 'icon-search',
		plain : true,
		onClick: function (){
			doSearch();
		}
	});

	function doSearch(){
		var rwlx = $("#selectlx").combobox("getValue");
		var czr = $.trim($("#selectCzr").attr("userid"));
		var ysr = null;
		if($.trim($("#selectYsr").attr("userid")).length>0)
			ysr = $.trim($("#selectYsr").attr("userid"));
		var param ={"rwlx":rwlx,"czr":czr,"ysr":ysr};
		$("#tbRwDateGrid").datagrid("reload",param);
	};
	
</script>
</body>
</html>