<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html"; charset=UTF-8>
    <title>拒绝资产报废</title>
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
     <div id="jjbfMain">
	   <form id="jjbfInfoForm" method="post">		    
		<table width="100%" border="0" style="border-spacing:10px;">
			<tr height="15"></tr>
			<tr><td width="1%"></td><td align="right">拒绝原因：</td><td><input class="easyui-textbox" data-options="multiline:true" id="refuseRemark" name="refuseRemark" style="width: 300px; height: 150px"/></td><td></td></tr>	
		</table>	
	   </form>
		<br>
		
	   	<table border="0">
			 <tr>
			   <td width='140px'></td>
			   <td width='100px' align="center"><a id="saveBtn_jjbf" href="javascript:void(0);" >确认</a></td>
			   <td width='100px' align="center"><a id="cancelBtn_jjbf" href="javascript:void(0);" >取消</a></td>			   
		    </tr>
	     </table>
	</div>
	
	<script type="text/javascript">	    
		    	 		
		$("#cancelBtn_jjbf").linkbutton({
			iconCls: "icon-cancel"
		});
		
		$("#cancelBtn_jjbf").click(function(){
			$("#jjbfInfoDiv").dialog("close");
		});
		
		$("#saveBtn_jjbf").linkbutton({    
		    iconCls: 'icon-save',
		    onClick : function(){			    	
		    	$.messager.progress();	// 显示进度条
		    	$("#jjbfInfoForm").form('submit',{		    		
		    		url:getContextPath() + "/console/zcgl/bfsq/refuseBFSQ.do",    
	    		    onSubmit: function(param){
	    		    	var isValid = $(this).form('validate');	    		   	    		    		    		    		    		    	
						if (!isValid){
							$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
							return false;
						}
						 var arr=$("#sqbfList").datagrid("getChecked");
	    			     var zcidStr="";
	    			     for(var i=0;i<arr.length;i++){
	    			    	 zcidStr+=arr[i].zcid+",";
	    			     }
	    			     zcidStr=zcidStr.substring(0,zcidStr.length-1);
	    			     param.zcidStr=zcidStr;
						 return isValid;
	    		    },
	    		    success:function(data){
	    		    	$.messager.progress('close');
	    		    	if(data == "success"){
	    					$.messager.alert('成功','提交成功','info');
	    					$("#jjbfInfoDiv").dialog('close');
	    					$("#sqbfList").datagrid("uncheckAll");
	    					$("#sqbfList").datagrid("reload");
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
