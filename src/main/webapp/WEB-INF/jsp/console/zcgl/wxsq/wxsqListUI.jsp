<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>申请维修资产列表</title>
</head>
<body>
	<div id="sqwxListMain">
		<table id="sqwxListDatagrid"></table>
		<table align="center">
			<tr>
				<td style="padding-right:150px"><a href="#" class="easyui-linkbutton" id="sqwxAgreeBtn">同意</a></td>
				<td><a href="#" class="easyui-linkbutton" id="sqwxRefuseBtn">拒绝</a></td>
			</tr>
		</table>
		<div id="sqwxToolbar" style="width: 100%;height:30px;padding-top: 10px;">
			<table align="right">
				<tr>
					<td style="padding-left:200px">资产名称：</td><td style="padding-right:10px"><input id="input_zcmc"/></td>
					<td style="padding-left:5px">资产类型：</td><td style="padding-right:10px"><input id="search_zclx"/></td>
					<td style="padding-left:5px">所属部门：</td><td style="padding-right:10px"><input id="search_dept"/></td>
					<td style="padding-left:5px"><a href="#" class="easyui-linkbutton" id="search_sqwx" plain="true">筛选</a></td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		$("#sqwxListMain").dialog({
			width : 1024,
			height : 600,
			title : '申请维修资产列表',
			closed : false,
			cache : false,
			shadow : true,
 			resizable : false,
 			modal : true,
 			inline : true,  //是否在父容器中，若不在会出现很多BUG
		});
		
		$("#input_zcmc").textbox({
			width : 165,
		});
		$("#search_zclx").searchbox({
			width : 165,
			editable : false,
			searcher: function(value,name){
				wxsqList.query_zclx();
			}
		});
		
		var wxsqList={
			query_dept: function () {
				dialogObj = $("<div id='dialogObj'></div>");
				wxdept  = null;
				dialogObj.appendTo("body");
				$("#dialogObj").dialog({
					href: getContextPath() + "/console/wxgl/wxdept/queryWXDeptUI.do",
					title: "资产管理部门查询",
					width: 512,
					height: 300,
					modal: true,
					onClose: function () {
						if(wxdept != null) {
							dialogObj.remove();
							$("#search_dept").searchbox("setValue",wxdept.deptName);
							$("#search_dept").attr("deptno",wxdept.deptNo); 
						}
						dialogObj.remove();
					}
				});
			},
			query_zclx: function () {
				dialogObj = $("<div id='dialogObj'></div>");
				czclx  = null;
				dialogObj.appendTo("body");
				$("#dialogObj").dialog({
					href: getContextPath() + "/console/catalog/zclxgl/queryZCLXUI.do",
					title: "资产类型查询",
					width: 512,
					height: 300,
					modal: true,
					onClose: function () {
						if(czclx != null) {
							dialogObj.remove();
							$("#search_zclx").searchbox("setValue",czclx.mc);
							$("#search_zclx").attr("lxid",czclx.lxId); 
						}
						dialogObj.remove();
					}
				});
			}
		};
		
		$("#search_dept").searchbox({
			width : 125,
			editable : false,
			searcher: function(value,name){
				wxsqList.query_dept();
			}
		});
		
		
		
		$("#sqwxAgreeBtn").linkbutton({
			iconCls : 'icon-ok',
			height : 21,
			onClick : function(){
				var selectedData = $("#sqwxListDatagrid").datagrid("getChecked");
				if(selectedData.length<1){
					$.messager.alert('错误','请至少选中一个申请维的资产。','info');
				}else{
					var refuseReasons = $("<div id='agreeWXSQDialog'></div>");
					refuseReasons.appendTo("body");
					$("#agreeWXSQDialog").dialog({
						width : 512,
						height : 300,
						closed : false,
						title : '新建故障维修任务',
						inline : true,
						cache : false,
						shadow : true,
			 			resizable : false,
			 			modal : true,
			 			href : getContextPath() + "/console/zcgl/wxsq/agreeWXSQUI.do",
					});
				}
			},
		});
		$("#sqwxRefuseBtn").linkbutton({
			iconCls : 'icon-no',
			height : 21,
			onClick : function(){
				var selectedData = $("#sqwxListDatagrid").datagrid("getChecked");
				if(selectedData.length<1){
					$.messager.alert('错误','请至少选中一个申请维的资产。','info');
				}else{
					var refuseReasons = $("<div id='refuseSQWXDialog'></div>");
					refuseReasons.appendTo("body");
					$("#refuseSQWXDialog").dialog({
						width : 512,
						height : 300,
						closed : false,
						title : '拒绝资产维修',
						inline : true,
						cache : false,
						shadow : true,
			 			resizable : false,
			 			modal : true,
			 			href : getContextPath() + "/console/zcgl/wxsq/refuseWXSQUI.do",
					});
				}
			},
		});
		
		$("#sqwxListDatagrid").datagrid({
			width : '100%',
			height : '95%',
			method : 'post',
			selectOnCheck: false,
			checkOnSelect: false,
			rownumbers: true,
			singleSelect: true,
			url : getContextPath() + "/console/zcgl/wxsq/wxsqList.do",
			columns : [[
			          {
	 					halign : "center",
	 					align : "center",
	 					field : "dm",
	 					title : "编号",
	 					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
	 					checkbox : true,
			          },  
			          {
	 					width : "14%",
	 					field : "zcdm",
	 					title : "资产编号",
	 					halign : "center",
	 					align : "center",
	 					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
	 					resizable : true,	
			          },
			          {
	 					width : "14%",
	 					field : "zc",
	 					title : "资产名称",
	 					halign : "center",
	 					align : "center",
	 					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
	 					resizable : true,	
				       },
			          {
	 					width : "15%",
	 					field : "zclx",
	 					title : "资产类型",
	 					halign : "center",
	 					align : "center",
	 					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
	 					resizable : true,	
				      },
			          {
	 					width : "14%",
	 					field : "zjnx",
	 					title : "折旧年限",
	 					halign : "center",
	 					align : "center",
	 					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
	 					resizable : true,	
					  },
			          {
	 					width : "14%",
	 					field : "num",
	 					title : "资产数量",
	 					halign : "center",
	 					align : "center",
	 					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           return content;  
                       },
	 					resizable : true,	
					   },
					   {
	 					width : "15%",
	 					field : "deptName",
	 					title : "所属部门",
	 					halign : "center",
	 					align : "center",
	 					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
	 					resizable : true,	
					   },
					   {
	 					width : "13.2%",
	 					field : "glrmc",
	 					title : "部门资产管理员",
	 					halign : "center",
	 					align : "center",
	 					formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
	 					resizable : true,	
					   },]], onLoadSuccess: function () {
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
			fitColumns : true,
			idField : 'zcdm',    //指明哪一个字段是标识字段。翻页后仍然被选择
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
 			toolbar : "#sqwxToolbar",
 			emptyMsg : "没有数据",
 			
		});
		$("#search_sqwx").linkbutton({
			iconCls: 'icon-search',
			onClick: function (){
				var zcmc = $("#input_zcmc").textbox("getValue");
				var zclxid = $("#search_zclx").attr("lxid");
				var deptNo = $("#search_dept").attr("deptno");
				var param = {"zcmc":zcmc,"zclxid":zclxid,"deptNo":deptNo};
				$("#sqwxListDatagrid").datagrid("reload",param);
			}
		});
	</script>
</body>
</html>