<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>部门易耗品管理页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  
  <body>
   <div id="registBMYHPUIDialog">
  	<table id="registBMYHPToolBar" width="100%">
<<<<<<< HEAD
  		<tr><td><a id="addBMYHP" href="#" onclick="registBMYHPOpt.add()">登记</a></td>
=======
  		<tr><td><a id="addBMYHP" href="#" onclick="registBMYHPOpt.registYHP()">登记</a></td>
>>>>>>> upstream/master
  			<td align="right">
  			<table>
  			<td>易耗品类型：</td><td><input id="BMYHP_lx" ></td>
  			<td width="5%"/>
  			<td><a id="queryBMYHPlx" href="#">筛选</a></td>
  			</table>
  			</td>
  		</tr>
  	</table>
<<<<<<< HEAD
	<table id="BMYHPList" align="center"></table>
	</div>
   <script type="text/javascript">
=======
	<table id="registYHPList" align="center"></table>
	</div>
   <script type="text/javascript">
   
>>>>>>> upstream/master
	$("#registBMYHPUIDialog").dialog({
		title: '部门易耗品管理列表',    
	    width: 1024,    
	    height: 600,    
	    closed: false,    
	    cache: false,    
	    modal: true,
	    inline: true   
	});
	
	$("#addBMYHP").linkbutton({
		iconCls:'icon-add',
		plain:true,
		
	});   
	
	$("#queryBMYHPlx").linkbutton({
		iconCls:'icon-search',
		plain:true,
		onClick: function (){
			doSearch();
		}
	});  
	
	 
	   function doSearch(){
<<<<<<< HEAD
			var lx=$("#BMYHP_lx").searchbox('getValue');
			var fzr=null;
			var param ={"fzr":fzr,"lx":lx};
			$("#BMYHPList").datagrid("reload",param);
=======
			var lxId=$("#BMYHP_lx").attr("lx_id");
			var fzr=null;
			var param ={"fzr":fzr,"lxId":lxId};
			$("#registYHPList").datagrid("reload",param);
>>>>>>> upstream/master
		};
	
   var registBMYHPOpt = {
		   queryYHPLX: function(dialogId,url,title,param,searchboxId){
       		dialogObj = $('<div id="' + dialogId + '"></div>');
				dialogObj.appendTo("body");
				cyhplx=null;
				$("#" + dialogId).dialog({
                   href: getContextPath() + url,
                   title: title,
                   queryParams: param,
                   width: 512,
                   height: 300,
                   cache: false,
                   modal: true,
                   draggable: true,
                   inline: true,
                   onClose: function() {
                       if(cyhplx!=null){
                       	$("#"+searchboxId).searchbox("setValue",cyhplx.mc);
<<<<<<< HEAD
                       	$("#"+searchboxId).attr("lx_id",cyhplx.id);
=======
                       	$("#"+searchboxId).attr("lx_id",cyhplx.lxId);
>>>>>>> upstream/master
                       }
                       dialogObj.remove();// 关闭时remove对话框
                   }
               });
       	},
       	
    	newDialog: function(dialogId,url,title,param){
			// 弹出新的对话框
			var dialogObj = $('<div id="' + dialogId + '"></div>');
			dialogObj.appendTo("body");
			$("#" + dialogId).dialog({
                href: getContextPath() + url,
                title: title,
<<<<<<< HEAD
                top: 220,
=======
>>>>>>> upstream/master
                queryParams: param,
                width: 512,
                height: 300,
                shadow : true, //显示阴影
        		resizable : false, //不允许改变大小
                modal: true,
                draggable: true,
                inline: true,
                onClose: function() {
                    dialogObj.remove();// 关闭时remove对话框
                }
            });
		},
<<<<<<< HEAD
		
		edit : function(index){
			editYHP = $("#BMYHPList").datagrid('getData').rows[index];
=======
		registYHP: function () {
>>>>>>> upstream/master
			$.ajax({
			    url: getContextPath() + "/console/yhpgl/yhpdj/isAdmin.do",
			    cache: false,
			    async: true,
			    type: "post",
			    dataType: "text",
			    success: function(data) {
			    	console.log(data);
			    	if(data == 'success'){
<<<<<<< HEAD
			    		registBMYHPOpt.newDialog("editYHPDialog", "/console/yhpgl/yhpdj/updateYHPUI.do", "编辑低值易耗品",{});
			    	}else{
			    		$.messager.alert('失败', '您不是管理员，无法完成编辑操作');
=======
						registBMYHPOpt.newDialog("registYHPDialog", "/console/yhpgl/yhpdj/insertBMYHPUI.do", "低值易耗品登记",{});
			    	}else{
			    		$.messager.alert('失败', '您不是管理员，无法完成登记操作','info');
			    	}
			    },
			    error: function() {
			        $.messager.alert('失败', '页面加载失败');
			    }
			});
		},
		edit : function(index){
			editYHP = $("#registYHPList").datagrid('getData').rows[index];
			$.ajax({
			    url: getContextPath() + "/console/yhpgl/yhpdj/isAdmin.do",
			    cache: false,
			    async: true,
			    type: "post",
			    dataType: "text",
			    success: function(data) {
			    	if(data == 'success'){
			    		registBMYHPOpt.newDialog("editYHPDialog", "/console/yhpgl/yhpdj/updateYHPUI.do", "编辑低值易耗品",{});
			    	}else{
			    		$.messager.alert('失败', '您不是管理员，无法完成编辑操作','info');
>>>>>>> upstream/master
			    	}
			    },
			    error: function() {
			        $.messager.alert('失败', '页面加载失败');
			    }
			});
		},
		
		bh : function(index){
<<<<<<< HEAD
			bhYHP = $("#BMYHPList").datagrid('getData').rows[index];
=======
			bhYHP = $("#registYHPList").datagrid('getData').rows[index];
>>>>>>> upstream/master
			$.ajax({
			    url: getContextPath() + "/console/yhpgl/yhpdj/isAdmin.do",
			    cache: false,
			    async: true,
			    type: "post",
			    dataType: "text",
			    success: function(data) {
<<<<<<< HEAD
			    	console.log(data);
			    	if(data == 'success'){
			    		registBMYHPOpt.newDialog("bhYHPDialog", "/console/yhpgl/yhpdj/addYHPUI.do", "补货低值易耗品",{});
			    	}else{
			    		$.messager.alert('失败', '您不是管理员，无法完成编辑操作');
=======
			    	if(data == 'success'){
			    		registBMYHPOpt.newDialog("bhYHPDialog", "/console/yhpgl/yhpdj/addYHPUI.do", "补货低值易耗品",{});
			    	}else{
			    		$.messager.alert('失败', '您不是管理员，无法完成补货操作','info');
>>>>>>> upstream/master
			    	}
			    },
			    error: function() {
			        $.messager.alert('失败', '页面加载失败');
			    }
			});
			
		}
       	
   };
   
   $("#BMYHP_lx").searchbox({
		width: 115,
		searcher: function(value,name){
			registBMYHPOpt.queryYHPLX("queryYHPLXDialog", "/console/catalog/yhplxgl/queryYhplxUI.do", "查找易耗品类型", {},"BMYHP_lx");
		},
		editable: false,
		prompt: "请点击右侧图标"
	});
   
   
<<<<<<< HEAD
   $("#BMYHPList").datagrid({
	    url: getContextPath()+"/console/yhpgl/yhpdj/getYHPList.do",
=======
   $("#registYHPList").datagrid({
	    url: getContextPath()+"/console/yhpgl/yhpdj/getBMYHPList.do",
>>>>>>> upstream/master
		onLoadSuccess: function(data){
			
			$(".editBtn").linkbutton({
				iconCls: 'icon-edit',
				plain: true,
				height: 21
				
			});
			
			$(".bhBtn").linkbutton({
				iconCls: 'icon-buhuo',
				plain: true,
				height: 21
			});
<<<<<<< HEAD
			$(".allocateBtn").linkbutton({
				iconCls: 'icon-allocate',
				plain: true,
				height: 21
			});
=======
>>>>>>> upstream/master
			
			$(".bhBtn").tooltip({
					content : '补货',
					position : 'top',
					trackMouse: true,
				});
				
				$(".editBtn").tooltip({
					content : '编辑',
					position : 'top',
					trackMouse: true
				});
<<<<<<< HEAD
				$(".allocateBtn").tooltip({
					content : '调拨',
					position : 'top',
					trackMouse: true,
				});
=======
>>>>>>> upstream/master
				
				
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
	
		columns: [[
					{
						field:'lx',
						title:'类型',
						align:'center',
						width:'15%',
						formatter:function(value,row,index){ 
							var content=null;
							if(row.lx!=null){
		                     content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>"; 
							}
		                     return content;  
		                }
					},{
						field:'xh',
						title:'规格型号',
						align:'center',
						width:'15%',
						formatter:function(value,row,index){ 
							var content=null;
							if(row.xh!=null){
		                     content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>"; 
							}
		                     return content;  
		                }
					},{
						field:'ccbh',
						title:'出产编号',
						align:'center',
						width:'15%',
						formatter:function(value,row,index){ 
							var content=null;
							if(row.ccbh!=null){
		                     content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>"; 
							}
		                     return content;  
		                }
					},{
						field:'num',
						title:'持有数量',
						align:'center',
						width:'10%',
						formatter:function(value,row,index){ 
							var content=null;
							if(row.num!=null){
		                     content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>"; 
							}
		                     return content;  
		                }
					},{
<<<<<<< HEAD
						field:'cfdd',
						title:'存放地点',
=======
						field:'deptName',
						title:'操作部门',
>>>>>>> upstream/master
						align:'center',
						width:'21.9%',
						formatter:function(value,row,index){ 
							var content=null;
<<<<<<< HEAD
							if(row.cfdd!=null){
=======
							if(row.deptName!=null){
>>>>>>> upstream/master
		                     content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>"; 
							}
		                     return content;  
		                }
					},{
						field:'leftLimit',
						title:'库存下限',
						align:'center',
						width:'10%',
						formatter:function(value,row,index){ 
							var content=null;
							if(row.leftLimit!=null){
		                     content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>"; 
							}
		                     return content;  
		                }
					},{
						field:'opt',
						title:'操作',
<<<<<<< HEAD
						align:'center',
						width:'15%',
						formatter:function(value,row,index){
							return '&nbsp;&nbsp;<a href="#" class="editBtn" onclick="registBMYHPOpt.edit('+index+')"></a>&nbsp;&nbsp;'+
							'<a href="#" class="bhBtn" onclick="registBMYHPOpt.bh('+index+')"></a>&nbsp;&nbsp;'+
							'<a href="#" class="allocateBtn" onclick=""></a>';
=======
						align:'left',
						width:'15%',
						formatter:function(value,row,index){
							return '&nbsp;&nbsp;<a href="#" class="editBtn" onclick="registBMYHPOpt.edit('+index+')"></a>&nbsp;&nbsp;'+
							'<a href="#" class="bhBtn" onclick="registBMYHPOpt.bh('+index+')"></a>&nbsp;&nbsp;'
>>>>>>> upstream/master
						}
					},
				]],
	   
	   
	    fit:true,
	    toolbar: "#registBMYHPToolBar",
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
		emptyMsg : "没有数据",
		errorMsg :"加载失败", 
   });
   
   </script>
   
  </body>
</html>
