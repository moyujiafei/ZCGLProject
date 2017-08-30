<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
</style>
</head>
  
<body>
<form id="revertZCForm" method="post">
		<table class="mytable">
		  
		  <tr><td style="text-align: right;">资产代码：</td><td><input  id="revertZCUI_txbzcdm" name="dm"/></td><td></td></tr>
		  <tr><td style="text-align: right;">资产名称：</td><td><input  id="revertZCUI_txbzcmc" name="mc"/></td><td></td></tr>
		  <tr><td style="text-align: right;">资产类型：</td><td><input id="revertZCUI_txbzclx" name="lxid"/></td><td></td></tr>
		  <tr><td style="text-align: right;">申请原因：</td><td><input  id="revertZCUI_txbremark" name="remark"/></td></tr>
		</table>
	</form>
	<table>
	  <tr><td style="width:120px;"></td><td style="width:100px;text-align: center;"><a onclick="revertZCUI.submitRevertZC()" data-options="iconCls:'icon-ok'" class="easyui-linkbutton">确认</a></td><td style="width:150px;text-align: center;"><a data-options="iconCls:'icon-cancel'" onclick="revertZCUI.cancel()" class="easyui-linkbutton">取消</a></td></tr>
	</table>
<script type="text/javascript">

revertZCUI = {
		
	cancel: function () {
		$("#assignZCList_newRevertDlg").dialog("close");
	},
	
	submitRevertZC: function () {
		$.messager.progress();
		$("#revertZCForm").form("submit",{
			url: getContextPath() + "/console/zcgl/assign/revertZC.do",
			onSubmit: function (param) {
				var valid = $("#revertZCForm").form("validate");
				if (!valid) {
					return valid;
				}
				param.zcid = revertRow.zcid;
			},
			success: function (result) {
				$.messager.progress("close");
				if (result == "success") {
					
				  $("#assignZCList_newRevertDlg").dialog("close");
			      $("#dgassignZCList").datagrid("reload");
			      
				} else {
					$.messager.alert("提示",result,"info");
				}
			}
		});
	}	
};

$("#revertZCUI_txbzcdm").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: revertRow.zcdm
});
$("#revertZCUI_txbzcmc").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: revertRow.zc
});

$("#revertZCUI_txbzclx").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: revertRow.zclx
});

$("#revertZCUI_txbremark").textbox({
	multiline: true,
	height: 70
});

</script>	
</body>
</html>
