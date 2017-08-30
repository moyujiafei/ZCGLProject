<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>编辑资产类型</title>
</head>
<body>
	<div>
		<form id="dataForm" method="post" enctype="multipart/form-data">
			<table class="mytable">
				<tr>
					<input id="oldId" type="hidden" name="id" value="${Oldzclx.id }"/>
					<td style="text-align:right;">分 类 号:</td>
					<td><input id="zclxLxIdTextbox" name="lxid" /></td>
				</tr>
				<tr>
					<td style="text-align:right;">分类名称:</td>
					<td><input id="zclxMcTextbox" name="mc"/><span style="color:#FF0000;margin-left: 3px;">*</span>
						<input id="oldzclxMcTextbox" type="hidden" name="oldMC" value="${Oldzclx.mc }"/>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">分类照片:</td>
					<td><input id="zclxImageFilebox" type="text" name="image_upload" ></td>
				</tr>
				<tr>
					<td style="text-align:right;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
					<td><input id="zclxRemarkTextbox" name="remark" /></td>
				</tr>
			</table>
			<table style="margin-top: 10px;">
				<tr>
					<td width="140px"></td>
					<td width="100px" style="text-align: center"><a
						href="javascript:void(0);" id="okBtn_zclx">确定</a></td>
					<td width="100px" style="text-align: center"><a
						href="javascript:void(0);" id="cancelBtn_zclx">取消</a></td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$(function() {
			$("#zclxLxIdTextbox").textbox({
				prompt : "资产分类号",
				value: "${Oldzclx.lxId}",
				readonly:true,
				iconCls: 'icon-lock'
			});
			$("#zclxMcTextbox").textbox({
				prompt : "必填项",
				value: "${Oldzclx.mc}",
				required : true,
				validType : "remote['${pageContext.servletContext.contextPath}/console/catalog/zclxgl/checkZCLXByUpdateMC.do?oldMC="+$("#oldzclxMcTextbox").val()+"','newMC']", // 唯一性验证
				invalidMessage : "分类名称已存在",
			});
			$("#zclxImageFilebox").filebox({ 
				required : false,
				multiple:true,
				accept:'image/jpeg',
			    buttonText: '选择文件', 
			    buttonAlign: 'right' 
			}),
			$("#zclxRemarkTextbox").textbox({
				prompt : "备注",
				height: 60,
				value: "${Oldzclx.remark }",
				multiline : true
			});
			$("#okBtn_zclx").linkbutton({
				iconCls : "icon-ok",
				onClick : function() {
					$.messager.progress(); // 显示进度条
					$("#dataForm").form("submit", {
						url : getContextPath() + "/console/catalog/zclxgl/updateZCLX.do",
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
								$.messager.alert('成功', '修改成功', 'info');
								$("#updateZclxDialog").dialog('close');
								$("#zclxgnDatagrid").datagrid('reload');
							}
							else {
								$.messager.alert('失败', data, 'info');
							}
						}
					});
				}
			});
	
			$("#cancelBtn_zclx").linkbutton({
				iconCls : "icon-no",
				onClick : function() {
					$("#updateZclxDialog").dialog("close");
				}
			});
		});
	</script>
</body>

</html>
