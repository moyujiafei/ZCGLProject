<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>新建易耗品分类</title>
</head>
<body>
	<div>
		<form id="dataForm" method="post" enctype="multipart/form-data">
			<table class="mytable">
				<tr>
					<td style="text-align:right;">分 类 号:</td>
					<td>
						<input id="yhplxLxIdTextbox" name="lxid" />
						<span style="color:#FF0000;margin-left: 3px;">*</span>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">分类名称:</td>
					<td>
						<input id="yhplxMcTextbox" name="mc" />
						<span style="color:#FF0000;margin-left: 3px;">*</span>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">分类照片:</td>
					<td>
						<input id="yhplxImageFilebox" type="text" name="image_upload" >
						<span style="color:#FF0000;margin-left: 3px;">*</span>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
					<td><input id="yhplxRemarkTextbox" name="remark" /></td>
				</tr>
			</table>
			<table style="margin-top: 10px;">
				<tr>
					<td width="140px"></td>
					<td width="100px" style="text-align: center"><a
						href="javascript:void(0);" id="okBtn_yhplx">确定</a></td>
					<td width="100px" style="text-align: center"><a
						href="javascript:void(0);" id="cancelBtn_yhplx">取消</a></td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$(function() {
			$("#yhplxLxIdTextbox").textbox({
				prompt: "必填项,长度为4位以内的数字",
				required : true,
				validType : "remote['${pageContext.servletContext.contextPath}/console/catalog/yhplxgl/checkYHPLXByLXID.do','lxid']", // 唯一性验证
				invalidMessage : "分类号已存在或长度超过4位",
			});
			$("#yhplxMcTextbox").textbox({
				prompt: "必填项",
				required : true,
				validType : "remote['${pageContext.servletContext.contextPath}/console/catalog/yhplxgl/checkYHPLXByMC.do','mc']", // 唯一性验证
				invalidMessage : "分类名称已存在",
			});
			$("#yhplxImageFilebox").filebox({ 
				prompt: "必选项",
				required : true,
				multiple:true,
				accept:'image/jpeg',
			    buttonText: '选择文件', 
			    buttonAlign: 'right' 
			}),
			$("#yhplxRemarkTextbox").textbox({
				height: 60,
				multiline : true
			});
			$("#okBtn_yhplx").linkbutton({
				iconCls : "icon-ok",
				onClick : function() {
					$.messager.progress(); // 显示进度条
					$("#dataForm").form("submit", {
						url : getContextPath() + "/console/catalog/yhplxgl/insertYHPLX.do",
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
								$("#insertYHPLXDialog").dialog('close');
								$("#yhplxglDatagrid").datagrid('reload');
							}
							else {
								$.messager.alert('失败', data, 'info');
							}
						}
					});
				}
			});
	
			$("#cancelBtn_yhplx").linkbutton({
				iconCls : "icon-no",
				onClick : function() {
					$("#insertYHPLXDialog").dialog("close");
				}
			});
		});
	</script>
</body>

</html>
