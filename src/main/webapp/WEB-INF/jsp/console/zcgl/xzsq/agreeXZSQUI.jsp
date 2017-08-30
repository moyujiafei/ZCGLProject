<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html"; charset=UTF-8>
    <title>同意资产闲置</title>
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
     <div id="zcxzMain">
	     <form id="zcxzInfoForm" method="post">		    
			<table width="100%" border="0" style="border-spacing:10px;">
			<tr height="15"></tr>
			<tr>
				<td align="right">新的存放地点：</td>
				<td><input id="newXZSQCFDD" name="newCFDDMC" style="width: 300px; height: 150px"/>
					<span style="color:#FF0000;margin-left: 3px;">*</span>
				</td>
				 <td style="padding-left:0px" ><a href="#" id="searchXZSQCFDDBtn"></a></td>
			</tr>	
		</table>	
		<br>
		
	   <table border="0">
			 <tr>
			   <td width='140px'></td>
			   <td width='100px' align="center"><a id="saveBtn_zcxzsq" href="javascript:void(0);" >确认</a></td>
			   <td width='100px' align="center"><a id="cancelBtn_zcxzsq" href="javascript:void(0);" >取消</a></td>			   
		    </tr>
	      </table>
	   </form>
	</div>
	
	<script type="text/javascript">	    	      	 
		var cfdd;
	   	$("#newXZSQCFDD").textbox({
	   		editable : false,
	   		required : true,
	   		width : 300,
	   		height : 150,
	   		prompt: "必填项",
	   		multiline:true,
	   	});  
	   	$("#searchXZSQCFDDBtn").linkbutton({
	   		iconCls : 'icon-search'
	   	});   
	   	
	   	$("#searchXZSQCFDDBtn").bind('click',function(){
	   		var queryCFDDUI = $("<div id='queryCFDDUI'></div>");
			queryCFDDUI.appendTo("body");
			vfj = null;
			$("#queryCFDDUI").dialog({
				title : "查找存放地点",
				href : getContextPath() + "/console/catalog/fjgl/queryCFDDUI.do",
				height : 300,
				width : 512,
				closed : false,
				 onClose: function() {
					 if (vfj != null){
						 queryCFDDUI.remove();
						 $('#newXZSQCFDD').textbox("setText",vfj.xqmc + vfj.jzw + vfj.floor+vfj.room);
						 cfdd = vfj.fjId;
					 }
					 queryCFDDUI.remove();
	             }
			});
	   	});
		$("#cancelBtn_zcxzsq").linkbutton({
			iconCls: "icon-cancel"
		});
		
		$("#cancelBtn_zcxzsq").click(function(){
			$("#sqxzInfoDiv").dialog("close");
		});
		
		$("#saveBtn_zcxzsq").linkbutton({    
		    iconCls: 'icon-save',
		    onClick : function(){
		    	$.messager.progress();	// 显示进度条
		    	$("#zcxzInfoForm").form('submit',{
		    		url:getContextPath() + "/console/zcgl/xzsq/agreeXZSQ.do",    
	    		    onSubmit: function(param){
	    		    	var isValid = $(this).form('validate');	    		   	    		    		    		    		    		    	
						if (!isValid){
							$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
							return false;
						}	
						param.newCFDD = cfdd;
						 var arr=$("#sqxzList").datagrid("getChecked");
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
	    					$("#sqxzInfoDiv").dialog('close');
	    					$("#sqxzList").datagrid("uncheckAll"); 
	    					$("#sqxzList").datagrid("reload");
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
