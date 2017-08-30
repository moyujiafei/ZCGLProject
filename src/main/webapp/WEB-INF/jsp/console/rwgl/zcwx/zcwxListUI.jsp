<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" >
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
	<div id="zcwxListMain">  
	<table align="right" id="zcwxListToolBar" width="100%">
			<tr>
			<td width="250"></td>
				<td style="padding-left:20px;padding-right:10px">资产名称 : </td>
				<td><input id="selectZcmc"  name="zcmc"  style="width:125px"></td>
				<td style="padding-left:20px;padding-right:10px">资产类型 : </td>
				<td><input id="selectZclx"  name="zclx"  style="width:125px"></td>
				<td style="padding-left:20px;padding-right:10px"">使用人 : </td>
				<td><input id="selectSyr"  name="syr"  style="width:125px"></td>
				<td style="padding-left:10px;padding-right:10px"><a href="#"  id="searchWXBtn">筛选</a></td>
			</tr>
	 </table>
	 <table id ="tbZcwxDateGrid"></table>
	 
	</div>
<script type="text/javascript">
var zcWxManageOpt = {
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
		newMinDialog: function(dialogId,url,title,param){
			// 弹出新的对话框
			var dialogObj = $('<div id="' + dialogId + '"></div>');
			dialogObj.appendTo("body");
			$("#" + dialogId).dialog({
                href: getContextPath() + url,
                title: title,
                top: 220,
                width: 512,
                height: 300,
                queryParams: param,
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
		finishWX: function(index){
			var rowInfo = $("#tbZcwxDateGrid").datagrid("getRows")[index];
			var param = {"zcid": rowInfo.zcid};
			zcWxManageOpt.newMinDialog("finishDialog","/console/rwgl/zcwx/finishGZWXUI.do","完成维修",param);
		},
		resubmitWXSQ:function(index){
			var rowInfo = $("#tbZcwxDateGrid").datagrid("getRows")[index];
			var param = {"zcid": rowInfo.zcid};
			zcWxManageOpt.newMinDialog("resubmitWXSQ","/console/rwgl/zcwx/resubmitWXSQUI.do","申请重新维修",param);
		},
		submitBFSQ:function(index){
			var rowInfo = $("#tbZcwxDateGrid").datagrid("getRows")[index];
			var param = {"zcid": rowInfo.zcid};
			zcWxManageOpt.newMinDialog("submitBFSQ","/console/rwgl/zcwx/submitBFSQUI.do","申请报废",param);
		},
		submitXZSQ:function(index){
			var rowInfo = $("#tbZcwxDateGrid").datagrid("getRows")[index];
			var param = {"zcid": rowInfo.zcid};
			zcWxManageOpt.newMinDialog("submitXZSQ","/console/rwgl/zcwx/submitXZSQUI.do","申请闲置",param);
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
    	},
    	query_zclx: function () {
				dialogObj = $("<div id='dialogObj'></div>");
				czclx  = null;
				dialogObj.appendTo("body");
				$("#dialogObj").dialog({
					href: getContextPath() + "/console/catalog/zclxgl/queryZCLXUI.do",
					title: "资产类型查询",
					width: 512,
					height: 300,
					modal: true,
					onClose: function () {
						if(czclx != null) {
							dialogObj.remove();
							$("#selectZclx").searchbox("setValue",czclx.mc);
							$("#selectZclx").attr("lxid",czclx.lxId); 
						}
						dialogObj.remove();
					}
				});
			}

};

$(function(){
	$("#zcwxListMain").dialog({
  		title : '待维修资产列表',
  		height:600,
  		width:1024,
  		closed : false,
		cache : false,
		shadow : true, //显示阴影
		resizable : false, //不允许改变大小
		modal : true, //是否窗口化
		inline : true, //是否在父容器中，若不在会出现很多BUG
  	});	
  	
  	$("#selectZclx").searchbox({    
		editable: false,
		searcher: function(value,name){
			zcWxManageOpt.query_zclx();
		}
    });
  	
  	$("#selectZcmc").textbox({    
  		width: 120,
     	height: 25
    });
  	
  	$("#selectSyr").searchbox({    
  		width: 120,
     	height: 25,
		editable: false,
		searcher: function(value,name){
			zcWxManageOpt.queryWXUser("syrDialog","/console/wxgl/wxuser/queryWXUserUI.do","使用人列表",{},"selectSyr");
		}
    });
  	
  	$("#tbZcwxDateGrid").datagrid({
  		width : "100%",
		height : "100%",
		method : "post",
		toolbar:"#zcwxListToolBar",
		url : getContextPath() + "/console/rwgl/zcwx/zcwxList.do",
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
			//完成维修
			$(".searchOneBtn").linkbutton({
				plain : true,
				height : 21,
				iconCls : 'icon-finishwx'
			});
			//重新维修
			$(".searchTwoBtn").linkbutton({
				plain : true,
				height : 21,
				iconCls : 'icon-rewx'
			});
			//申请闲置
			$(".searchThreeBtn").linkbutton({
				plain : true,
				height : 21,
				iconCls : 'icon-rubbish'
			});
			//申请报废
			$(".searchFourBtn").linkbutton({
				plain : true,
				height : 21,
				iconCls : 'icon-free'
			});
			$(".tip").tooltip({ 
      		  	trackMouse: true,
                onShow: function(){ 
                    $(this).tooltip('tip').css({   
                        width:300, 
                        height:300,
                        boxShadow: '1px 1px 3px #292929',
                    });  
                }  
             });
		},	
		
	columns : [[
			{
				width : "14.5%",
				field : "zcdm",
				title : "资产编号",
				halign : "center",
				align : "center",
				resizable : true,
				formatter:function(value,row){ 
                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                   return content;  
             	}
			},
			{
				width : "14.5%",
				field : "zc",
				title : "资产名称",
				halign : "center",
				align : "center",
				resizable : true,
				formatter:function(value,row){ 
                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                   return content;  
             	}
			},
			{
				width : "14.5%",
				field : "zclx",
				title : "资产类型",
				halign : "center",
				align : "center",
				resizable : true,
				formatter:function(value,row){ 
                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                   return content;  
             	}
			},
			{
				width : "14.5%",
				field : "zjnx",
				title : "折旧年限",
				halign : "center",
				align : "center",
				resizable : true,
				formatter:function(value,row){ 
                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                   return content;  
             	}
			},
			{
				width : "14.5%",
				field : "num",
				title : "资产数量",
				halign : "center",
				align : "center",
				resizable : true,
				formatter:function(value,row){ 
                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                   return content;  
             	}
			},
			{
				width : "14.5%",
				field : "syrmc",
				title : "使用人",
				halign : "center",
				align : "center",
				resizable : true,
				formatter:function(value,row){ 
                   var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                   return content;  
             	}
			},
		
			{
				width : "15%",
				field : "opt",
				title : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
				align : "left",
				//实现详情操作，还未完成  resubmitWXSQ
                formatter: function(value, row, index) {
                	//dialogId,url,title
                	return '&nbsp;&nbsp;<a href="#" class="searchOneBtn" title="完成维修" onclick="zcWxManageOpt.finishWX(' 
                  	+ index + ')"></a>'+
                  	'&nbsp;&nbsp;<a href="#" class="searchTwoBtn" title="申请重新维修" onclick="zcWxManageOpt.resubmitWXSQ(' 
                  	+ index + ')"></a>'+
                  	'&nbsp;&nbsp;<a href="#" class="searchThreeBtn" title="申请报废" onclick="zcWxManageOpt.submitBFSQ(' 
                  	+ index + ')"></a>'+
                  	'&nbsp;&nbsp;<a href="#" class="searchFourBtn" title="申请闲置" onclick="zcWxManageOpt.submitXZSQ(' 
                  	+ index + ')"></a>';
                },
				resizable : false,
			} ] ]
		
	});
});
  	
  	
	$("#searchWXBtn").linkbutton({
		iconCls: 'icon-search',
		plain : true,
		onClick: function (){
			doSearch();
		}
	});
	
	function doSearch(){
		var zclx = $("#selectZclx").attr("lxid");
		var zcmc = $.trim($("#selectZcmc").textbox("getValue"));
		var	syr = $.trim($("#selectSyr").attr("userid"));
		var param ={"zclx":zclx,"zcmc":zcmc,"syr":syr};
		$("#tbZcwxDateGrid").datagrid("reload",param);
	};
	
</script>
</body>
</html>