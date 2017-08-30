<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>建筑物列表</title>
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
	<div id="jzwListMain">  
	 <table id ="tbJzwDateGrid"></table>
	  <div id="divJzwToolbar" style="padding:3px;">
	  	<form id="downLoadForm" method="post">
			<input type="hidden" name="randomInfo" value="<%=Math.random()%>"/>
		</form>
		<table>
			<tr>
				<td><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add"  onclick="objJzw.add()">新增</a></td>
				<td style="padding-left:20px"><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-import" onclick="objJzw.jzwExcelImport()">导入</a></td>
				<td style="padding-left:20px"><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-export"  onclick="objJzw.jzwExcelexport('导出excel', $('#tbJzwDateGrid'))">导出</a></td>
				<td style="padding-left:120px;padding-right:10px">校区 : </td>
				<td><input id="selectXq"  name="xqmc"  style="width:125px"></td>
				<td style="padding-left:20px;padding-right:10px">建筑类型 : </td>
				<td><input id="selectJzlx"  name="lxmc"  style="width:125px"></td>
				<td style="padding-left:20px;padding-right:10px">建筑物 : </td>
				<td><input id="inputJzw"  name="mc"  style="width:125px"/></td>
				<td style="padding-left:10px"><a href="#"  id="searchJZWBtn">查询</a></td>
			</tr>
	    </table>
	 </div>
	</div>
<script type="text/javascript">
	var objJzw =  {
			add : function(){
				var addJzw = $("<div id='insertJZWUI'></div>");
				addJzw.appendTo("body");
				$("#insertJZWUI").dialog({
					title: '新建建筑物', 	
				    width: 512,    
				    height: 300,    
				    closed: false,
				    modal: true,
				    inline : true,
				    onClose:function(){
				    	addJzw.remove();
				    }
				});
				$("#insertJZWUI").dialog('open').dialog('refresh',  getContextPath()+"/console/catalog/jzwgl/insertJZWUI.do");			
			},
			edit : function(){
				var editJzw = $("<div id = 'updateJZWUI'><div>");
				editJzw.appendTo("body");
				$("#updateJZWUI").dialog({
					title : '编辑建筑物',
					width : 512,
					height : 300,
					close : false,
					modal : true,
					inline : true,
					onClose:function(){
						editJzw.remove();
					}
				});
				$("#updateJZWUI").dialog('open').dialog('refresh',getContextPath()+"/console/catalog/jzwgl/updateJZWUI.do");
			},
			dele : function(id,index){
				$.messager.confirm('确认','您确认想要删除此记录吗？',function(sure){    
				    if (sure){    
				    	$.post(getContextPath() + "/console/catalog/jzwgl/delJZW.do?action=delete&id=" + id + "&").success(function() {  
                            $('#tbJzwDateGrid').datagrid('reload');  
                            $('#tbJzwDateGrid').datagrid('clearSelections');
                        	}  
                    	).error(function(){
                    		$.messager.show({title:'操作提示',msg:'删除失败！',showType:'show'});
                    	});
				   }
				}); 
			},
			jzwExcelImport : function(uid,index){
				var jzwExcelImport = $("<div id = 'importJZWUI'><div>");
				jzwExcelImport.appendTo("body");
				$("#importJZWUI").dialog({
					title : '建筑物批量导入',
					width : 512,
					height : 200,
					close : false,
					modal : true,
					inline : true,
					onClose:function(){
						jzwExcelImport.remove();
					}
				});
				$("#importJZWUI").dialog('open').dialog('refresh',getContextPath()+"/console/catalog/jzwgl/importJZWExcelUI.do");
			},
			
			jzwExcelexport : function(response, fileName){
				$.messager.confirm('导出', '确定要导出建筑物表格吗？', function(r){
					if (r){
						downloadJZWExcel();
					}
				});
			},
	};
	
  	$("#jzwListMain").dialog({
  		title : '建筑物管理',
  		height:600,
  		width:1024,
  		closed : false,
		cache : false,
		shadow : true, //显示阴影
		resizable : false, //不允许改变大小
		modal : true, //是否窗口化
		inline : true, //是否在父容器中，若不在会出现很多BUG
  	});	
  	$("#selectXq").combobox({    
        required:true,  
     	url: getContextPath()+"/console/catalog/jzwgl/getXQComboWithAll.do",
        valueField: 'id',
		textField: 'text',
		editable: false,
		panelHeight: 80,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		},
    });
  	$("#selectJzlx").combobox({    
        required:true,  
        url: getContextPath()+"/console/catalog/jzwgl/getJZLXComboWithAll.do",
        valueField: 'id',
		textField: 'text',
		editable: false,
		panelHeight: 80,
		onLoadSuccess : function(){
			var data =$(this).combobox("getData");
			$(this).combobox("select",data[0].id);
		}
    });
  	

  	$("#inputJzw").searchbox({
  		prompt : '请输入建筑物名称',
  		searcher : function(){
  			doSearch();
  		},
  	});
  	$("#tbJzwDateGrid").datagrid({
  		width : "100%",
		height : "100%",
		method : "post",
		url : getContextPath() + "/console/catalog/jzwgl/jzwList.do",
		onLoadSuccess : function(data) {
			$(".editBtn").linkbutton({
				plain : true,
				height : 21,
				iconCls : 'icon-edit'
			});
			$(".deleteBtn").linkbutton({
				plain : true,
				height : 21,
				iconCls : 'icon-no'
			});
		},
		
	columns : [[
			{
				width : "20%",
				field : "xqmc",
				title : "校区",
				halign : "center",
				align : "center",
				resizable : true,
			},
			{
				width : "15%",
				field : "lxmc",
				title : "建筑物类型",
				halign : "center",
				align : "center",
				resizable : true,
			},
			{
				width : "16%",
				field : "jzw",
				title : "建筑物",
				halign : "center",
				align : "center",
				resizable : true,
			},
			{
				width : "36%",
				field : "dz",
				title : "地址",
				halign : "center",
				align : "center",
				resizable : true,
			},
			{
				width : "15%",
				field : "opt1",
				title : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
				align : "left",
                formatter: function(value, row, index) {
                	return '&nbsp;&nbsp;<a href="#"class="editBtn" title="编辑" onclick="objJzw.edit()"></a>&nbsp;&nbsp;<a href="#" class="deleteBtn"  title="删除" onclick="objJzw.dele('+row.jzwId+',' +index +')" ></a>';
                },
				resizable : false,
			}, ] ],
		pagination : true, //是否有分页工具
		pagePosition : "bottom", //分页工具位置
		pageSize : 15, //分页默认大小
		pageList : [ 15 ],
		striped : true, //斑马线样式,
		nowrap : true, //是否在一行显示所有，自动换行
		loadMsg : "努力加载中，请稍后。。。。",//加载信息
		rownumbers : true, //是否显示行号
		singleSelect : true, //只能同时选择一行
		showHeader : true,//显示行头，默认true;
		toolbar : "#divJzwToolbar",
		emptyMsg : "没有找到符合条件的记录",
	});
  	
  	function downloadJZWExcel(){
		$("#downLoadForm").form('submit', {
			url : getContextPath() + "/console/catalog/jzwgl/exportJZWExcel.do",
			success: function(data){
				if(data != '"success"'){
					$.messager.alert('失败',data,'warning');
				}
			}
		});
	};
	
	$("#searchJZWBtn").linkbutton({
		plain: true,
		iconCls: 'icon-search',
		onClick: function (){
			doSearch();
		}
	});
	
	function doSearch(){
		var xqmc = $("#selectXq").combobox("getValue");
		var lxmc = $("#selectJzlx").combobox("getValue");
		var mc = $.trim($("#inputJzw").searchbox("getValue"));
		var param ={"xqmc":xqmc,"lxmc":lxmc,"mc":mc};
		$("#tbJzwDateGrid").datagrid("reload",param);
	};
</script>
</body>
</html>