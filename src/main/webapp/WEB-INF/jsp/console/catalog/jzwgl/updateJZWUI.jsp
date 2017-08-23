<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑建筑物</title>
</head>
<body>
<form id="editJZWFrom" method="post">
	<div>
	  <table class="mytable" style="margin-top:5px">
	  <tr><td style="text-align:right;">校&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区 ： </td><td><input id="xqmc" name="xqid" ></td></tr>
	  <tr><td style="text-align:right;">建筑类型 ： </td><td><input id="lxmc" name="lxid"  ></td></tr>
	  <tr><td style="text-align:right;">建  筑  物 ： </td><td><input id="jzw" name="mc"  ><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
	  <tr><td style="text-align:right;">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址 ： </td><td><input id="dz" name="dz"  ></td></tr>
	  <tr><td style="text-align:right;">停用标志 ： </td><td><input id="tybz" name="tybz" ></td></tr>
	  </table>
	  <table>
	        <tr>
	            <td width="140px"></td>
	            <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="okBtn_jzw">确定</a></td>
	            <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancelBtn_jzw">取消</a></td>
	        </tr>
	  </table>
	</div>
	</form>
<script type="text/javascript">
	var updateJZW = $("#tbJzwDateGrid").datagrid("getSelected");
	$("#xqmc").combobox({   
	    required:true,  
	    url: getContextPath()+"/console/catalog/jzwgl/getXQCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable: false,
		value : updateJZW.xqid,
	});
	
	$("#lxmc").combobox({    
	    required:true,  
	    url: getContextPath()+"/console/catalog/jzwgl/getJZLXCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable: false,
		value : updateJZW.lxid,
	});
	
	$("#jzw").textbox({
		prompt: "必填项",
		required:true,
		value : updateJZW.jzw,
		validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/jzwgl/checkJZWByUpdateMC.do?oldJZWMC="+updateJZW.jzw+"','newJZWMC']",
        invalidMessage: "建筑名已存在",
	});
	
	$("#dz").textbox({
		multiline:true,
		height : 50,
		value : updateJZW.dz,
	});
	$("#tybz").combobox({
		required:true,  
	    url: getContextPath()+"/console/catalog/jzwgl/getTYBZCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable: false,
		value : updateJZW.tybz,
	});

	
	$("#okBtn_jzw").linkbutton({    
	    iconCls: 'icon-ok',
	    onClick : function(){
	    	$.messager.progress();	// 显示进度条
	    	$("#editJZWFrom").form('submit',{
	    		url:getContextPath() + "/console/catalog/jzwgl/updateJZW.do",    
    		    onSubmit: function(param){
    		    	var isValid = $(this).form('validate');
    		    	param.id = updateJZW.jzwId;
					if (!isValid){
						$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
					}
					 return isValid;
    		    },
    		    success:function(data){
    		    	$.messager.progress('close');
    		    	if(data == "success"){
    					$.messager.alert('成功','编辑建筑物成功','info');
    					$("#updateJZWUI").dialog('close');
    					$("#tbJzwDateGrid").datagrid("reload");
    				} else{
    					$.messager.alert('失败',data,'waring');
    				}
    		    },
	    	});
	    },
	});  
	$("#cancelBtn_jzw").linkbutton({    
	    iconCls: 'icon-no'   
	});  
	
	$("#cancelBtn_jzw").click(function(){    
		$("#updateJZWUI").dialog('close');
	});  
</script>
</body>
</html>