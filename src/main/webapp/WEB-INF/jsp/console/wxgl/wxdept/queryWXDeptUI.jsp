<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查找微信部门页面</title>
  </head>
  
  <body>
	<div class="easyui-layout" style="width:497px;height:262px;">
                <div data-options="region:'south'" style="height:35px;">
                    <table>
                        <tr>
                            <td width='106px'></td>
                            <td width='100px' align="center"><a id="wxdept_confirm" href="javascript:void(0);">选择</a></td>
                            <td width='60px'></td>
                            <td width='100px' align="center"><a id="wxdept_cancel" href="javascript:void(0);">取消</a></td>
                            <td></td>
                        </tr>
                    </table>
                </div>
                <div data-options="region:'center',iconCls:'icon-more',collapsible:false" style="width:100%;">
                    <ul  id="wxdeptTree"></ul>
                </div>
            </div>
	<script type="text/javascript">
    	 
    	 var dialogId=dialogObj.attr("id");
    	 
    	 //加载用户树
         $('#wxdeptTree').tree({
             url: getContextPath() + "/console/wxgl/wxdept/getWXDeptTree.do",
             lines: true,
             animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
         });
         
         $("#wxdept_confirm").linkbutton({
    	 	iconCls: 'icon-ok'
    	 });
    	 
    	 $("#wxdept_confirm").bind('click',function(){
    	 	var node=$("#wxdeptTree").tree("getSelected");
    	 	if(node==null){
    	 		$.messager.alert('提示','请选择一个部门！','info');
    	 	}else if(node.text=="全部"){
    	 		if(${isEdit}==false){
    	 			wxdept={deptNo: null,deptName: "全部"};
                	$("#"+dialogId).dialog("close");
                }else{
                	$.messager.alert('提示','请选择一个部门！','info');
                }   
    	 	}else{
    	 		$.messager.progress();
    	 		$.ajax({
                        url: getContextPath() + "/console/wxgl/wxdept/queryWXDept.do",
                        cache: false,
                        async: true,
                        data: {
                            "id": node.id
                        },
                        type: "post",
                        dataType: "json",
                        success: function(result) {
                            $.messager.progress('close');
                          	wxdept=result;
                          	$("#"+dialogId).dialog("close");
                        },
                        error: function() {
                            $.messager.progress('close');
                            $.messager.alert('失败', '查询部门信息失败');
                        }
                  });
    	 	}
    	 });
    	 
    	 $("#wxdept_cancel").linkbutton({
    	 	iconCls: 'icon-cancel'
    	 });
    	 
    	 $("#wxdept_cancel").bind('click',function(){
    	 	$("#"+dialogId).dialog("close");
    	 });
    </script>
  </body>
</html>
