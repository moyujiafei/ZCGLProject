<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>低值易耗品编辑</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  
  
  <body>
  <div id="editYHPDialog">
   <form id="editYhpform" method="post" enctype="multipart/form-data">
		<table align="center" cellspacing="5px" style="margin-top:20px">
		  <tr><td style="text-align: right;">易耗品类型：</td><td><input id="updateYHPUI_yhplx" name="lx"/></td></tr>
		  <tr><td style="text-align: right;">易耗品照片：</td><td><input id="updateYHPUI_yhpzp" name="file_upload"/></td></tr>
		  <tr><td style="text-align: right;">规格型号：</td><td><input id="updateYHPUI_xh" name="xh"/></td></tr>
		  <tr><td style="text-align: right;">出厂编号：</td><td><input id="updateYHPUI_ccbh" name="ccbh"/></td></tr>
		  <tr><td style="text-align: right;">持有数量：</td><td><table><tr><td><input id="updateYHPUI_num" name="newzcgl"/><td style="width:23px"></td><td style="text-align: right;">库存下限：</td><td><input id="updateYHPUI_left_limit" name="leftLimit"/><span style="color:#FF0000;margin-left: 3px;">*</span></td><tr></table></td></tr>
		  <tr><td style="text-align: right;">存放地点：</td><td><input id="updateYHPUI_cfdd"/><span style="color:#FF0000;margin-left: 3px;">*</span></td></tr>
		</table>
			<input id="updateYHPUI_cfdd_id" name="cfdd" type="hidden"/>		
	</form>
	  <table>
	        <tr>
	            <td width="140px"></td>
	            <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="confirm_editYHP">确定</a></td>
	            <td width="100px" style="text-align: center"><a href="javascript:void(0);" id="cancel_editYHP">取消</a></td>
	        </tr>
	  </table>
	</div>
<script type="text/javascript">
	var updateYHP = {
			query_zccfdd : function () {
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
							 $('#updateYHPUI_cfdd').searchbox("setText",vfj.xqmc + vfj.jzw + vfj.floor+vfj.room);
							 cfdd = vfj.fjId;
							 $("#updateYHPUI_cfdd_id").val(cfdd);
						 }
						 queryCFDDUI.remove();
		             }
				});
			},
	};
	
	$("#confirm_editYHP").linkbutton({
		iconCls:'icon-ok',
		plain:true,
		
	});
	
	$("#cancel_editYHP").linkbutton({
		iconCls:'icon-no',
		plain:true,
	});
	$("#confirm_editYHP").bind('click',function(){
		var limit=$("#updateYHPUI_left_limit").numberbox('getValue');
		var num=$("#updateYHPUI_num").val();
		/* var xh=$("#updateYHPUI_xh").val();
		var ccbh=$("#updateYHPUI_ccbh").val(); */
			$.messager.progress();
			$("#editYhpform").form('submit',{
				url: getContextPath() + "/console/yhpgl/yhpdj/updateYHP.do",
				onSubmit: function(param){
					var valid = $("#editYhpform").form("validate");
					if (!valid) {
						$.messager.progress('close');
						$.messager.alert("提示","请按照要求填写表单！","info");
					}
					if(valid && !isNaN(limit)&&limit<=0){
						$.messager.progress('close');
						valid=false;
						$.messager.alert("提示","库存下限要为大于0的数字！","info");
					}
					if(valid && limit>=num){
						$.messager.progress('close');
						valid=false;
						$.messager.alert("提示","库存下限【"+limit+"】大于持有数量【"+num+"】！","info");
					}
					/* if(valid && ($.trim(xh)=='' || $.trim(ccbh)=='' )){
						$.messager.progress('close');
						valid=false;
						$.messager.alert("提示","规格型号和出厂编号不能为空！","info");
					} */
					param.yhpid=editYHP.yhpId;
					return valid;
			    },    
			    success:function(result){    
			    	$.messager.progress('close');
					if (result == "success") {
						$("#editYHPDialog").dialog("close");
						$("#registYHPList").datagrid("reload");	
					} else {
						$.messager.alert("提示",result,"info");
					}   
			    } 
				
			});
	});
	
	$("#cancel_editYHP").bind('click',function(){
		$("#editYHPDialog").dialog('close');
	});
	
	$("#updateYHPUI_yhpzp").filebox({
			prompt: "单个文件小于1M，格式为jpg",
		 	width:320,
	 	    accept:'image/jpeg',
			buttonText:'选择图片',
			buttonAlign:'right'
		});
	
	$("#updateYHPUI_yhplx").textbox({
		value : editYHP.lx,
		width : 320,
		iconCls:'icon-lock', 
	    iconAlign:'right' ,
		disabled:true,
	});
	
	$("#updateYHPUI_xh").textbox({
		value : editYHP.xh,
		width : 320,
		editable : true,
	});
	$("#updateYHPUI_ccbh").textbox({
		value : editYHP.ccbh,
		width : 320,
		editable : true,
	});
	
	
	$("#updateYHPUI_num").textbox({
		value : editYHP.num,
		width : 110,
		iconCls:'icon-lock', 
	    iconAlign:'right' ,
		disabled:true,
	});
	
	$("#updateYHPUI_left_limit").numberbox({
		value : editYHP.leftLimit,
		width : 110,
		precision : 0,
		editable : true,
		prompt: "必填项",
		required : true,
	});
	
	$("#updateYHPUI_cfdd").searchbox({
		value : editYHP.cfdd,
		width : 320,
		editable : true,
		prompt: "请点击右侧图标",
		required : true,
		searcher: function(value,name){
			updateYHP.query_zccfdd();
		},
	});

	$(function(){
		$.messager.progress();
		$.ajax({
		 	url: getContextPath() + "/console/zcgl/regist/getCFDDTextbox.do",
	        cache: false,
	        async: true,
	        data : {
	       	 "fjid" : editYHP.cfdd,
	        },
	        type: "post",
            dataType: "json",// 返回类型必须为json
            success: function(result) {
                $.messager.progress('close');
                if (result.xqmc != null) {
                 	$("#updateYHPUI_cfdd").searchbox({
                   		required: true,
                   		editable : false,
                   		searcher: function(value,name){
                   			updateYHP.query_zccfdd();
                   		},
                   		value : result.xqmc+result.jzw+result.floor+result.room,
                   	});
                   	cfdd=result.fjId;
                    $("#updateYHPUI_cfdd_id").val(cfdd);
                }else {
                	$("#updateYHPUI_cfdd").searchbox({
                   		required: true,
                   		editable : false,
                   		searcher: function(value,name){
                   			updateYHP.query_zccfdd();
                   		},
                   	});
                }
            },        
            error: function() {
                $.messager.progress('close');
            }
		});
	});
</script>
  </body>
</html>
