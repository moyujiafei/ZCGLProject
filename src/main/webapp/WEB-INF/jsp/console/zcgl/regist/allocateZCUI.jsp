<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
	<form id="allocateZCForm" method="post">
		<table class="mytable">
		  
		  <tr><td style="text-align: right;">资产代码：</td><td><input id="allocateZCUI_txbzcdm" name="dm"/></td><td></td></tr>
		  <tr><td style="text-align: right;">资产名称：</td><td><input id="allocateZCUI_txbzcmc" name="mc"/></td><td></td></tr>
		  <tr><td style="text-align: right;">资产类型：</td><td><input id="allocateZCUI_txbzclx" name="lxid"/></td><td></td></tr>
		  <tr><td style="text-align: right;">资产管理部门：</td><td><input id="allocateZCUI_txbzcgl" name="zcglid" /><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		  <tr><td style="text-align: right;">资产存放地点：</td><td><input id="allocateZCUI_txbcfdd"/><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		</table>
	</form>
	<table>
	  <tr><td style="height: 20px;"></td></tr>
	  <tr><td style="width:120px;"></td><td style="width:100px;text-align: center;"><a onclick="allocateZCUI.submitAllocateZC()" data-options="iconCls:'icon-ok'" class="easyui-linkbutton">确认</a></td><td style="width:150px;text-align: center;"><a data-options="iconCls:'icon-cancel'" onclick="allocateZCUI.cancel()" class="easyui-linkbutton">取消</a></td></tr>
	</table>
<script>
var cfdd = null;
allocateZCUI = {
	query_zcglid: function () {
		dialogObj = $("<div id='dialogObj'></div>");
		wxdept  = null;
		dialogObj.appendTo("body");
		$("#dialogObj").dialog({
			href: getContextPath() + "/console/wxgl/wxdept/queryWXDeptUI.do",
			title: "资产管理部门查询",
			width: 512,
			height: 300,
			queryParams: {isEdit: true},
			onClose: function () {
				if(wxdept != null) {
					dialogObj.remove();
					$("#allocateZCUI_txbzcgl").searchbox("setValue",wxdept.deptName); 
				}
				dialogObj.remove();
			}
		});
	},
	
	query_zccfdd : function () {
		var queryCFDDUI = $("<div id='queryCFDDUI'></div>");
		queryCFDDUI.appendTo("body");
		vfj = null;
		$("#queryCFDDUI").dialog({
			title : "查找存放地点",
			href : getContextPath() + "/console/catalog/fjgl/queryCFDDUI.do",
			height : 300,
			width : 512,
			closed : false,
			 onClose: function() {
				 if (vfj != null){
					 queryCFDDUI.remove();
					 $('#allocateZCUI_txbcfdd').searchbox("setText",vfj.xqmc + vfj.jzw + vfj.floor+vfj.room);
					 cfdd = vfj.fjId;
				 }
				 queryCFDDUI.remove();
             }
		});
	},
	
	cancel: function () {
		$("#registZCList_newAllocatedlg").dialog("close");
	},
	
	submitAllocateZC: function () {
		$.messager.progress();
		$("#allocateZCForm").form('submit',{
			
			url: getContextPath() + "/console/zcgl/regist/allocateZC.do",
			onSubmit: function(param){
				
				var valid = $("#allocateZCForm").form("validate");
				if (!valid) {
					$.messager.progress('close');
					return valid;
				}
				param.zcid=allocateRow.zcid;
				param.wxdeptNo=wxdept.deptNo;
				param.cfdd = cfdd;
		    },    
		    success:function(result){    
		    	$.messager.progress('close');
				if (result.trim() == "success") {
					$("#registZCList_newAllocatedlg").dialog("close");
					$("#dgregistZCList").datagrid("reload");	
				} else {
					$.messager.alert("提示",result,"info");
				}   
		    } 
			
		});
		
	}

};

$("#allocateZCUI_txbzcdm").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: allocateRow.zcdm
});
$("#allocateZCUI_txbzcmc").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: allocateRow.zc
});

$("#allocateZCUI_txbzclx").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: allocateRow.zclx
});

$("#allocateZCUI_txbzcgl").searchbox({
	required: true,
	editable : false,
	searcher: function(value,name){
		allocateZCUI.query_zcglid();
	}
});

$("#allocateZCUI_txbcfdd").searchbox({
	required: true,
	editable : false,
	searcher: function(value,name){
		allocateZCUI.query_zccfdd();
	}
});
</script>
</body>
</html>
