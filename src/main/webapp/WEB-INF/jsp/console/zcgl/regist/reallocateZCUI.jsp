<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
	<form id="reallocateZCForm" method="post">
		<table class="mytable" style="margin-top:1px">
		  <tr><td style="text-align: right;">资产代码：</td><td><input id="reallocateZCUI_txbzcdm" name="dm"/></td></tr>
		  <tr><td style="text-align: right;">资产名称：</td><td><input id="reallocateZCUI_txbzcmc" name="mc"/></td></tr>
		  <tr><td style="text-align: right;">资产类型：</td><td><input id="reallocateZCUI_txbzclx" name="lxid"/></td></tr>
		  <tr><td style="text-align: right;">原有资产管理部门：</td><td><input id="reallocateZCUI_txboldzcgl" name="oldzcgl"/></td></tr>
		  <tr><td style="text-align: right;">新的资产管理部门：</td><td><input id="reallocateZCUI_cmbnewzcgl" name="newzcgl"/></td></tr>
		  <tr><td style="text-align: right;">资产存放地点：</td><td><input id="reallocateZCUI_txbcfdd" name="new_cfddmc"/></td></tr>
		</table>
	</form>
	<table>
	  
	  <tr><td style="width:120px;"></td><td style="width:100px;text-align: center;"><a onclick="reallocateZCUI.submitReallocateZC()" data-options="iconCls:'icon-ok'" class="easyui-linkbutton">确认</a></td><td style="width:150px;text-align: center;"><a data-options="iconCls:'icon-cancel'" onclick="reallocateZCUI.cancel()" class="easyui-linkbutton">取消</a></td></tr>
	</table>
<script>
var cfdd;
reallocateZCUI = {
	
	cancel: function () {
		$("#registZCList_newReallocatedlg").dialog("close");
	},
	
	query_newzcglbm: function () {
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
					$("#reallocateZCUI_cmbnewzcgl").searchbox("setValue",wxdept.deptName); 
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
					 $('#reallocateZCUI_txbcfdd').searchbox("setText",vfj.xqmc + vfj.jzw + vfj.floor+vfj.room);
					 cfdd = vfj.fjId;
				 }
				 queryCFDDUI.remove();
             }
		});
	},
	
	
	submitReallocateZC: function () {
		$.messager.progress();
		$("#reallocateZCForm").form('submit',{
			
			url: getContextPath() + "/console/zcgl/regist/reallocateZC.do",
			onSubmit: function(param){
				
				var valid = $("#reallocateZCForm").form("validate");
				if (!valid) {
					$.messager.progress('close');
					return valid;
				}
				param.zcid=reallocateRow.zcid;
				param.wxdeptNo=wxdept.deptNo;
				param.new_cfdd = cfdd;
		    },    
		    success:function(result){    
		    	$.messager.progress('close');
				if (result == "success") {
					$("#registZCList_newReallocatedlg").dialog("close");
					$("#dgregistZCList").datagrid("reload");	
				} else {
					$.messager.alert("提示",result,"info");
				}   
		    } 
			
		});
		
	}

};

	$("#reallocateZCUI_txbzcdm").textbox({
		disabled: true,
		iconCls:'icon-lock', 
	    iconAlign:'right' ,
		value: reallocateRow.zcdm
	});
	$("#reallocateZCUI_txbzcmc").textbox({
		disabled: true,
		iconCls:'icon-lock', 
	    iconAlign:'right' ,
		value: reallocateRow.zc
	});
	
	$("#reallocateZCUI_txbzclx").textbox({
		disabled: true,
		iconCls:'icon-lock', 
	    iconAlign:'right' ,
		value: reallocateRow.zclx
	});
	
	$("#reallocateZCUI_txboldzcgl").textbox({
		disabled: true,
		iconCls:'icon-lock', 
	    iconAlign:'right' ,
		value: reallocateRow.deptName
	});
	
	$("#reallocateZCUI_cmbnewzcgl").searchbox({
		required: true,
		editable: false,
		searcher: function(value,name){
			reallocateZCUI.query_newzcglbm();
		}
	});
	
	$(function(){
		$.messager.progress();
		$.ajax({
		 	url: getContextPath() + "/console/zcgl/regist/getCFDDTextbox.do",
	        cache: false,
	        async: true,
	        data : {
	       	 "fjid" : reallocateRow.cfdd,
	        },
	        type: "post",
            dataType: "json",// 返回类型必须为json
            success: function(result) {
                $.messager.progress('close');
                if (result.xqmc != null) {
                 	$("#reallocateZCUI_txbcfdd").searchbox({
                   		required: true,
                   		editable : false,
                   		searcher: function(value,name){
                   			reallocateZCUI.query_zccfdd();
                   		},
                   		value : result.xqmc+result.jzw+result.floor+result.room,
                   	});
                   	cfdd=result.fjId;
                }else {
                	$("#reallocateZCUI_txbcfdd").searchbox({
                   		required: true,
                   		editable : false,
                   		searcher: function(value,name){
                   			reallocateZCUI.query_zccfdd();
                   		},
                   	});
                }
            },        
            error: function() {
                $.messager.progress('close');
            }
		});
	});
</script>
</body>
</html>
