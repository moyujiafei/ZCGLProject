<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html"; charset=UTF-8>
    <title>同意资产报废</title>
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
			<table align="center" style="border-spacing:10px;padding-top:15px;">
			    <tr align="right">
			    	<td width="1%"></td>
			        <td width="20%">新的存放地点：</td>
			        <td style="width: 200px; height:100px"><input id="newBFSQCFDD" name="newCFDDMC" />
			        </td>
			        <td><span style="color:#FF0000;margin-left: 3px;">*</span></td>
			        <td style="padding-left:0px" ><a href="#" id="searchBFSQCFDDBtn"></a></td>
			    </tr>	
		    </table>
		 </form>   	
		  <br>
	       <table>
			 <tr>
			   <td width='140px'></td>
			   <td width='100px' align="center"><a id="saveBtn_zcbfsq" href="javascript:void(0);" >确认</a></td>
			   <td width='100px' align="center"><a id="cancelBtn_zcbfsq" href="javascript:void(0);" >取消</a></td>			   
		     </tr>
	       </table>	   
	</div>
	
	<script type="text/javascript">	
		var cfdd;
	   	$("#newBFSQCFDD").textbox({
	   	    prompt: "必填项",
	   		editable : false,
	   		required : true,
	   		width : 300,
	   		height : 150,
	   		multiline:true,
	   	});    
	   	$("#searchBFSQCFDDBtn").linkbutton({
	   		iconCls : 'icon-search'
	   	});   
	   	
	   	$("#searchBFSQCFDDBtn").bind('click',function(){
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
						 $('#newBFSQCFDD').textbox("setText",vfj.xqmc + vfj.jzw + vfj.floor+vfj.room);
						 cfdd = vfj.fjId;
					 }
					 queryCFDDUI.remove();
	             }
			});
	   	});
	   	
		$("#cancelBtn_zcbfsq").linkbutton({
			iconCls: "icon-cancel"
		});
		
		$("#cancelBtn_zcbfsq").click(function(){
			$("#sqbfInfoDiv").dialog("close");
		});
		$("#saveBtn_zcbfsq").linkbutton({    
		    iconCls: 'icon-save',
		    onClick : function(){
		    	$.messager.progress();	// 显示进度条
		    	$("#zcbfInfoForm").form('submit',{
		    		url:getContextPath() + "/console/zcgl/bfsq/agreeBFSQ.do",    
	    		    onSubmit: function(param){
	    		    	var isValid = $(this).form('validate');	    		   	    		    		    		    		    		    	
						if (!isValid){
							$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
							return false;
						}	
						param.newCFDD = cfdd;
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
	    					$("#sqbfInfoDiv").dialog('close');	
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
