<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>低值易耗品登记</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  
  
  <body>
  <div id="registYHPDialog">
   <form id="registYhpform" method="post" enctype="multipart/form-data">
		<table align="center" cellspacing="5px" style="margin-top:20px">
		  <tr><td style="text-align: right;">易耗品类型：</td><td><input id="registYHPUI_yhplx" /><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		  <tr><td style="text-align: right;">易耗品照片：</td><td><input id="registYHPUI_yhpzp" name="file_upload"/><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		  <tr><td style="text-align: right;">规格型号：</td><td><input id="registYHPUI_xh" name="xh"/></td></tr>
		  <tr><td style="text-align: right;">出厂编号：</td><td><input id="registYHPUI_ccbh" name="ccbh"/></td></tr>
		  <tr><td style="text-align: right;">持有数量：</td><td><table><tr><td><input id="registYHPUI_num" name="num"/><span style="color:#FF0000;margin-left: 3px;">*</span>
		  <td style="width:23px"></td><td style="text-align: right;">库存下限：</td><td><input id="registYHPUI_left_limit" name="leftLimit"/><span style="color:#FF0000;margin-left: 3px;">*</span></td><tr></table></td></tr>
		  <tr><td style="text-align: right;">存放地点：</td><td><input id="registYHPUI_cfdd" /><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		</table>
		<input id="registYHPUI_yhplx_Id" type="hidden" name="lx" style="display: none;"/>
		<input id="registYHPUI_cfdd_fjId" type="hidden" name="cfdd" style="display: none;"/>
	</form>
	  <table>
	        <tr>
	            <td width="140px"></td>
	            <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="confirm_addYHP">确定</a></td>
	            <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancel_addYHP">取消</a></td>
	        </tr>
	  </table>
	</div>
<script type="text/javascript">
	
	var registYHP = {
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
							 $('#registYHPUI_cfdd').searchbox("setText",vfj.xqmc + vfj.jzw + vfj.floor+vfj.room);
							 cfdd = vfj.fjId;
							 $("#registYHPUI_cfdd_fjId").val(cfdd);
						 }
						 queryCFDDUI.remove();
		             }
				});
			},
			queryYhplx: function(){
			dialogObj=$("<div id='YhpLxDialogDiv'></div>");
			dialogObj.appendTo("body");
			cyhplx=null;
			$("#YhpLxDialogDiv").dialog({
				title: "查找易耗品类型",
				href: getContextPath()+"/console/catalog/yhplxgl/queryYhplxUI.do",
				width: 512,
                height: 300,
                cache: false,
				queryParams: {isEdit: true},
				modal: true,
				inline: true,
				onClose: function(){
					if(cyhplx!=null){
						$("#registYHPUI_yhplx").searchbox("setValue",cyhplx.mc);
						$("#registYHPUI_yhplx").attr("id",cyhplx.id);
						$("#registYHPUI_yhplx_Id").val(cyhplx.id);
					}
					dialogObj.remove();// 关闭时remove对话框
				}
			});
		}
	};
	
	$("#confirm_addYHP").linkbutton({
		iconCls:'icon-ok',
		plain:true,
		
	});
	
	$("#cancel_addYHP").linkbutton({
		iconCls:'icon-no',
		plain:true,
	});
	
	$("#confirm_addYHP").bind('click',function(){
		$.messager.progress();
		$('#registYhpform').form('submit', {    
		    url: getContextPath()+"/console/yhpgl/yhpdj/insertQYYHP.do",    
		    onSubmit: function(param){    
		        // do some check    
		        var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
					$.messager.alert("提示","请按照要求填写表单",'info');
				}else{
					var num=$("#registYHPUI_num").val();
					var limit=$("#registYHPUI_left_limit").val();
					if(isNaN(num) || num<=0 ){
						$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
						isValid=false;
						$.messager.alert("提示","持有数量不是大于0的正整数！",'info');
					}
					if(isNaN(limit) || limit<=0 ){
						$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
						isValid=false;
						$.messager.alert("提示","持有下限不是大于0的正整数！",'info');
					}
				}
				//param.lx=$("#registYHPUI_yhplx").attr("id");
				return isValid;	// 返回false终止表单提交
		    },    
		    success:function(data){    
		        $.messager.progress('close');
		        if(data=="success"){
			        $.messager.alert("提示","登记成功！",'info');
		        	$("#registYHPDialog").dialog('close');
		        }else{
		        	$.messager.alert("提示",data,'info');
		        }  
		    }    
		});  
		
	});
	
	$("#cancel_addYHP").bind('click',function(){
		$("#registYHPDialog").dialog('close');
	});
	
	$("#registYHPUI_yhpzp").filebox({
			prompt: "单个文件小于1M，格式为jpg",
		 	width:320,
	 	    accept:'image/jpeg',
			buttonText:'选择图片',
			buttonAlign:'right',
			required: true
		});
	
	$("#registYHPUI_yhplx").searchbox({
		width : 320,
		required:true,
		editable: false,
		searcher: function(value,name){
			registYHP.queryYhplx();
		}
	});
	
	$("#registYHPUI_xh").textbox({
		width : 320,
		editable : true,
	});
	$("#registYHPUI_ccbh").textbox({
		width : 320,
		editable : true,
	});
	
	
	$("#registYHPUI_num").numberbox({
		value: 1,
		width : 110,
		required:true,
		editable : true,
		prompt: "必填项",
		precision: 0
	});
	
	$("#registYHPUI_left_limit").numberbox({
		value : 0,
		width : 110,
		editable : true,
		required:true,
		prompt: "必填项",
		precision: 0
	});
	
	$("#registYHPUI_cfdd").searchbox({
		width : 320,
		editable : false,
		required: true,
		prompt: "请点击右侧图标",
		searcher: function(value,name){
			registYHP.query_yhpcfdd();
		},
	});

</script>
  </body>
</html>
