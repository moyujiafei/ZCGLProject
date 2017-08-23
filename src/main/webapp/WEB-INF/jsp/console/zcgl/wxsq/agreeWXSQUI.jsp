<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建故障维修任务</title>
</head>
<body>
<div id="agreeWXSQDialog">
	<table align="center" cellspacing="10px" style="padding-top: 10px;">
			<tr>
				<td colspan="3" style="font-family: '宋体';font-size:16px;color:gray;">&emsp;&emsp;同意对上述资产进行维修。现在是否要创建故障维修任务？</td>
			</tr>
			<tr>
				
				<td align="right" width="30%">开始时间：</td>
			  	<td width="175"><input id="startTime" name="kssj"></td>
			</tr>
			<tr>
				
				<td align="right">结束时间：</td>
			  <td width="175"><input id="endTime" name="jssj"></td>
			</tr>
			<tr>
				
			  <td align="right">操  作  人：</td>
			  <td width="175"><input id="operator" name="czrmc" userid="czr"></td>
			  <td width="50" style="padding-left:1px"><a href="#" id="search_operator"></a><span style="color:#FF0000;margin-left: 3px;">*</span></td>
			</tr>
			<tr>
				
				<td align="right">验  收  人：</td>
			  <td width="175"><input id="receiver" name="ysrmc" userid="ysr"></td>
		      <td style="padding-left:1px"><a href="#" id="search_receiver"></a><span style="color:#FF0000;margin-left: 3px;">*</span></td>
			</tr>
			<tr>
				
			  <td align="right">验收时间：</td>
			  <td width="175"><input id="checkTime" name="yssj"></td>
			  <td width="10%"></td>
			</tr>
			<tr>
				<td width="165"></td>
				<td colspan="3" align="left" style="padding-left:70px"><a href="#" id="agreeWXSQSave">确定</a>&emsp;&emsp;&emsp;<a href="#" id="agreeWXSQCancel">取消</a></td>
			</tr>
	  </table><form id="agreeWXSQForm" method="post">
		
	</form>
</div>
<script type="text/javascript">
	$("#startTime").datetimebox({
		required : true,
		showSeconds : false,
		onLoadSuccess : function(){
			
		},
	});
	$("#startTime").datetimebox("setValue",new Date().getFullYear()+ "-");
	$("#endTime").datetimebox({
		required : true,
		showSeconds : false,
	});
	$("#endTime").datetimebox("setValue",new Date().getFullYear()+ "-");
	
	$("#operator").searchbox({
		editable : false,
		prompt: "必填项",
		required : true,
		searcher: function(value,name){
			searchOpt.queryOperator("queryCZRUIDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找操作人", {isEdit:true},"operator");
		}		
	});
	$("#receiver").searchbox({
		prompt: "必填项",
		editable : false,
		required : true,
		searcher: function(value,name){
			searchOpt.queryReceiver("queryYSRUIDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找验收人", {isEdit: true},"receiver");
		}
	});
	
	$("#checkTime").datetimebox({
		required : true,
		showSeconds : false,
	});
	$("#checkTime").datetimebox("setValue",new Date().getFullYear()+ "-");
	
	
	$("#agreeWXSQSave").linkbutton({
		iconCls : 'icon-save',
		height : 21,
		onClick : function(){
			$.messager.progress();	
			$("#agreeWXSQForm").form('submit',{
				url:getContextPath() + "/console/zcgl/wxsq/agreeWXSQ.do",
				onSubmit: function(param){
					var selectedData = $("#sqwxListDatagrid").datagrid("getChecked");
					var zcidStr="";
    		    	var isValid = $(this).form('validate');
    		    	for(var i=0;i<selectedData.length;i++){
    			    	zcidStr += selectedData[i].zcid+",";
    			    }
    			    zcidStr = zcidStr.substring(0,zcidStr.length-1);
    			    param.zcidString = zcidStr;
    			    if (!isValid){
						$.messager.progress('close');	
					}
					param.czr=$("#operator").attr("userid");
					param.ysr=$("#receiver").attr("userid");
					param.kssj = $("#startTime").datetimebox("getValue");
					param.jssj = $("#endTime").datetimebox("getValue");
					param.yssj = $("#checkTime").datetimebox("getValue");
					return isValid;
    		    },
    		    success:function(data){
    		    	$.messager.progress('close');
    		    	if(data == "success"){
    					$.messager.alert('成功','创建维修任务成功','info');
    					$("#agreeWXSQDialog").dialog('close');
    					$("#sqwxListDatagrid").datagrid("uncheckAll");
    					$("#sqwxListDatagrid").datagrid("reload");
    				} else{
    					$.messager.alert('失败',data,'waring');
    				}
    		    },
			});
		}
	});
	$("#agreeWXSQCancel").linkbutton({
		iconCls : 'icon-cancel',
		height : 21,
		onClick : function(){
			$("#agreeWXSQDialog").dialog('close');
		},
	});
	var searchOpt = {
	      	  newDialog: function(dialogId,url,title,param){
	      	    var dialogObj = $('<div id="' + dialogId + '"></div>');
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                  href: getContextPath() + url,
	                  title: title,
	                  queryParams: param,
	                  width: 512,
	                  height: 300,
	                  collapsible: true,
	                  modal: true,
	                  draggable: true,
	                  inline: true,
	                  onClose: function() {
	                      dialogObj.remove();// 关闭时remove对话框
	                  }
	              });
	      	},
	      	queryOperator: function(dialogId,url,title,param,searchboxId){
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
	      	queryReceiver: function(dialogId,url,title,param,searchboxId){
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
</script>
</body>
</html>