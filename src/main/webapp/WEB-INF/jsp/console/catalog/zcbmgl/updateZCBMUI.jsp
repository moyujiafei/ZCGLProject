<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
  <head>
    <title>编辑资产部门参数页面</title>
  </head>
  
  <body>
	<div id="zcbmMain">
	<form id="zcbmInfoForm" method="post">
		<table class="mytable" style="margin-top:30px">
			<tr><td align="right">部&nbsp;&nbsp;&nbsp;门&nbsp;&nbsp;&nbsp;名：</td><td><input id="deptName" name="deptName" /></tr>
			<tr><td align="right">&nbsp;易耗品负责人：</td><td><input type="text"  id="fzr" name="fzrmc" userid="${zcgl.fzr}" /></td></tr>
			<tr><td align="right">固定资产管理员：</td><td><input type="text"  id="glr" name="glrmc" userid="${zcgl.glr}" /></td></tr>
		</table>
      <table style="margin-top:40px">
			<tr>
				<td width='140px'></td>
				<td width='100px' align="center"><a id="saveBtn_zcbm" href="javascript:void(0);" >确认</a></td>
				<td width='100px' align="center"><a id="cancelBtn_zcbm" href="javascript:void(0);" >取消</a></td>
				<td></td>
			</tr>
	</table>
	</form>
	</div>
	
	<script type="text/javascript">
	var CZCGL = $("#zcbmList").datagrid("getSelected");
		$("#deptName").textbox({
			width: 300,
			iconCls: 'icon-lock',  
			iconAlign: 'right',
			readonly: true,
			value: "${zcgl.deptName}"
		});
		
		$("#fzr").searchbox({
			searcher: function(value,name){
				testOpt.queryWXUser("queryFZRUIDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找部门负责人", {isEdit:true},"fzr");   
			},
			required: true,
			editable: false,
			width: 300,
			value: "${zcgl.fzrmc}"
		});
		
		
		$("#glr").searchbox({
			searcher: function(value,name){
				testOpt.queryWXUser("queryGLRUIDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找资产管理员", {isEdit:true},"glr");
			},
			required: true,
			editable: false,
			width: 300,
			value: "${zcgl.glrmc}"
		});
		
    	
		$("#saveBtn_zcbm").linkbutton({
			iconCls: "icon-ok"
		});
		
		$("#cancelBtn_zcbm").linkbutton({
			iconCls: "icon-no"
		});
		
		$("#cancelBtn_zcbm").click(function(){
			$("#zcbmInfoDiv").dialog("close");
		});
		
		$("#saveBtn_zcbm").click(function(){
			$.messager.progress();
			
			$("#zcbmInfoForm").form("submit",{
				url: getContextPath()+"/console/catalog/zcbmgl/updateZCBM.do",
				onSubmit: function(param){
					param.id = CZCGL.id;
					var isValid = $(this).form('validate');
					if (!isValid){
						$.messager.progress('close');
						$.messager.alert('提示','请按照要求填写表单','info');  
					}
					param.fzr=$("#fzr").attr("userid");
					param.glr=$("#glr").attr("userid");
					return isValid;
				},
				
				success: function(result){
					$.messager.progress("close");
					if(result=="success"){
						$("#zcbmInfoDiv").dialog("close");
					$.messager.alert("提示","修改成功！","info");
					}else{
						$.messager.alert("提示",result,"info");
					}
				},
				error: function(){
					$.messager.progress("close");
					$.messager.alert("提示","修改失败！","info");
				}
			});
		});
		
		         var testOpt = {
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
