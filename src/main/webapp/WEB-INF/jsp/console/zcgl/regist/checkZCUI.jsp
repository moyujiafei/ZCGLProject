<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
<div id="dlginsertCheckZCUI">
	<form id="checkZCForm" method="post" enctype="multipart/form-data">
	<table align="center" cellspacing="5px" style="margin-top:1px">
		<tr>
  			<td style="text-align:right;">资产代码：</td>
  			<td>
	  			<table>
		  			<tr>
					  <td><input id="txbcheckZC_dm" name="dm" style="width:110px;"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
				  	  <td style="text-align:right;">资产名称：</td>
					  <td><input id="txbcheckZC_mc" name="mc"  style="width:110px;"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
			  		</tr>
	  			</table>
  			</td>
 		</tr>
 		<tr>
 			<td style="text-align:right;">资产类型：</td>
 			<td><input id="srbcheckZC_lxid" name="lxmc" style="width:300px;" /><span style="color:#FF0000;margin-left: 3px;">*</span></td>
 		</tr>
	    <tr>
			<td style="text-align:right;">单价：</td>
			<td>
	 			<table>
		  			<tr>
			  			<td><input id="nbbcheckZC_cost" name="cost" style="width:90px;" /><span style="margin-left: 3px;">&nbsp;元</span></td>
			  			<td style="text-align:right;">&nbsp;&nbsp;资产数量：</td>
			  			<td><input id="txbcheckZC_num" name="num" style="width:110px;"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
			  		</tr>
	 			</table>
			</td>
		</tr>
		<tr>
			<td style="text-align:right;">规格型号：</td>
			<td>
	 			<table>
		  			<tr>
			  			<td><input id="txbcheckZC_xh" name="xh" /></td>
			  			<td style="padding-left:10px">出厂编号：</td>
			  			<td><input id="txbcheckZC_ccbh" name="ccbh" /></td>
			  		</tr>
	 			</table>
			</td>
		</tr>
		<tr>
			<td style="text-align:right;">购置时间：</td>
			<td>
 				<table>
	  				<tr>
			  			<td><input id="dtbcheckZC_gzsj" name="gzsj" /></td>
			  			<td style="padding-left:10px">折旧年限：</td>
			  			<td><input id="txbcheckZC_zjnx" name="zjnx"/>&nbsp;&nbsp;年<span style="color:#FF0000;margin-left: 3px;">*</span></td>
		  			</tr>
  				</table>
			</td>
		</tr>
		<tr>
			<td style="text-align:right;">资产照片：</td>
			<td><input id="fbcheckZC_zczp" name="file_upload" style="width:300px;"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
		</tr>
		</table>
		<table>
           <tr>
               <td height="30px"></td>
               <td width="140px"></td>
               <td width="100px" style="text-align: center"><a data-options="iconCls:'icon-ok'" class="easyui-linkbutton" onclick="CheckZCUI.checkZC()">确认</a></td>
               <td width="100px" style="text-align: center"><a data-options="iconCls:'icon-cancel'" onclick="CheckZCUI.cancel()" class="easyui-linkbutton">取消</a></td>
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

var CheckZCUI = {
		
	checkZC: function () {
		$.messager.progress();
		$("#checkZCForm").form("submit",{
			
			url:getContextPath() + "/console/zcgl/regist/checkZC.do",
			onSubmit: function (param) {
				
				var valid = $("#checkZCForm").form("validate");
				if (!valid) {
					$.messager.progress('close');				
					return valid;
				}
				param.id=checkRow.zcid;
				param.dm = checkRow.zcdm;
				param.lxid=$("#srbcheckZC_lxid").attr("lxid"); 
			},
			success: function (result) {
				$.messager.progress("close");
				if (result =="success") {
					$("#registZCList_newCheckdlg").dialog("close");
					$("#dgregistZCList").datagrid("reload");	
				} else if (result =="ZCDM_ERROR") {
					$.messager.alert("提示","该资产代码已经存在，请换一个！","info");
				} else if (result =="ZCMC_ERROR") {
					$.messager.alert("提示","资产名称不能为空或者是空格！","info");
				} else if (result =="ZJNX_ERROR") {
					$.messager.alert("提示","折旧年限必须是数字且不能小于0！","info");
				} else if (result =="NUM_ERROR") {
					$.messager.alert("提示","资产数量必须是正整数！","info");
				} else if (result=="PIC_ERROR") {
					$.messager.alert("提示","资产照片必须小于1MB！","info");
				} else if (result=="COST_ERROR") {
					$.messager.alert("提示","资产单价必须为正数！","info");
				}else {
					$.messager.alert("提示",result,"info");
				}
				
			}
		});
	},
	
	cancel: function () {
		$("#registZCList_newCheckdlg").dialog("close");
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
					$("#srbcheckZC_lxid").searchbox("setValue",czclx.mc);
					$("#srbcheckZC_lxid").attr("lxid",czclx.id); 
				}
				dialogObj.remove();
			}
		});
	}
};

$("#txbcheckZC_dm").textbox({
	disabled: true,
	iconCls:'icon-lock', 
    iconAlign:'right' ,
	value: "${zcInfo.zcdm}"
});

$("#txbcheckZC_mc").textbox({
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

$("#srbcheckZC_lxid").searchbox({
	required:true,
	editable: false,
	value:"${zcInfo.zclx}",
	searcher: function(value,name){
		CheckZCUI.query_zclx();
	}
});

$("#txbcheckZC_xh").textbox({
	width:110,
	value: "${zcInfo.xh}"
});

$("#txbcheckZC_ccbh").textbox({
	width:110,
	value: "${zcInfo.ccbh}"
});
var registGzsjStr = "${zcInfo.gzsj}";
var registGzsj = '';
if(registGzsjStr) {
	registGzsj = new Date(registGzsjStr);
}
$("#dtbcheckZC_gzsj").datebox({
	width:110,
	value: FormatDate(registGzsj),
});

$("#nbbcheckZC_cost").numberbox({
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

$("#txbcheckZC_num").numberbox({
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

$("#txbcheckZC_zjnx").numberbox({
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

$("#fbcheckZC_zczp").filebox({
	required: true,
	prompt: "必选项",
    buttonText: "选择照片", 
    buttonAlign: "right",
    accept: "image/jpeg"
});
</script>
</body>
</html>
