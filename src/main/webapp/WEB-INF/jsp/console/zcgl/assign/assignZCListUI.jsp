<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
	<div id="dlgassignZCList">
		<div id="tbassignZCList" style="width: 100%;height:30px;padding-top: 10px;">
			<div style="float:right;">
				<div style="float:left;line-height: 25px;">资产名称：</div><div style="float:left;padding-right: 10px;"><input id="assignZC_txbZcmc" name="zcmc"/></div>
				<div style="float:left;line-height: 25px;">资产类型：</div><div style="float:left;padding-right: 10px;"><input id="assignZC_searchbZclx" name="zclx"/></div>
				<div style="float:left;line-height: 25px;">所属部门：</div><div style="float:left;padding-right: 10px;"><input id="assignZC_searchbBM" name="zcbm"/></div>
				<div style="float:left;line-height: 25px;padding-right: 10px;"><a class="easyui-linkbutton" data-options="iconCls: 'icon-search'" href="#" onclick="assignZCList.query()" plain= "true">筛选</a></div>
			</div>
		</div>
		<div id="dgassignZCList"></div>		
	</div>
<script>

	
	assignZCList = {
			
		query: function () {
			
			$("#dgassignZCList").datagrid("load",{
				zcmc:$("#assignZC_txbZcmc").val(),
				zclx:$("#assignZC_searchbZclx").attr("lxid"),
				deptNo:$("#assignZC_searchbBM").attr("deptno")
			});
		},
		
		assignZC: function (index) {
			assignRow = $("#dgassignZCList").datagrid("getData").rows[index];
			var assignZCList_newAssignDlg = $("<div id='assignZCList_newAssignDlg'></div>");
			assignZCList_newAssignDlg.appendTo("body");
			$("#assignZCList_newAssignDlg").dialog({
				
				href: getContextPath() + "/console/zcgl/assign/assignZCUI.do",
				title: "资产分配",
				width: 512,
				height: 300,
				modal:true,
				
				onClose: function(){
					assignZCList_newAssignDlg.remove();
				}
			});
		},
			
		revertZC: function (index) {
			revertRow = $("#dgassignZCList").datagrid("getData").rows[index];
			var assignZCList_newRevertDlg = $("<div id='assignZCList_newRevertDlg'></div>");
			assignZCList_newRevertDlg.appendTo("body");
			$("#assignZCList_newRevertDlg").dialog({
				
				href: getContextPath() + "/console/zcgl/assign/revertZCUI.do",
				title: "申请归还资产",
				width: 512,
				height: 300,
				modal:true,
				
				onClose: function(){
					assignZCList_newRevertDlg.remove();
				}
			});
		},
		
		reassignZC: function (index) {
			reassignRow = $("#dgassignZCList").datagrid("getData").rows[index];
			var assignZCList_newReassignDlg = $("<div id='assignZCList_newReassignDlg'></div>");
			assignZCList_newReassignDlg.appendTo("body");
			$("#assignZCList_newReassignDlg").dialog({
				
				href: getContextPath() + "/console/zcgl/assign/reassignZCUI.do",
				title: "资产重新分配",
				width: 512,
				height: 300,
				modal:true,
				
				onClose: function(){
					assignZCList_newReassignDlg.remove();
				}
			});
		},
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
							$("#assignZC_searchbBM").searchbox("setValue",wxdept.deptName);
							$("#assignZC_searchbBM").attr("deptno",wxdept.deptNo); 
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
							$("#assignZC_searchbZclx").searchbox("setValue",czclx.mc);
							$("#assignZC_searchbZclx").attr("lxid",czclx.lxId); 
						}
						dialogObj.remove();
					}
				});
			}

	};

	$("#dlgassignZCList").dialog({
		title: "${sessionScope.loginInfo.name}"+"管理的资产分配列表",
		width: 1024,
		height: 600,
        cache: false,
        shadow: true, // 显示阴影
        resizable: false, // 不允许改变大小
        modal: true, // 模式化窗口，锁定父窗口
        inline: true, // 是否在父容器中，若不在会出现很多BUG
	});
	
	$("#assignZC_txbZcmc").textbox({});
	
	$("#assignZC_searchbZclx").searchbox({
		editable:false,
     	searcher: function(value,name){
     		assignZCList.query_zclx();
     	}
	});
	
	$("#assignZC_searchbBM").searchbox({
		editable:false,
     	searcher: function(value,name){
     		assignZCList.query_dept();
     	}
	});
	
	$("#dgassignZCList").datagrid({
		url: getContextPath() + "/console/zcgl/assign/zcList.do",
		fit: true,
        singleSelect: true,
        pagination: true,
        striped: true,
        rownumbers: true,
        toolbar: "#tbassignZCList",
        pageSize: 15,
        pageList: [15],
        emptyMsg : "没有数据", 
        columns: [
                  [{
                      field: 'zcdm',
                      title: '资产编号',
                      align: 'center',
                      width: '10.2%',
                      formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
                      resizable: false,
                  }, {
                      field: 'zc',
                      title: '资产名称',
                      align: 'center',
                      width: '15.1%',
                      formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
                      resizable: false,
                  }, {
                      field: 'zclx',
                      title: '资产类型',
                      align: 'center',
                      width: '12.8%',
                      formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
                      resizable: false,
                  }, {
                      field: 'zjnx',
                      title: '折旧年限',
                      align: 'center',
                      width: '9%',
                      formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
                      resizable: false,

                  }, {
                      field: 'num',
                      title: '资产数量',
                      align: 'center',
                      width: '10%',
                      formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
                      resizable: false,

                  }, {
                      field: 'deptName',
                      title: '所属部门',
                      align: 'center',
                      width: '10%',
                      formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
                      resizable: false,
                      

                  }, {
                      field: 'glrmc',
                      title: '部门资产管理员',
                      align: 'center',
                      width: '10%',
                      formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
                      resizable: false,

                  }, {
                      field: 'zcztmc',
                      title: '资产状态',
                      align: 'center',
                      width: '10%',
                      formatter:function(value,row){ 
                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                           if(value==null){
                        	   value="";
                        	   return  content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";
                           }
                           return content;  
                       },
                      resizable: false,


                  }, {
                      field: 'opt',
                      title: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作',
                      align: 'left',
                      width: '15%',
                      resizable: false,
                      formatter:function (value,row,index) {
                    	  if (row.zczt==0) {
	                    	  return "&nbsp;&nbsp;<a class='assignZC_assignZcBtn' onclick='assignZCList.assignZC("+index+")' href='#'></a>&nbsp;&nbsp;<a class='assignZC_revertZcBtn' onclick='assignZCList.revertZC("+index+")' href='#'></a>";
                    	  }
                    	  if (row.zczt == 10) {
                    		  return "&nbsp;&nbsp;<a class='assignZC_reassignZcBtn' onclick='assignZCList.reassignZC("+index+")' href='#'></a>";
                    	  }
                      }
                  }]
              ],

              onLoadSuccess: function () {
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
            	  
            	  $(".assignZC_assignZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-assign",
            	  });
            	  
            	  $(".assignZC_assignZcBtn").tooltip({
            		  position: 'bottom',    
            		  content: '分配资产',
            	  });
            	  
            	  $(".assignZC_revertZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-guihuan",
            	  });
            	  
            	  $(".assignZC_revertZcBtn").tooltip({
            		  position: 'bottom',    
            		  content: '申请归还',
            	  });
            	  
            	  $(".assignZC_reassignZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-reassign",
            	  });
            	  
            	  $(".assignZC_reassignZcBtn").tooltip({
            		  position: 'bottom',    
            		  content: '重新分配',
            	  });
            	  
            	  
              },
	});
	
</script>
</body>
</html>
