<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>易耗品补货页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  
  
  <body>
    <div id="bhYHPDialog">
   <form id="bhYhpform" method="post">
		<table align="center" cellspacing="10dp" style="margin-top: 50px;">
		  <tr><td style="text-align: right;">当前持有数量：</td><td><input id="bhYHPUI_num" name="num"/></td></tr>
		  <tr style="height:20px"></tr>
		  <tr><td style="text-align: right;">新增入库数量：</td><td><input id="bhYHPUI_add_num" name="addNum"/></td></tr>
		</table>
	</form>
	  <table>
                    <tr>
                        <td height="20px"></td>
                    </tr>
                </table>
                <table style="margin-bottom:15px">
                    <tr>
                        <td width="140px"></td>
                        <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="confirm_bhYHP">确定</a></td>
                        <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancel_bhYHP">取消</a></td>
                    </tr>
                </table>
	</div>
<script type="text/javascript">
	$("#confirm_bhYHP").linkbutton({
		iconCls : "icon-ok",
		plain : true,
	}); 
	$("#cancel_bhYHP").linkbutton({
		iconCls : "icon-no",
		plain : true,
	});
	
	$("#confirm_bhYHP").bind('click',function(){
		var addnum=$("#bhYHPUI_add_num").numberbox('getValue');
		if(addnum<=0||addnum%1!=0){
			$.messager.alert("提示","新增入库数量应为大于0的正整数");
		}else{
		$("#bhYhpform").form('submit',{
			url: getContextPath() + "/console/yhpgl/yhpdj/addYHP.do",
			onSubmit: function(param){
				var valid = $("#bhYhpform").form("validate");
				if (!valid) {
					$.messager.progress('close');
					return valid;
				}
				
				param.yhpid=bhYHP.yhpId;
				
		    },    
		    success:function(result){    
		    	$.messager.progress('close');
				if (result == "success") {
					$("#bhYHPDialog").dialog("close");
					$("#registYHPList").datagrid("reload");	
				} else {
					$.messager.alert("提示",result,"info");
				}   
		    },
			
		});
		}
	});
	
	$("#cancel_bhYHP").bind('click',function(){
		$("#bhYHPDialog").dialog('close');		
	});
	
    $("#bhYHPUI_num").textbox({
		value : bhYHP.num,
		width : 320,
		iconCls:'icon-lock', 
	    iconAlign:'right' ,
		disabled:true,
    });
    
    $("#bhYHPUI_add_num").numberbox({
    	value : 1,
    	precision : 0,
		editable : true,
		prompt: "必填项",
		width : 320,
    });
</script>
  </body>
  
  
</html>
