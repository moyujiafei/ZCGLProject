<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>低值易耗品调拨</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  
  
  <body>
  <div id="allocateYHPDialog">
   <form id="allocateYhpform" method="post">
		<table align="center" cellspacing="5px" style="margin-top:50px">
		  <tr><td style="text-align: right;">当前持有数量：</td><td><input id="allocateYHPUI_num" /></td></tr>
		  <tr><td style="text-align: right;">调拨部门：</td><td><input id="allocateYHPUI_bm" name="allocatebm"/><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		  <tr><td style="text-align: right;">调拨数量：</td><td><input id="allocateYHPUI_allocateNum" name="allocateNum"/><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		  <tr><td style="text-align: right;">存放地点：</td><td><input id="allocateYHPUI_cfdd" /><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		</table>
		<input id="allocateYHPUI_cfdd_fjId" type="hidden" name="cfdd" style="display: none;"/>
	</form>
	  <table style="margin-top:40px">
	        <tr>
	            <td width="140px"></td>
	            <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="confirm_allocateYHP">确定</a></td>
	            <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancel_allocateYHP">取消</a></td>
	        </tr>
	  </table>
	</div>
<script type="text/javascript">
	var yhp=$("#registYHPList").datagrid("getSelected");
	
	$("#allocateYHPUI_num").textbox({
		disable: true,
		iconCls: 'icon-lock',
		width: 320,
		value: yhp.num
	});
	
	$("#allocateYHPUI_bm").searchbox({
		width : 320,
		required:true,
		editable: false,
		searcher: function(value,name){
			allocateYHP.query_cfbm();
		},
		prompt: "必填项"
	});
	
	$("#allocateYHPUI_allocateNum").numberbox({
		value : 1,
		width : 320,
		editable : true,
		required:true,
		prompt: "必填项"
	});
	
	$("#allocateYHPUI_cfdd").searchbox({
		width : 320,
		required:true,
		editable: false,
		searcher: function(value,name){
			allocateYHP.query_yhpcfdd();
		},
		prompt: "必填项"
	});
	
	$("#confirm_allocateYHP").linkbutton({
		iconCls:'icon-ok',
		plain:true,
		
	});
	
	$("#cancel_allocateYHP").linkbutton({
		iconCls:'icon-no',
		plain:true,
	});
	
	$("#confirm_allocateYHP").bind('click',function(){
		$.messager.progress();
		$('#allocateYhpform').form('submit', {    
		    url: getContextPath()+"/console/yhpgl/yhpdj/allocateQYYHP.do",    
		    onSubmit: function(param){    
		        // do some check    
		        var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
					$.messager.alert("提示","请按照要求填写表单",'info');
				}else{
					var num=$("#allocateYHPUI_num").val();
					var allocateNum=$("#allocateYHPUI_allocateNum").val();
					if(allocateNum>num ||allocateNum<1){
						$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
						isValid=false;
						$.messager.alert("提示","调拨数量不是1-"+num+"范围内的正整数！",'info');
					}
				}
				param.yhpid=yhp.yhpId;
				param.allocatebm_deptno=$("#allocateYHPUI_bm").attr("deptno");
				return isValid;	// 返回false终止表单提交
		    },    
		    success:function(data){    
		        $.messager.progress('close');
		        if(data=="success"){
			        $.messager.alert("提示","调拨成功！",'info');
		        	$("#allocateYHPDialog").dialog('close');
		        	$("#registYHPList").datagrid("reload");
		        }else{
		        	$.messager.alert("提示",data,'info');
		        }  
		    }    
		});  
		
	});
	
	$("#cancel_allocateYHP").bind('click',function(){
		$("#allocateYHPDialog").dialog('close');
	});
	
	var allocateYHP = {
			query_yhpcfdd : function () {
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
							 $('#allocateYHPUI_cfdd').searchbox("setText",vfj.xqmc + vfj.jzw + vfj.floor+vfj.room);
							 cfdd = vfj.fjId;
							 $("#allocateYHPUI_cfdd_fjId").val(cfdd);
						 }
						 queryCFDDUI.remove();
		             }
				});
			},
			query_cfbm: function () {
				dialogObj = $("<div id='dialogObj'></div>");
				wxdept  = null;
				dialogObj.appendTo("body");
				$("#dialogObj").dialog({
					href: getContextPath() + "/console/wxgl/wxdept/queryWXDeptUI.do",
					title: "资产管理部门查询",
					width: 512,
					height: 300,
					queryParams: {isEdit: true},
					onClose: function () {
						if(wxdept != null) {
							dialogObj.remove();
							$("#allocateYHPUI_bm").searchbox("setValue",wxdept.deptName);
							$("#allocateYHPUI_bm").attr("deptno",wxdept.deptNo);
						}
						dialogObj.remove();
					}
				});
			},
	}
</script>
</body>
</html>