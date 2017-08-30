<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>建筑物批量导入</title>
</head>
<body>
<div>
	<table align="center" cellspacing="5px" style="padding-top: 50px;">
		<tr>
			<td>选择文件：</td>
			<td style="text-align: center;">
			<form id="rawPicUploadForm" enctype="multipart/form-data" method="post">
			    <input id="file_upload" name="file_upload" type="text" ><span style="color:#FF0000;margin-left: 3px;">*</span>
		    </form>
			</td>
			<td><a id="doUploadBtn" href="#" class="easyui-linkbutton" iconCls="icon-ok" >确定</a></td>
		 </tr>
	</table>
</div>
<script type="text/javascript">
	$('#file_upload').filebox({  
		width: 200,
	    buttonText: '选择文件', 
	    buttonAlign: 'right' ,
	    multiple: false,
	    accept:"application/vnd.ms-excel",
	    required: true,
	});
	
	$("#doUploadBtn").linkbutton({
		iconCls:'icon-ok',
		onClick: function(){
			objUpd.doUpload();
		},
	});
	
	var objUpd = {
			doUpload : function(){
    			if($.trim($("#file_upload").filebox("getText")) == ""){
					$.messager.progress('close');
					$.messager.alert('警告','请选择excel文件','warning');
					return false;
				}
	    		$.messager.confirm('确认上传', '确定要上传选中的excel文件吗？', function(r){
					if (r){
						$.messager.progress();	// 显示进度条
						$('#rawPicUploadForm').form('submit', {
							url: getContextPath() + "/console/catalog/jzwgl/importJZWExcel.do",
							success: function(data){
								$.messager.progress('close');//如果提交成功则隐藏进度条
								if(data == 'success'){
									$("#tbJzwDateGrid").datagrid("reload");
									$("#importJZWUI").dialog("close");
									$.messager.alert('成功','导入成功','info');
								} else {
									$.messager.alert('警告',data,'warning');
								}
							}
						});
					}
				});
			},
	};

</script>
</body>
</html>    