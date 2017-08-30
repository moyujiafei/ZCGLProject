<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建建筑物</title>
</head>
<body>
<form id="insertJZWForm" method="post">
	<div>
	 <table class="mytable" style="margin-top:5px">
	  <tr><td style="text-align:right;">校&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区 ： </td><td><input id="xqmc" name="xqId"  ></td></tr>
	  <tr><td style="text-align:right;">建筑类型 ： </td><td><input id="lxmc" name="lxId"  ></td></tr>
	  <tr><td style="text-align:right;">建 筑 物 ： </td><td><input id="jzw" name="jzw"  ><span style="color:#FF0000;margin-left: 3px;">*</span></td><td id="jzwMessage" style="color:FF0000"></td></tr>
	  <tr><td style="text-align:right;">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址 ： </td><td><input id="dz" name="dz"  ></td></tr>
	  <tr><td style="text-align:right;">停用标志 ： </td><td><input id="tybz" name="tybz"  ></td></tr>
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
	$("#xqmc").combobox({    
	    required:true,  
	    url: getContextPath()+"/console/catalog/jzwgl/getXQCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable: false,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		}
	});
		$("#lxmc").combobox({    
	    required:true,  
	    url: getContextPath()+"/console/catalog/jzwgl/getJZLXCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable: false,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		}
	});
	
	$("#jzw").textbox({
		prompt: "必填项",
		required:true,  
		validType: "remote['${pageContext.servletContext.contextPath}/console/catalog/jzwgl/checkJZWByMC.do','mc']",
        invalidMessage: "建筑名已存在",
	});
	
	$("#dz").textbox({
		multiline:true,
		height : 50,
	});
	$("#tybz").combobox({
		required:true,  
	    url: getContextPath()+"/console/catalog/jzwgl/getTYBZCombo.do",
	    valueField: 'id',
		textField: 'text',
		editable: false,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		}
	});
	
	$("#okBtn_jzw").linkbutton({    
	    iconCls: 'icon-ok',
    	onClick : function(){
    		$.messager.progress();	// 显示进度条
    		$("#insertJZWForm").form('submit', {    
    		    url:getContextPath()+"/console/catalog/jzwgl/insertJZW.do",    
    		    onSubmit: function(param) {
                    var valid = $(this).form("validate");
                    if (!valid) {
                        $.messager.progress('close');
                    }
                    return valid;
                },
    		    success:function(data){
    		    	$.messager.progress('close');
    		    	if(data == "success"){
						$.messager.alert('成功','新增建筑物成功','info');
						$("#insertJZWUI").dialog('close');
						$("#tbJzwDateGrid").datagrid("reload");
					} else{
						$.messager.alert('失败',data,'waring');
					}
    		    }
    		}); 
	    }
	});  
	$("#cancelBtn_jzw").linkbutton({    
	    iconCls: 'icon-no'   
	});  
	
	$("#cancelBtn_jzw").click(function(){    
		$("#insertJZWUI").dialog('close');
	});  
	
</script>
</body>
</html>