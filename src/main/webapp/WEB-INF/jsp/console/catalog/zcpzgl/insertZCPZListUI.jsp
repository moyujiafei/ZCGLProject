<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>批量导入资产品种</title>
<style type="text/css">
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		}
</style>
</head>
<body>
	<div>
		<form id="fileForm"  method="post" enctype="multipart/form-data">
			<table align="center" cellspacing="5px" style="padding-top: 50px;">
		 			<tr>
		 				<td>选择文件：</td>
		 				<td><input id="zcpzExcelFilebox" name="excel_upload" type="text" style="width:250px"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
		 				<td><a href="javascript:void(0);" id="okBtn_zclx">确定</a></td>
		 			</tr>
	 		</table>
		</form>
	</form>
	</div>
	<script type="text/javascript">
		$(function() {
		//文件上传框
		$("#zcpzExcelFilebox").filebox({ 
			width:'200px',
			required:true,
			prompt: "添加Excel文件",
			multiple:true,
			accept:'application/vnd.ms-excel ',
		    buttonText: '选择文件', 
		    buttonAlign: 'right' 
		})
		$("#okBtn_zclx").linkbutton({
				iconCls : "icon-ok",
				onClick : function() {
					$.messager.progress(); // 显示进度条
					$("#fileForm").form("submit", {
						url : getContextPath() + "/console/catalog/zcpzgl/insertZCPZList.do",
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
								$.messager.alert('成功', '导入成功', 'info');
								$("#insertZcpzListDialog").dialog('close');
								$("#zcpzListDatagrid").datagrid('reload');
							}
							else {
								$.messager.alert('失败', data, 'info');
							}
						}
					});
				}
			});

		});

	</script>
</body>

</html>
