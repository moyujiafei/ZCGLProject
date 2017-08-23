<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>查询页面</title>
  </head>
  
  <body>
		<div class="easyui-layout" style="width:497px;height:262px;">
               	<div data-options="region:'north'" align="center" style="height:35px;" >
					<input id="wxuser_name" /><a id="search" style="margin-left: 10px;" href="#">查找</a>
				</div>
                <div data-options="region:'south'" style="height:35px;">
                    <table>
                        <tr>
                            <td width='106px'></td>
                            <td width='100px' align="center"><a id="wxuser_confirm" href="#">确认</a></td>
                            <td width='60px'></td>
                            <td width='100px' align="center"><a id="wxuser_cancel" href="#">取消</a></td>
                            <td></td>
                        </tr>
                    </table>
                </div>
                <div data-options="region:'center',iconCls:'icon-more',collapsible:false" style="width:100%;">
                    <ul  id="usertree"></ul>
                </div>
            </div>
    <script type="text/javascript">
        var dialogId=dialogObj.attr("id");
        var param={};       	  	
    	 $("#search").linkbutton({
    	 	iconCls: 'icon-search'
    	 });
    	 
    	 $('#wxuser_name').textbox({    
			prompt: "请输入用户名，支持模糊查找"
		 });
    	 
    	 
    	 $('#search').bind('click', function(){    
        	if($("#wxuser_name").textbox("getText")!=null){
    	 		param={"wxuser_name":$("#wxuser_name").textbox("getText")};
	    	 }else{
	    	 	param={};
	    	 }   
	    	 //重新加载用户树
	    	 $('#usertree').tree({
	             url: getContextPath() + "/console/wxgl/wxuser/getWXUserTree.do",
	             lines: true,
	             animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
	             queryParams: param,
         	  });
    	  });    
    	 
    	 
    	 //加载用户树
         $('#usertree').tree({
             url: getContextPath() + "/console/wxgl/wxuser/getWXUserTree.do",
             lines: true,
             animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
             queryParams: param,
         });
         
         $("#wxuser_confirm").linkbutton({
    	 	iconCls: 'icon-ok'
    	 });
    	 
    	 $("#wxuser_confirm").bind('click',function(){
    	 	var node=$("#usertree").tree("getSelected");
    	 	if(node==null){
    	 		$.messager.alert('提示','请选择一位用户！','info');
    	 	}else if(node.text=="全部" && ${isEdit}==false){
    	 		wxuser={name: "全部",userid: ""};
                $("#"+dialogId).dialog("close");   
    	 	}else if(node.children!=null){
    	 		$.messager.alert('提示','请选择一位用户','info');
    	 	}else{
    	 		$.messager.progress();
    	 		$.ajax({
                        url: getContextPath() + "/console/wxgl/wxuser/queryWXUser.do",
                        cache: false,
                        async: true,
                        data: {
                            "id": node.id
                        },
                        type: "post",
                        dataType: "json",
                        success: function(result) {
  								 $.messager.progress('close');
  								 wxuser=result;
                            	 $("#"+dialogId).dialog("close");   
                        },
                        error: function() {
                            $.messager.progress('close');
                            $.messager.alert('失败', '查询用户信息失败');
                        }
                  });
    	 	}    	
    	 });
    	 
    	 $("#wxuser_cancel").linkbutton({
    	 	iconCls: 'icon-cancel'
    	 });
    	 
    	 $("#wxuser_cancel").bind('click',function(){
    	 	$("#"+dialogId).dialog("close");    	 	
    	 });
    </script>
  </body>
</html>
