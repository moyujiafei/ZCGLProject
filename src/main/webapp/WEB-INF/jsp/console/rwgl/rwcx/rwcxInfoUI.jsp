<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'rwcxInfoUI.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
	<table id="rwcxInfoToolBar" width="100%">
			<tr>
				<td style="padding-left:20px;padding-right:10px">开始时间 : </td>
				<td><input id="readkssj"  name="kssj"></td>
				<td style="padding-left:20px;padding-right:10px">结束时间 : </td>
				<td><input id="readjssj"  name="jssj" ></td>
				<td style="padding-left:20px;padding-right:10px">操作人 : </td>
				<td><input id="readCzr"  name="czr"  ></td>
				<td style="padding-left:20px;padding-right:10px">验收人 : </td>
				<td><input id="readYsr"  name="ysr"  /></td>
				<td style="padding-left:10px;padding-right:10px">验收时间:</a></td>
				<td><input id="readYssj"  name="yssj"  /></td>
			</tr>
	 </table>
	 <table id ="tbRwXqDateGrid"></table>
  
 <script type="text/javascript">
$(function(){
	var kssj = '';
	var jssj = '';
	var yssj = '';
	$(function(){
		var date = new Date('${rw.kssj}');
	    var y = date.getFullYear();
	    var m = date.getMonth() + 1;
	    var d = date.getDate();
	    kssj =  y + '-' +m + '-' + d;
	    $("#readkssj").textbox({
	     	width: 115,
	     	height: 25,
	     	editable:false,
	     	iconCls:'icon-lock',
	        value: kssj
	     });
        
        var date = new Date('${rw.jssj}');
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        jssj =  y + '-' +m + '-' + d;
        $("#readjssj").textbox({
	     	width: 100,
	     	height: 25,
	     	editable:false,
	     	iconCls:'icon-lock',
	        value: jssj
	     });
        
        var date = new Date('${rw.yssj}');
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        yssj =  y + '-' +m + '-' + d;
        if(yssj=='NaN-NaN-NaN') yssj ='';
        $("#readYssj").textbox({
	     	width: 100,
	     	height: 25,
	     	editable:false,
	     	iconCls:'icon-lock',
	        value: yssj
	     });
	});
	 
	 
	
	 $("#readCzr").textbox({
	     	width: 100,
	     	height: 25,
	     	editable:false,
	     	iconCls:'icon-lock',
	        value: '${rw.czrmc}'
	     });
	 $("#readYsr").textbox({
	     	width: 100,
	     	height: 25,
	     	editable:false,
	     	iconCls:'icon-lock',
	        value: '${rw.ysrmc}'
	     });
	
	 
	 $("#tbRwXqDateGrid").datagrid({
	  		width : "100%",
	  		height: "100%",
			method : "post",
			url : getContextPath() + "/console/rwgl/rwcx/getRWZCList.do?rwid="+${rw.id},
			pagination : true, //是否有分页工具
			pagePosition : 'bottom', //分页工具位置
			pageSize : 15, //分页默认大小
			pageList : [ 15 ],
			toolbar:"#rwcxInfoToolBar",
			striped : true, //斑马线样式,
			nowrap : true, //是否在一行显示所有，自动换行
			loadMsg : "努力加载中，请稍后。。。。",//加载信息
			rownumbers : true, //是否显示行号
			singleSelect : true, //只能同时选择一行
			showHeader : true,//显示行头，默认true;
			emptyMsg : "没有数据",
			onLoadSuccess: function(data){
				$(".tip").tooltip({ 
           		  	trackMouse: true,
                    onShow: function(){ 
                         $(this).tooltip('tip').css({   
                             width:300, 
                             height:300,
                             boxShadow: '1px 1px 3px #292929',
                         });  
                    }  
                 });
			},
			
			columns : [[
				{
					width : "12%",
					field : "zcid",
					title : "资产编号",
					halign : "center",
					align : "center",
					resizable : true,
					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                    }
				},
				{
					width : "14%",
					field : "zc",
					title : "资产名称",
					halign : "center",
					align : "center",
					resizable : true,
					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                    }
				},
				{
					width : "16%",
					field : "zclx",
					title : "资产类型",
					halign : "center",
					align : "center",
					resizable : true,
					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                    }
				},
				{
					width : "12%",
					field : "zjnx",
					title : "折旧年限",
					halign : "center",
					align : "center",
					resizable : true,
					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                    }
				},
				{
					width : "12%",
					field : "num",
					title : "资产数量",
					halign : "center",
					align : "center",
					resizable : true,
					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                    }
				},
				{
					width : "12%",
					field : "syrmc",
					title : "使用人",
					halign : "center",
					align : "center",
					resizable : true,
					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                    }
				},
				{
					width : "12%",
					field : "deptName",
					title : "所属部门",
					halign : "center",
					align : "center",
					resizable : true,
					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                    }
				},
				{
					width : "12%",
					field : "glrmc",
					title : "部门资产管理员",
					halign : "center",
					align : "center",
					resizable : true,
					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                    }
				}] ],
		});

});
</script>
  </body>
</html>
