<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>当前用户信息</title>

<style type="text/css">
<!--
body {
	margin-right: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

</style>

</head>

<body>
	<table border="0" cellpadding="0" cellspacing="0" style="margin-left: auto;margin-right: auto;width: 100%;">
		<tr>
			<%--<td style="width: 40%">
				<c:choose>
					<c:when test="${empty sessionScope.loginInfo.wxUsername}">
						<div style="width: 100%;text-align: center;font-size: 16px;font-weight: bolder;">请扫描二维码以绑定微信账号</div>
						<div>
							<img alt="" src="${pageContext.servletContext.contextPath }/console/user/getQrcodImg.do?randomVal=<%=Math.random()%>">
						</div>
					</c:when>
					<c:otherwise>
						<div id="reBindBtn" style="width: 100%;text-align: center;font-size: 16px;font-weight: bolder;display:block;">
							<span>重新绑定微信账号:</span><br><br>
							<span><a class="easyui-linkbutton" iconCls="icon-redo" onclick="currUserOpt.toRebind();">重新绑定</a></span>
						</div>
						<div id="reBindQrcode" style="display: none;">
							<div style="width: 100%;text-align: center;font-size: 16px;font-weight: bolder;">请扫描二维码以绑定微信账号</div>
							<div>
								<img id="qrcodeImg4Bind" alt="" src="">
							</div>
						</div>
					</c:otherwise>
				</c:choose>
			</td>--%>
			<td>
				<table align="center" border="0px" style="border-collapse:separate;border-spacing:15px;margin-top: 15px;">
					<tr>
						<td align="right">姓名：</td>
						<td><input type="text" class="easyui-textbox" size="30" data-options="iconCls: 'icon-lock',readonly:true" style="text-align:center;" value="${sessionScope.loginInfo.name}" /></td>
					</tr>
					<tr>
						<td align="right">登录名：</td>
						<td><input type="text" class="easyui-textbox" size="30" data-options="iconCls: 'icon-lock',readonly:true" style="text-align:center;" value="${sessionScope.loginInfo.uname}" /></td>
					</tr>
					<tr>
						<td align="right">角色：</td>
						<td><input type="text" class="easyui-textbox" size="30" data-options="iconCls: 'icon-lock',readonly:true" style="text-align:center;" value="${sessionScope.loginInfo.roleName}" /></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-no" onclick="javascript:$('#currUser').dialog('close');">关闭</a></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
<script type="text/javascript">
var currUserOpt = {
	toRebind : function(){
		$("#reBindBtn").hide();
		$("#reBindQrcode").show(0,function(){
			$("#qrcodeImg4Bind").attr("src",getContextPath() + "/console/user/getQrcodImg.do?randomVal=<%=Math.random()%>");
		});
	}
}
</script>
</body>
</html>
