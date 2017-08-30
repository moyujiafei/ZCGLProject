<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>拒绝资产上交</title>
</head>
<body>
	<div id="refuseDialog">
		<form id="refuseForm" method="post">
			<table align="center" cellspacing="10px" style="padding-top: 20px;">
				<tr>
					<td align="center" valign="top" style="display:table-cell;">拒绝原因:</td>
					<td><input id="refuseReasons" name="refuseRemark"/></td>
				</tr>
				<tr>
					<td ></td>
				</tr>
				<tr>
					<td colspan="2" style="padding-left:85px"><a href="#" id="refuseSave">确定</a>&emsp;&emsp;&emsp;<a href="#" id="refuseCancel">取消</a></td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$("#refuseReasons").textbox({
			width : 300,
			height : 150,
			multiline : true,
		});
		$("#refuseSave").linkbutton({
			iconCls : 'icon-save',
			onClick : function(){
				$.messager.progress();
				$("#refuseForm").form('submit' ,{
					url : getContextPath() + "/console/zcgl/sendback/refuseSendbackSQ.do",
					onSubmit : function(param){
						var selectedData = $("#sjzcListDatagrid").datagrid("getChecked");
						var zcidStr="";
	    		    	var isValid = $(this).form('validate');
	    		    	for(var i=0;i<selectedData.length;i++){
	    			    	zcidStr += selectedData[i].zcid+",";
	    			    }
	    			    zcidStr = zcidStr.substring(0,zcidStr.length-1);
	    			    param.zcidString = zcidStr;
						if (!isValid){
							$.messager.progress('close');
						}
						return isValid;
					},
					success : function(data){
						$.messager.progress('close');
						if(data == 'success') {
							$.messager.alert('成功','拒绝上交资产成功','info');
							$("#refuseDialog").dialog("close");
							$("#sjzcListDatagrid").datagrid("uncheckAll");
							$("#sjzcListDatagrid").datagrid("reload");
						} else{
    						$.messager.alert('失败',data,'waring');
    					}
					},
				});
			},
		});
		$("#refuseCancel").linkbutton({
			iconCls : 'icon-no',
			onClick : function(){
				$("#refuseDialog").dialog("close");
			},
		});
	</script>
</body>
</html>