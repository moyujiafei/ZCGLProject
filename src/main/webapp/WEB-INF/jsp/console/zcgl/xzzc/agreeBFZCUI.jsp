<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html"; charset=UTF-8>
    <title>同意报废资产</title>
    <style type="text/css">
        body{
           margin-left:0px;             
           margin-top:0px;
           margin-right:0px;
           margin-bottom:0px;
        }
    </style>
  </head>
  
  <body>
     <div id="zcbfMain">
	     <form id="zcbfInfoForm" method="post">		    
			<table class="mytable">
			<tr height="15"></tr>
			<tr><td align="right">报废原因：</td><td><input class="easyui-textbox" data-options="multiline:true" id="remark" name="remark" style="width: 350px; height: 100px"/></td><td></td></tr>	
		</table>	
		<br>
		<br>
	   <table border="0">
			 <tr>
			   <td width='140px'></td>
			   <td width='100px' align="center"><a id="saveBtn_zcbf" href="javascript:void(0);" >确认</a></td>
			   <td width='100px' align="center"><a id="cancelBtn_zcbf" href="javascript:void(0);" >取消</a></td>			   
		    </tr>
	      </table>
	   </form>
	</div>
	
	<script type="text/javascript">
	    var VZC = $("#xzzcList").datagrid("getSelected");	     	 
		
		$("#cancelBtn_zcbf").linkbutton({
			iconCls: "icon-cancel",
		});
		
		$("#cancelBtn_zcbf").click(function(){
			$("#zcbfInfoDiv").dialog("close");
		});
		
		$("#saveBtn_zcbf").linkbutton({    
		    iconCls: "icon-save",
		    onClick : function(){
		    	$.messager.progress();	// 显示进度条
		    	$("#zcbfInfoForm").form('submit',{
		    		url:getContextPath() + "/console/zcgl/xzzc/agreeBFZC.do",    
	    		    onSubmit: function(param){
	    		    	var isValid = $(this).form('validate');
	    		    	param.zcid = VZC.zcid;
						if (!isValid){
							$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
						}
						 return isValid;
	    		    },
	    		    success:function(data){
	    		    	$.messager.progress('close');
	    		    	if(data == "success"){
	    					$.messager.alert('成功','提交成功','info');
	    					$("#zcbfInfoDiv").dialog('close');
	    					$("#xzzcList").datagrid("reload");
	    				} else{
	    					$.messager.alert('失败',data,'waring');
	    				}
	    		    },
		    	});
		    },
		});  
	</script>
  </body>
</html>
