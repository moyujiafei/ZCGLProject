<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>My JSP 'importFJExcelUI.jsp' starting page</title>
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
 		<form id="fileUploadForm" enctype="multipart/form-data" method="post">
	 		<table align="center" cellspacing="5px" style="padding-top: 50px;">
	 			<tr>
	 				<td>选择文件：</td>
	 				<td><input id="file_upload" name="file_upload" type="text" style="width:250px"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
	 				<td><a id="doUploadBtn" href="#" class="easyui-linkbutton" iconCls="icon-ok" >确定</a></td>
	 			</tr>
	 		</table>
 		</form>
 	</div> 
 <script type="text/javascript">
 	$("#file_upload").filebox({
 		width: 200,
	    buttonText: '选择文件', 
	    buttonAlign: 'right' ,
	    multiple: false,
	    accept:"application/vnd.ms-excel",  //重要
	    required: true,
 	});
 	$("#doUploadBtn").linkbutton({
 		iconCls : 'icon-ok',
 		onClick : function(){
 			objUpd.doUpload();
 		},
 	});
 	
 	var objUpd  = {
 			doUpload : function(){
 	 			if($.trim($("#file_upload").filebox("getText")) == ""){
 	 				$.messager.progress('close');
 	 				$.messager.alert('警告','请选择excel文件','warning');
 	 				return false;
 	 			};
 	 			$.messager.confirm('确定','您确认要上传选中的Excel吗？',function(result){
 	 				if(result){
 	 					$.messager.progress();
 	 					$('#fileUploadForm').form('submit',{
 	 						url : getContextPath() + "/console/catalog/fjgl/importFJExcel.do",
 	 						success : function(data){
 	 							$.messager.progress('close');
 	 							if(data == 'success') {
 	 								$("#fjListDatagrid").datagrid('reload');
 	 								$("#importFJUI").dialog('close');
 	 								$.messager.alert('成功','导入成功','info');
 	 							}else{
 	 								$.messager.alert('失败',data,'warning');
 	 							}
 	 						}
 	 					});
 	 				}
 	 			});
 			}
 	};
 </script>  
 </body>
</html>
