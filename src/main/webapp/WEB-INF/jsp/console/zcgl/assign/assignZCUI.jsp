<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
<form id="assignZCForm" method="post">
		<table class="mytable">
		  <tr><td style="text-align: right;">资产代码：</td><td><input id="assignZCUI_txbzcdm" name="dm"/></td><td></td></tr>
		  <tr><td style="text-align: right;">资产名称：</td><td><input id="assignZCUI_txbzcmc" name="mc"/></td><td></td></tr>
		  <tr><td style="text-align: right;">资产类型：</td><td><input id="assignZCUI_txbzclx" name="lxid"/></td><td></td></tr>
		  <tr><td style="text-align: right;">资产管理部门：</td><td><input id="assignZCUI_txbzcgl" name="zcgl"/></td></tr>
		  <tr><td style="text-align: right;">资产使用人：</td><td><input id="assignZCUI_txbsyr" name="syrname" userid="syr"/><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		</table>
	</form>
	<table>
	  <tr><td style="height: 10px;"></td></tr>
	  <tr><td style="width:120px;"></td><td style="width:100px;text-align: center;"><a onclick="assignZCUI.submitAssignZC()" data-options="iconCls:'icon-ok'" class="easyui-linkbutton">确认</a></td><td style="width:150px;text-align: center;"><a data-options="iconCls:'icon-cancel'" onclick="assignZCUI.cancel()" class="easyui-linkbutton">取消</a></td></tr>
	</table>
<script>

assignZCUI = {
		
	cancel: function () {
		$("#assignZCList_newAssignDlg").dialog("close");
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
			modal: true,
			inline: true,
			queryParams: {isEdit:true},
			onClose: function () {
				if(wxuser!=null){
					$("#assignZCUI_txbsyr").searchbox("setValue",wxuser.name);
					$("#assignZCUI_txbsyr").attr("userid",wxuser.userid);
				}
				dialogObj.remove(); 
			}
		});
		
			
	},
	
	submitAssignZC: function () {
		$.messager.progress();
		$("#assignZCForm").form("submit",{
			url:getContextPath() + "/console/zcgl/assign/assignZC.do",
			onSubmit: function (param) {
				var valid = $("#assignZCForm").form("validate");
				if (!valid){
					$.messager.progress("close");
					return false;
				}
				param.zcid = assignRow.zcid;
				param.syr=$("#assignZCUI_txbsyr").attr("userid");
			},
			success: function (result) {
				$.messager.progress('close');
				if (result=="success") {
					
				  $("#assignZCList_newAssignDlg").dialog("close");
			      $("#dgassignZCList").datagrid("reload");
			      
				} else {
					$.messager.alert("提示",result,"info");
				}
				
			},

			
		});
		
	}
	
};

$("#assignZCUI_txbzcdm").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: assignRow.zcdm
});
$("#assignZCUI_txbzcmc").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: assignRow.zc
});

$("#assignZCUI_txbzclx").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: assignRow.zclx	
});

$("#assignZCUI_txbzcgl").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
    value:assignRow.deptName
});

$("#assignZCUI_txbsyr").searchbox({
	required: true,
	editable: false,
	searcher: function(value,name){
		assignZCUI.query_syr();
	}
});


</script>
</body>
</html>
