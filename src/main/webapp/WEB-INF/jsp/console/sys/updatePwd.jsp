<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title></title>

</head>

<body>
	<form id="updatePwdForm">
		<table align="center" style="border-collapse:separate;border-spacing:10px;">
			<tr>
				<td align="right">用户名：</td>
				<td><input type="text" class="easyui-textbox" data-options="iconCls:'icon-man',readonly:true" size="20" value="${sessionScope.loginInfo.name}"/></td>
				<td></td>
			</tr>
			<tr>
				<td align="right">登录名：</td>
				<td><input id="uname" type="text" class="easyui-textbox" data-options="iconCls:'icon-man',readonly:true" size="20" value="${sessionScope.loginInfo.uname}" /></td>
				<td></td>
			</tr>
			<tr>
				<td align="right">原密码：</td>
				<td><input id="oldpwd" name="oldpwd" type="password"  size="20" /></td>
				<td id="OlpwdAlert" style="color:#FF0000">*</td>
			</tr>
			<tr>
				<td align="right">新密码：</td>
				<td><input id="newpwd" name="newpwd" type="password" size="20" /></td>
				<td id="NewpwdAlert" style="color:#FF0000">*</td>
			</tr>
			<tr>
				<td align="right">确认新密码：</td>
				<td><input id="renewpwd" type="password" size="20" /></td>
				<td id="NewpwdAlert1" style="color:#FF0000">*</td>
			</tr>
		</table>
		<br>
		<br>
		<table align="center">
			<tr>
				<td align="center"><a id="saveBtn_updatePwd" href="javascript:void(0);">确认</a></td>
				<td width="50%"></td>
				<td><a id="cancel_updatePwd" href="javascript:void(0);">取消</a></td>
			</tr>
		</table>
	</form>

	<script type="text/javascript">
		$.extend($.fn.validatebox.defaults.rules, {
			equals : {
				validator : function(value, param) {
					return value == $(param[0]).val();
				},
				message : '两次输入的密码不一致，请重新输入！'
			}
		});
		$("#oldpwd").textbox({
			required : true,
			iconCls:'icon-more'
		});

		$("#newpwd").textbox({
			required : true,
			validType : 'length[5,12]',
			iconCls:'icon-more'
		});

		$("#renewpwd").textbox({
			required : true,
			validType : [ 'length[5,12]', "equals['#newpwd']" ],
			delay : 300,
			iconCls:'icon-more'
		});

		$("#saveBtn_updatePwd").linkbutton({
			iconCls : 'icon-save'
		});

		$("#cancel_updatePwd").linkbutton({
			iconCls : 'icon-cancel'
		});

		$("#cancel_updatePwd").click(function() {
			$("#pwddialog").dialog("close");
		});

		$("#saveBtn_updatePwd").click(function() {
			$.messager.progress();
			$("#updatePwdForm").form("submit", {
				url : getContextPath() + "/console/user/updatePwd.do", //表单提交地址
				onSubmit : function(param) {
					param.uid = "${sessionScope.loginInfo.uid}";
					var isvalidate = $(this).form("validate");
					if (!isvalidate) {
						$.messager.progress('close');
						$.messager.alert("提示", "请按照要求填写表单！", "info");
						return false;
					}
					return isvalidate;
				},
				success : function(data) {
					$.messager.progress('close');
					if(data == 'success'){
						$.messager.alert("成功", "修改成功,需要重新登录", "info",function(){
							window.location.href =getContextPath() + "/console/user/quit.do";
						});
					} else {
						$.messager.alert("错误", data, "warning");
					}
				}
			});
		});
	</script>
</body>
</html>
