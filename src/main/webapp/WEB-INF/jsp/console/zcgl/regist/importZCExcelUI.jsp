<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
  </head>
  
 <body>
 	<div>
 		<form id="importZCExcel_uploadForm" enctype="multipart/form-data" method="post">
	 		<table align="center" cellspacing="5px" style="padding-top: 50px;">
	 			<tr>
	 				<td>选择文件：</td>
	 				<td><input id="importZCExcel_fbfile" name="file_upload" type="text" style="width:250px"/><span style="color:#FF0000;margin-left: 3px;">*</span></td>
	 				<td><a id="importZCExcel_doUploadBtn" href="#" class="easyui-linkbutton" iconCls="icon-ok" style="color:black" onclick="importZCExcel.doUpload()">确定</a></td>
	 			</tr>
	 		</table>
 		</form>
 	</div> 
 <script type="text/javascript">
 
 	$("#importZCExcel_fbfile").filebox({
	    buttonText: '选择文件', 
	    buttonAlign: 'left' ,
	    multiple: false,
	    accept:"application/vnd.ms-excel",  //重要
	    required: true,
 	});
 	$("#importZCExcel_doUploadBtn").linkbutton({
 		iconCls : 'icon-ok',
 		onClick : function(){
 			objUpd.doUpload();
 		},
 	});
 	
 	var importZCExcel  = {
 			
 			doUpload : function(){
 	 			if($.trim($("#importZCExcel_fbfile").filebox("getText")) == ""){
 	 				$.messager.progress('close');
 	 				$.messager.alert('警告','请选择excel文件','warning');
 	 				return false;
 	 			};
 	 			$.messager.confirm('确定','您确认要上传选中的Excel吗？',function(result){
 	 				if(result){
 	 					$.messager.progress();
 	 					$('#importZCExcel_uploadForm').form('submit',{
 	 						url : getContextPath() + "/console/zcgl/regist/importZCExcel.do",
 	 						success : function(data){
 	 							$.messager.progress('close');
 	 							if(data == 'success') {
 	 								$("#dgregistZCList").datagrid('reload');
 	 								$("#dlgregistZCList_newImportPic").dialog('close');
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
