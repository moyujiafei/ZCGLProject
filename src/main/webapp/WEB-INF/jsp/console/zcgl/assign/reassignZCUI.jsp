<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
	<form id="reassignZCForm" method="post">
		<table class="mytable">
		  
		  <tr><td style="text-align: right;">资产代码：</td><td><input style="width:110px;" id="reassignZCUI_txbzcdm" name="dm"/>&nbsp;&nbsp;资产名称：&nbsp;<input style="width:120px;" id="reassignZCUI_txbzcmc" name="mc"/></td></tr>
		  <tr><td style="text-align: right;">资产类型：</td><td><input style="width:300px;" id="reassignZCUI_txbzclx" name="lxid"/></td></tr>
		  <tr><td style="text-align: right;">原有资产使用人：</td><td><input style="width:300px;" id="reassignZCUI_txboldsyr" name="old_syr"/></td></tr>
		  <tr><td style="text-align: right;">新的资产使用人：</td><td><input style="width:300px;" id="reassignZCUI_txbnewsyr" name="new_syr"/><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		  <tr><td style="text-align: right;">重新分配原因：</td><td><input style="width:300px;" id="reassignZCUI_txbremark" name="remark"/></td></tr>
		</table>
	</form>
	<table>
	  
	  <tr><td style="width:120px;"></td><td style="width:100px;text-align: center;"><a onclick="reassignZCUI.submitReassignZC()" data-options="iconCls:'icon-ok'" class="easyui-linkbutton">确认</a></td><td style="width:150px;text-align: center;"><a data-options="iconCls:'icon-cancel'" onclick="reassignZCUI.cancel()" class="easyui-linkbutton">取消</a></td></tr>
	</table>
<script>

reassignZCUI = {
	cancel: function () {
		$("#assignZCList_newReassignDlg").dialog("close");
	},
		
	query_syr: function (){
		
		dialogObj = $("<div id='dialogObj'></div>");
		wxuser  = null;
		dialogObj.appendTo("body");
		$("#dialogObj").dialog({
			href: getContextPath() + "/console/wxgl/wxuser/queryWXUserUI.do",
			title: "资产使用人查询",
			width: 512,
			height: 300,
			queryParams: {isEdit: true},
			onClose: function () {
				if(wxuser!=null){
					$("#reassignZCUI_txbnewsyr").searchbox("setValue",wxuser.name); 
					$("#reassignZCUI_txbnewsyr").attr("userid",wxuser.userid);
				}
				dialogObj.remove();
			}
		});
		
	},
	
	submitReassignZC: function () {
		$.messager.progress();
		$("#reassignZCForm").form("submit",{
			url: getContextPath() + "/console/zcgl/assign/reassignZC.do",
			onSubmit: function (param) {
				var valid = $("#reassignZCForm").form("validate");
				if (!valid) {
					$.messager.progress("close");
					return valid;
				}
				param.zcid=reassignRow.zcid;
				param.newSyr=$("#reassignZCUI_txbnewsyr").attr("userid");
			},
			success: function (result) {
				$.messager.progress("close");
				if (result=="success") {
					
				  $("#assignZCList_newReassignDlg").dialog("close");
			      $("#dgassignZCList").datagrid("reload");
			      
				} else {
					$.messager.alert("提示",result,"info");
				}
			}
		});	
	}
};

$("#reassignZCUI_txbzcdm").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: reassignRow.zcdm
});
$("#reassignZCUI_txbzcmc").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: reassignRow.zc
});

$("#reassignZCUI_txbzclx").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: reassignRow.zclx
});

$("#reassignZCUI_txboldsyr").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
    value: reassignRow.syrmc
});

$("#reassignZCUI_txbnewsyr").searchbox({
	editable: true,
	required: true,
	searcher: function(value,name){
		reassignZCUI.query_syr();
	}
});

$("#reassignZCUI_txbremark").textbox({
	multiline: true,
	height: 50
});


</script>
</body>
</html>
