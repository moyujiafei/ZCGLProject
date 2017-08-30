<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
<div id="dlgupdateRegistZCUI">
	<form id="updateRegistZCUIForm" method="post" enctype="multipart/form-data">
		<table align="center" cellspacing="5px" style="margin-top:1px">
			<tr>
	 			<td style="text-align:right;">资产代码：</td>
	 			<td>
		  			<table>
			  			<tr>
						  <td><input id="txbupdateRegistZC_dm" name="dm" style="width:110px;"/>&nbsp;&nbsp;&nbsp;</td>
					  	  <td style="text-align:right;">资产名称：</td>
						  <td><input id="txbupdateRegistZC_mc" name="mc"  style="width:110px;"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
				  		</tr>
		  			</table>
	 			</td>
			</tr>
			<tr>
				<td style="text-align:right;">资产类型：</td>
				<td><input id="srbupdateRegistZC_lxid" name="lxmc" style="width:300px;" /><span style="color:#FF0000;margin-left: 3px;">*</span></td>
			</tr>
		    <tr>
	 			<td style="text-align:right;">单价：</td>
	 			<td>
		  			<table>
			  			<tr>
				  			<td><input id="nbbupdateRegistZC_cost" name="cost" style="width:90px;" /><span style="margin-left: 3px;">&nbsp;元</span></td>
				  			<td style="text-align:right;">&nbsp;&nbsp;资产数量：</td>
				  			<td><input id="txbupdateRegistZC_num" name="num" style="width:110px;"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
				  		</tr>
		  			</table>
	 			</td>
			</tr>
			<tr>
	 			<td style="text-align:right;">规格型号：</td>
	 			<td>
		  			<table>
			  			<tr>
				  			<td><input id="txbupdateRegistZC_xh" name="xh" /></td>
				  			<td style="padding-left:10px">出厂编号：</td>
				  			<td><input id="txbupdateRegistZC_ccbh" name="ccbh" /></td>
				  		</tr>
		  			</table>
	 			</td>
			</tr>
			<tr>
				<td style="text-align:right;">购置时间：</td>
				<td>
	 				<table>
		  				<tr>
				  			<td><input id="dtbupdateRegistZC_gzsj" name="gzsj" /></td>
				  			<td style="padding-left:10px">折旧年限：</td>
				  			<td><input id="txbupdateRegistZC_zjnx" name="zjnx"/>&nbsp;&nbsp;年<span style="color:#FF0000;margin-left: 3px;">*</span></td>
			  			</tr>
		  			</table>
	 			</td>
			</tr>
			<tr>
	 			<td style="text-align:right;">资产照片：</td>
	 			<td><input id="fbupdateRegistZC_zczp" name="file_upload" style="width:300px;"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
			</tr>
		</table>
		<table>
            <tr>
                <td height="30px"></td>
                <td width="140px"></td>
                <td width="100px" style="text-align: center"><a data-options="iconCls:'icon-ok'" class="easyui-linkbutton" onclick="updateRegistZCUI.submit_updateZC()">确认</a></td>
                <td width="100px" style="text-align: center"><a data-options="iconCls:'icon-cancel'" onclick="updateRegistZCUI.cancel()" class="easyui-linkbutton">取消</a></td>
            </tr>
         </table>
	</form>
</div>
<script>
	function FormatDate(date){
		if (date) {
			return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		}
		return "";
	}
	
	var updateRegistZCUI = {
		
		submit_updateZC: function () {
			$.messager.progress();
			$("#updateRegistZCUIForm").form("submit",{
				url: getContextPath() + "/console/zcgl/regist/updateRegistZC.do",
				onSubmit: function (param) {
					var valid = $("#updateRegistZCUIForm").form("validate");
					if (!valid) {
						$.messager.progress('close');
						return valid;	
					}
					
					param.id=editRow.zcid;
					param.lxid=$("#srbupdateRegistZC_lxid").attr("lxid");
					
				},
				success: function (result) {
					$.messager.progress('close');
					if (result == "success") {
						$("#registZCList_newEditdlg").dialog("close");
						$("#dgregistZCList").datagrid("reload");	
					} else if (result =="ZCMC_ERROR") {
						$.messager.alert("提示","资产名称不能为空或者是空格！","info");
					} else if (result =="ZJNX_ERROR") {
						$.messager.alert("提示","折旧年限必须是数字且不能小于0！","info");
					} else if (result =="NUM_ERROR") {
						$.messager.alert("提示","资产数量必须是正整数！","info");
					} else if (result =="COST_ERROR") {
						$.messager.alert("提示","资产单价必须是正数！","info");
					} else {
						$.messager.alert("提示",result,"info");
					}
				}
			});
			
		},
		
		cancel: function () {
			$("#registZCList_newEditdlg").dialog("close");
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
						$("#srbupdateRegistZC_lxid").searchbox("setValue",czclx.mc);
						$("#srbupdateRegistZC_lxid").attr("lxid",czclx.id); 
					}
					dialogObj.remove();
				}
			});
		}
	};
	
	$("#txbupdateRegistZC_dm").textbox({
		disabled: true,
		iconCls:'icon-lock', 
	    iconAlign:'right' ,
		value: "${zcInfo.zcdm}"
	});
	
	$("#txbupdateRegistZC_mc").textbox({
		prompt: "必填项",
		required: true,
		value: "${zcInfo.zc}",
		onChange: function (newValue, oldValue) {
			$.ajax({
				url: getContextPath() + "/console/zcgl/regist/checkRegistZCByMC.do",
				cache: false,
				data: {"zcmc":newValue},
				dataType: "json",
				success: function (result) {
					if (result) {
						$.messager.alert("提示","资产名称不能为空或者是空格！","info");
					}
				},
				error: function () {
					alert("Ajax error!");
				}
			});
		}
	});
	$("#srbupdateRegistZC_lxid").searchbox({
		required:true,
		editable: false,
		value:"${zcInfo.zclx}",
		searcher: function(value,name){
			updateRegistZCUI.query_zclx();
		}
	});
	$("#txbupdateRegistZC_xh").textbox({
		width:110,
		value: "${zcInfo.xh}"
	});
	
	$("#txtupdateRegistZC_zczp").textbox({
		
		width:110,
	});
	
	$("#txbupdateRegistZC_num").numberbox({
		required: true,
		prompt: "只能填入数字",
		width:110,
		value:"${zcInfo.num}",
		onChange: function (newValue, oldValue) {
			$.ajax({
				url: getContextPath() + "/console/zcgl/regist/checkNum.do",
				cache: false,
				data: {"num":newValue},
				dataType: "json",
				success: function (result) {
					if (result) {
						$.messager.alert("提示","资产数量必须是正整数!","info");
					}
				},
				error: function () {
					alert("Ajax error!");
				}
			});
		}
	});
	
	$("#txbupdateRegistZC_ccbh").textbox({
		width:110,
		value: "${zcInfo.ccbh}"
	});
	
	var registGzsjStr = "${zcInfo.gzsj}";
	var registGzsj = '';
	if(registGzsjStr) {
		registGzsj = new Date(registGzsjStr);
	}
	$("#dtbupdateRegistZC_gzsj").datebox({
		width:110,
		required: true,
		value: FormatDate(registGzsj),
		
	});
	
	$("#txbupdateRegistZC_zjnx").numberbox({
		prompt: "只能填入数字",
		width:110,
		precision: 1,
		required: true,
		value: "${zcInfo.zjnx}",
		onChange: function (newValue, oldValue) {
			$.ajax({
				url: getContextPath() + "/console/zcgl/regist/checkRegistZCByZJNX.do",
				cache: false,
				data: {"zjnx":newValue},
				dataType: "json",
				success: function (result) {
					if (result) {
						$.messager.alert("提示","折旧年限必须是数字且不能小于0！","info");
					}
				},
				error: function () {
					alert("请按要求填写");
				}
			});
		}
	});
	
	$("#nbbupdateRegistZC_cost").numberbox({
		required: true,
		width: 90,
		precision: 2,
		value: "${zcInfo.cost}",
		onChange: function (newValue, oldValue) {
			$.ajax({
				url: getContextPath() + "/console/zcgl/regist/checkCost.do",
				cache: false,
				data: {"cost":newValue},
				dataType: "json",
				success: function (result) {
					if (result) {
						$.messager.alert("提示","单价必须是正数!","info");
					}
				},
				error: function () {
					alert("Ajax error!");
				}
			});
		}
	});
	
	$("#fbupdateRegistZC_zczp").filebox({
		required: true,
		prompt: "必选项",
	    buttonText: "选择照片", 
	    buttonAlign: "right",
	    accept: "image/jpeg"
	});
	
</script>
</body>
</html>
