<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建微信应用</title>
</head>

<body>
	<div>
		<form id="dataForm" method="post" enctype="multipart/form-data">
			<table align="center" cellspacing="5" style="margin-top: 15px;" >
				<tr>
					<td>企业号：</td>
					<td><input id="corpIdTextbox" name="corpId" style="padding-left: 6px;width:120px;"/></td>
					<td>企业名：</td>
					<td><input id="corpNameTextbox" name="corpName" style="padding-left: 6px;width:120px;"></td>
				</tr>
				<tr>
					<td>企业Logo：</td>
					<td colspan="3"><input id="corpIconFilebox" name="corpIcon" style="padding-left: 6px;width:325px;"></td>
				</tr>
				<tr>
					<td>应用名：</td>
					<td><input id="appNameTextbox" name="appName" style="padding-left: 6px;width:120px;"/></td>
					<td>AgentId：</td>
					<td><input id="agentIdNumberbox" name="agentId" style="padding-left: 6px;width:120px;"/></td>
				</tr>
				<tr>
					<td>Secret：</td>
					<td><input id="secretTextbox" name="secret" style="padding-left: 6px;width:120px;" /></td>
					<td>TOKEN：</td>
					<td><input id="tokenTextbox" name="token" style="padding-left: 6px;width:120px;"/></td>
				</tr>
				<tr>
					<td>EncodingAESKey：</td>
					<td colspan="3"><input id="aesKeyTextbox" name="aesKey" style="padding-left: 6px;width:325px"/></td>
				</tr>
				<tr>
					<td>可信域名：</td>
					<td><input id="callbackUrlTextbox" name="callbackUrl" style="padding-left: 6px;width:120px;"/></td>
					<td>授信回调域：</td>
					<td><input id="serverUrlTextbox" name="serverUrl" style="padding-left: 6px;width:120px;"/></td>
				</tr>
				<table>
					<tr>
						<td height="10px"></td>
					</tr>
				</table>
				<table>
					<tr>
						<td width="140px"></td>
						<td width="100px" style="text-align: center"><a
							href="javascript:void(0);" id="okBtn">确定</a></td>
						<td width="100px" style="text-align: center"><a
							href="javascript:void(0);" id="cancelBtn">取消</a></td>
					</tr>
				</table>
		</form>
	</div>

	<script type="text/javascript">
		$(function() {
			$("#corpIdTextbox").textbox({
				required : true,
			});
			$("#corpNameTextbox").textbox({
				required : true,
			});
			$("#corpIconFilebox").filebox({
				buttonText : '选择图片',
			    buttonAlign : 'right',
			    accept : '	image/jpeg'
			});
			$("#appNameTextbox").textbox({
				required : true,
			});
			$("#agentIdNumberbox").numberbox({
				required : true,
				value : "${curWxAppInfo.agentId}"
			});
			$("#secretTextbox").textbox({
				required : true,
			});
			$("#tokenTextbox").textbox({
				required : true,
			});
			$("#aesKeyTextbox").textbox({
				required : true,
			});
			$("#callbackUrlTextbox").textbox({
				required : true,
			});
			$("#serverUrlTextbox").textbox({
				required : true,
			});
		});
		$("#cancelBtn").linkbutton({
			iconCls : "icon-no",
			onClick : function() {
				$("#insertWxAppDialog").dialog("close");
			}
		});
		$("#okBtn").linkbutton({
			iconCls : "icon-ok",
			onClick : function() {
				$.messager.progress(); // 显示进度条
				$("#dataForm").form("submit", {
					url : getContextPath() + "/console/sys/wxapp/insertWXAPP.do",
					onSubmit : function(param) {
						var valid = $(this).form("validate");
						if (!valid) {
							$.messager.progress('close');
							$.messager.alert("提示", "请按照要求填写表单", "info");
						}
						return valid;
					},
					success : function(data) {
						$.messager.progress('close');
						//表单提交成功后
						if (data == 'success') {
							$.messager.alert('成功', '添加成功', 'info');
							$("#insertWxAppDialog").dialog('close');
							$("#wxAppListDatagrid").datagrid('reload');
						} else {
							$.messager.alert('失败', data, 'info');
						}
					}
				});
				
			}
	
		});
	</script>



</body>
</html>
