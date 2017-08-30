<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查找易耗品类型页面</title>
  </head>
  
  <body>
	<div class="easyui-layout" style="width:497px;height:262px;">
                <div data-options="region:'north'" align="center" style="height:35px;" >
					<input id="yhplx_mc" /><a id="yhplx_search" style="margin-left: 10px;" href="#">查找</a>
				</div>
                <div data-options="region:'south'" style="height:35px;">
                    <table>
                        <tr>
                            <td width='106px'></td>
                            <td width='100px' align="center"><a id="yhplxTree_confirm" href="javascript:void(0);">选择</a></td>
                            <td width='60px'></td>
                            <td width='100px' align="center"><a id="yhplxTree_cancel" href="javascript:void(0);">取消</a></td>
                            <td></td>
                        </tr>
                    </table>
                </div>
                <div data-options="region:'center',iconCls:'icon-more',collapsible:false" style="width:100%;">
                    <ul  id="yhplxTree"></ul>
                </div>
            </div>
	<script type="text/javascript">
    	 
    	 var dialogId=dialogObj.attr("id");
    	 var param={};       	  	
    	 $("#yhplx_search").linkbutton({
    	 	iconCls: 'icon-search'
    	 });
    	 
    	 $('#yhplx_mc').textbox({    
			prompt: "请输入易耗品类型名，支持模糊查找",
			width: 210
		 });
    	 
    	 
    	 $('#yhplx_search').bind('click', function(){    
        	if($("#yhplx_mc").textbox("getText")!=null){
    	 		param={"yhplx_mc":$("#yhplx_mc").textbox("getText")};
	    	 }else{
	    	 	param={};
	    	 }   
	    	 //重新加载用户树
	    	 $('#yhplxTree').tree({
	             url: getContextPath() + "/console/catalog/yhplxgl/getYhplxTree.do",
	             lines: true,
	             animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
	             queryParams: param,
         	  });
    	  });    
    	 
    	 //加载资产类型树
         $('#yhplxTree').tree({
             url: getContextPath() + "/console/catalog/yhplxgl/getYhplxTree.do",
             lines: true,
             animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
         });
         
         $("#yhplxTree_confirm").linkbutton({
    	 	iconCls: 'icon-ok'
    	 });
    	 
    	 $("#yhplxTree_confirm").bind('click',function(){
    	 	var node=$("#yhplxTree").tree("getSelected");
    	 	if(node==null){
    	 		$.messager.alert('提示','请选择一个易耗品类型！','info');
    	 	}else if(node.text=="全部"){
    	 		if(${isEdit}==false){
    	 			cyhplx={lxId: null,mc: "全部"};
                	$("#"+dialogId).dialog("close");
                }else{
                	$.messager.alert('提示','请选择一个易耗品类型！','info');
                }   
    	 	}else{
    	 		$.messager.progress();
    	 		$.ajax({
                        url: getContextPath() + "/console/catalog/yhplxgl/queryYhplx.do",
                        cache: false,
                        async: true,
                        data: {
                            "id": node.id
                        },
                        type: "post",
                        dataType: "json",
                        success: function(result) {
                            $.messager.progress('close');
                          	cyhplx=result;
                          	$("#"+dialogId).dialog("close");
                        },
                        error: function() {
                            $.messager.progress('close');
                            $.messager.alert('失败', '查询易耗品类型信息失败');
                        }
                  });
    	 	}
    	 });
    	 
    	 $("#yhplxTree_cancel").linkbutton({
    	 	iconCls: 'icon-cancel'
    	 });
    	 
    	 $("#yhplxTree_cancel").bind('click',function(){
    	 	$("#"+dialogId).dialog("close");
    	 });
    </script>
  </body>
</html>
