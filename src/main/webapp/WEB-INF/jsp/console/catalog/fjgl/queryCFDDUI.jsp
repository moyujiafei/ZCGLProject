<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>查找存放地点</title>
<style type="text/css">
	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
</style>
</head>
<body>
	<div id="queryCFDDUI" class="easyui-layout" style="width:497px;height:248px;">
		<div data-options="region:'north'" align="center" style="height:35px;" >
			<table align="center" cellspacing="1px" style="padding-top: 2px;">
				<tr>
					<td style="text-align:right;">校        区：</td><td><input id="searchXQMCCombo" name="xqid" style="width:125px"/></td>
					<td style="text-align:right;">建  筑  物：</td><td><input id="searchJZWCombo" name="jzwid" style="width:125px"/></td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',iconCls:'icon-more',collapsible:false" style="width:100%;">
            <ul  id="cfddTree" style="padding-left:50px;"></ul>
        </div>
       <div data-options="region:'south'" style="height:35px;">
            <table>
                <tr>
                    <td width='106px'></td>
                    <td width='100px' align="center"><a id="queryCFDDconfirm" href="#">确认</a></td>
                    <td width='60px'></td>
                    <td width='100px' align="center"><a id="queryCFDDcancel" href="#">取消</a></td>
                    <td></td>
                </tr>
            </table>
       </div>
	</div>
	<script type="text/javascript">
	var param={}; 
	$("#searchXQMCCombo").combobox({
		 required:true,  
		    url: getContextPath()+"/console/catalog/fjgl/getXQCombo.do",
		    valueField: 'id',
			textField: 'text',
			editable: false,
			panelHeight: 80,
		 	onSelect: function(rec){    
		 		var url = getContextPath()+"/console/catalog/fjgl/getJZWCombo.do?xqid="+rec.id;    
		        $('#searchJZWCombo').combobox('reload',url);  
		    },
			onLoadSuccess : function(){
				var data =$(this).combobox("getData");
				$(this).combobox("select",data[0].id);
			},
	});
	
	$("#searchJZWCombo").combobox({
		required:true,  
 	    valueField: 'id',
 		textField: 'text',
 		editable: false,
 		panelHeight: 80,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		},
		
		onSelect : function (record) {
     		var xqid = $("#searchXQMCCombo").textbox("getValue");
     		$("#cfddTree").tree({
     			 url: getContextPath() + "/console/catalog/fjgl/queryJZW.do?jzwid="+record.id+"&xqid="+xqid,
     	         lines: true,
     	         animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
     	         cascadeCheck :false,
     		});
		}
	});
	
	$("#cfddTree").tree({
		 url: getContextPath() + "/console/catalog/fjgl/queryJZW.do",
         lines: true,
         animate: true,	//定义节点在展开或折叠的时候是否显示动画效果。
         cascadeCheck :false,
	});
	
	 $("#queryCFDDconfirm").linkbutton({
		 	iconCls: 'icon-save'
	 });
	 
	$("#queryCFDDconfirm").bind('click',function(){
	 	var node=$("#cfddTree").tree("getSelected");
	 	if(node==null){
	 		$.messager.alert('提示','请选择一个房间号！','info');
	 	}else if(node.children != null){
	 		$.messager.alert('提示','您选择的是楼层号，请选择一个房间号！','info');
	 	}else{
	 		$.messager.progress();
	 		$.ajax({
                    url: getContextPath() + "/console/catalog/fjgl/queryCFDD.do",
                    cache: false,
                    async: true,
                    data: {
                        "fjid": node.id,
                    },
                    type: "post",
                    dataType: "json",
                    success: function(result) {
						$.messager.progress('close');
						vfj=result;
						$("#queryCFDDUI").dialog("close");    
                    },
                    error: function() {
                        $.messager.progress('close');
                        $.messager.alert('失败', '查询存放地点信息失败');
                    }
              });
	 	}    	
	 });
	 
	 $("#queryCFDDcancel").linkbutton({
	 	iconCls: 'icon-cancel'
	 });
	 
	 $("#queryCFDDcancel").bind('click',function(){
	 	$("#queryCFDDUI").dialog("close");    	 	
	 });
	</script>
</body>
</html>