<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>拒绝资产维修</title>
</head>
<body>
<div id="refuseWXSQDialog">
		<form id="refuseWXSQForm">
			<table align="center" cellspacing="10px" style="padding-top: 20px;">
				<tr>
					<td align="center" valign="top" style="display:table-cell;">拒绝原因:</td>
					<td><input id="refuseWXSQReasons" name="refuseRemark"/></td>
				</tr>
				<tr></tr>
				<tr>
					<td colspan="2" style="padding-left:85px"><a href="#" id="refuseWXSQSave">确定</a>&emsp;&emsp;&emsp;<a href="#" id="refuseWXSQCancel">取消</a></td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$("#refuseWXSQReasons").textbox({
			width : 300,
			height : 150,
			multiline : true,
		});
		$("#refuseWXSQSave").linkbutton({
			iconCls : 'icon-save',
			onClick : function(){
				$.messager.progress();
				$("#refuseWXSQForm").form('submit' ,{
					url : getContextPath() + "/console/zcgl/wxsq/refuseWXSQ.do",
					onSubmit : function(param){
						var selectedData = $("#sqwxListDatagrid").datagrid("getChecked");
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
							$.messager.alert('成功','拒绝维修资产成功','info');
							$("#refuseSQWXDialog").dialog("close");
							$("#sqwxListDatagrid").datagrid("uncheckAll");
							$("#sqwxListDatagrid").datagrid("reload");
						} else{
    						$.messager.alert('失败',data,'waring');
    					}
					},
				});
			},
		});
		$("#refuseWXSQCancel").linkbutton({
			iconCls : 'icon-no',
			onClick : function(){
				$("#refuseSQWXDialog").dialog("close");
			},
		});
	</script>
</body>
</html>