<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查找资产类型页面</title>
  </head>
  
  <body>
	<div class="easyui-layout" style="width:497px;height:262px;">
                <div data-options="region:'north'" align="center" style="height:35px;" >
					<input id="zclx_mc" /><a id="zclx_search" style="margin-left: 10px;" href="#">查找</a>
				</div>
                <div data-options="region:'south'" style="height:35px;">
                    <table>
                        <tr>
                            <td width='106px'></td>
                            <td width='100px' align="center"><a id="zclxTree_confirm" href="javascript:void(0);">选择</a></td>
                            <td width='60px'></td>
                            <td width='100px' align="center"><a id="zclxTree_cancel" href="javascript:void(0);">取消</a></td>
                            <td></td>
                        </tr>
                    </table>
                </div>
                <div data-options="region:'center',iconCls:'icon-more',collapsible:false" style="width:100%;">
                    <ul  id="zclxTree"></ul>
                </div>
            </div>
	<script type="text/javascript">
    	 
    	 var dialogId=dialogObj.attr("id");
    	 var param={};       	  	
    	 $("#zclx_search").linkbutton({
    	 	iconCls: 'icon-search'
    	 });
    	 
    	 $('#zclx_mc').textbox({    
			prompt: "请输入资产类型名，支持模糊查找"
		 });
    	 
    	 
    	 $('#zclx_search').bind('click', function(){    
        	if($("#zclx_mc").textbox("getText")!=null){
    	 		param={"zclx_mc":$("#zclx_mc").textbox("getText")};
	    	 }else{
	    	 	param={};
	    	 }   
	    	 //重新加载用户树
	    	 $('#zclxTree').tree({
	             url: getContextPath() + "/console/catalog/zclxgl/getZCLXTree.do",
	             lines: true,
	             animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
	             queryParams: param,
         	  });
    	  });    
    	 
    	 //加载资产类型树
         $('#zclxTree').tree({
             url: getContextPath() + "/console/catalog/zclxgl/getZCLXTree.do",
             lines: true,
             animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
         });
         
         $("#zclxTree_confirm").linkbutton({
    	 	iconCls: 'icon-ok'
    	 });
    	 
    	 $("#zclxTree_confirm").bind('click',function(){
    	 	var node=$("#zclxTree").tree("getSelected");
    	 	if(node==null){
    	 		$.messager.alert('提示','请选择一个资产类型！','info');
    	 	}else if(node.text=="全部"){
    	 		if(${isEdit}==false){
    	 			czclx={lxId: null,mc: "全部"};
                	$("#"+dialogId).dialog("close");
                }else{
                	$.messager.alert('提示','请选择一个资产类型！','info');
                }   
    	 	}else{
    	 		$.messager.progress();
    	 		$.ajax({
                        url: getContextPath() + "/console/catalog/zclxgl/queryZCLX.do",
                        cache: false,
                        async: true,
                        data: {
                            "id": node.id
                        },
                        type: "post",
                        dataType: "json",
                        success: function(result) {
                            $.messager.progress('close');
                          	czclx=result;
                          	$("#"+dialogId).dialog("close");
                        },
                        error: function() {
                            $.messager.progress('close');
                            $.messager.alert('失败', '查询资产类型信息失败');
                        }
                  });
    	 	}
    	 });
    	 
    	 $("#zclxTree_cancel").linkbutton({
    	 	iconCls: 'icon-cancel'
    	 });
    	 
    	 $("#zclxTree_cancel").bind('click',function(){
    	 	$("#"+dialogId).dialog("close");
    	 });
    </script>
  </body>
</html>
