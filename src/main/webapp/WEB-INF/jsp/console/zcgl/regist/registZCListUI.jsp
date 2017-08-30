<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
  
<body>
	<div id="dlgregistZCList">
		<div id="tbregistZCList" style="width: 100%;height:30px;padding-top: 10px;">
			<div style="float:left;"><a class="easyui-linkbutton" href="#" data-options="iconCls: 'icon-add'" onclick="registZCList.regist()" plain="true">登记</a></div>
			<div style="float:left;padding-left: 10px;"><a class="easyui-linkbutton" href="#" data-options="iconCls: 'icon-import'" onclick="registZCList.importZCExcel()" plain="true">导入</a></div>
			<div style="float:right;">
				<div style="float:left;line-height: 25px;">资产名称：</div><div style="float:left;padding-right: 10px;"><input style="width:100px" id="registZC_txbZcmc" name="zcmc"/></div>
				<div style="float:left;line-height: 25px;">资产类型：</div><div style="float:left;padding-right: 10px;"><input style="width:100px" id="registZC_searchbZclx" name="zclx"/></div>
				<div style="float:left;line-height: 25px;">所属部门：</div><div style="float:left;padding-right: 10px;"><input style="width:100px" id="registZC_searchbDeptName" name="deptName"/></div>
				<div style="float:left;line-height: 25px;padding-right: 10px;"><a class="easyui-linkbutton" data-options="iconCls: 'icon-search'" href="#" onclick="registZCList.query()" plain="true">筛选</a></div>
			</div>
		</div>
		<div id="dgregistZCList"></div>		
	</div>
<script>

	registZCList = {
			
		regist: function () {
			var registZCList_newRegistdlg = $("<div id='registZCList_newRegistdlg'></div>");
			registZCList_newRegistdlg.appendTo("body");
			$("#registZCList_newRegistdlg").dialog({
				
				href: getContextPath() + "/console/zcgl/regist/insertRegistZCUI.do",
				title: "资产登记",
				width: 512,
				height: 300,
				modal:true,
				inline: true,
				onClose: function(){
					registZCList_newRegistdlg.remove();
				}
			});
			
		},
			
		query: function () {
			
			$("#dgregistZCList").datagrid("load",{
				zcmc:$("#registZC_txbZcmc").val(),
				zclx:$("#registZC_searchbZclx").attr("lxid"),
				deptNo:$("#registZC_searchbDeptName").attr("deptno")
			});
		},
			
		edit: function (index) {
			editRow = $("#dgregistZCList").datagrid("getData").rows[index];
			var registZCList_newEditdlg = $("<div id='registZCList_newEditdlg'></div>");
			registZCList_newEditdlg.appendTo("body");
			$("#registZCList_newEditdlg").dialog({
				
				href: getContextPath() + "/console/zcgl/regist/updateRegistZCUI.do?id="+editRow.zcid,
				title: "编辑已登记资产",
				width: 512,
				height: 300,
				modal:true,
				inlne: true,
				onClose: function(){
					registZCList_newEditdlg.remove();
				}
			});
		},
		
		del: function (index) {
			var row = $("#dgregistZCList").datagrid("getData").rows[index];
			$.messager.confirm('确认','您确认想要删除该记录吗？',function(sure){    
			    if (sure){    
			        $.ajax({
			        	url: getContextPath() + "/console/zcgl/regist/delRegistZC.do",
			        	cache: false,
			        	data: {"id":row.zcid},
			        	dataType: "text",
			        	success: function (result) {
			    			if (result.trim() == "success"){
			    				$("#dgregistZCList").datagrid("reload");	
			    			}
			        		
			        	},
			        	error: function (){
			        		alert("Ajax Error!");	
			        	}
			        });   
			    } 
			});  
		},
		
		allocate: function (index) {
			allocateRow = $("#dgregistZCList").datagrid("getData").rows[index];
			var registZCList_newAllocatedlg = $("<div id='registZCList_newAllocatedlg'></div>");
			registZCList_newAllocatedlg.appendTo("body");
			$("#registZCList_newAllocatedlg").dialog({
				
				href: getContextPath() + "/console/zcgl/regist/allocateZCUI.do",
				title: "资产调拨",
				width: 512,
				height: 300,
				modal:true,
				inlne: true,
				onClose: function(){
					registZCList_newAllocatedlg.remove();
				}
			});
		},
		
		reallocate: function (index) {
			reallocateRow = $("#dgregistZCList").datagrid("getData").rows[index];
			var registZCList_newReallocatedlg = $("<div id='registZCList_newReallocatedlg'></div>");
			registZCList_newReallocatedlg.appendTo("body");
			$("#registZCList_newReallocatedlg").dialog({
				
				href: getContextPath() + "/console/zcgl/regist/reallocateZCUI.do",
				title: "资产重新调拨",
				width: 512,
				height: 300,
				modal:true,
				inlne: true,
				onClose: function(){
					registZCList_newReallocatedlg.remove();
				}
			});
		},
		importZCExcel: function () {
			var dlgregistZCList_newImportPic = $("<div id='dlgregistZCList_newImportPic'></div>");
			dlgregistZCList_newImportPic.appendTo("body");
			$("#dlgregistZCList_newImportPic").dialog({
				href: getContextPath() + "/console/zcgl/regist/importZCExcelUI.do",
				title: "批量导入资产",
				width: 512,
				height: 200,
				modal:true,
				inlne: true,
				onClose: function () {
					dlgregistZCList_newImportPic.remove();
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
							$("#registZC_searchbDeptName").searchbox("setValue",wxdept.deptName);
							$("#registZC_searchbDeptName").attr("deptno",wxdept.deptNo); 
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
						$("#registZC_searchbZclx").searchbox("setValue",czclx.mc);
						$("#registZC_searchbZclx").attr("lxid",czclx.lxId); 
					}
					dialogObj.remove();
				}
			});
		},
		check:function (index) {
			checkRow = $("#dgregistZCList").datagrid("getData").rows[index];
			var registZCList_newCheckdlg = $("<div id='registZCList_newCheckdlg'></div>");
			registZCList_newCheckdlg.appendTo("body");
			$("#registZCList_newCheckdlg").dialog({
				
				href: getContextPath() + "/console/zcgl/regist/checkZCUI.do?id="+checkRow.zcid,
				title: "资产审核",
				width: 512,
				height: 300,
				modal:true,
				inlne: true,
				onClose: function(){
					registZCList_newCheckdlg.remove();
				}
			});
			
		}
			
	};

	$("#dlgregistZCList").dialog({
		title: "资产登记列表",
		width: 1024,
		height: 600,
        cache: false,
        shadow: true, // 显示阴影
        resizable: false, // 不允许改变大小
        modal: true, // 模式化窗口，锁定父窗口
        inline: true, // 是否在父容器中，若不在会出现很多BUG
	});
	
	$("#registZC_txbZcmc").textbox({});
	
	$("#registZC_searchbZclx").searchbox({
		editable: false,
		searcher: function(value,name){
			registZCList.query_zclx();
		}
	});
	
	$("#registZC_searchbDeptName").searchbox({
	   editable: false,
		searcher: function(value,name){
			registZCList.query_dept();
		} 
	});
	
	$("#dgregistZCList").datagrid({
		url: getContextPath() + "/console/zcgl/regist/zcList.do",
		fit: true,
        singleSelect: true,
        pagination: true,
        striped: true,
        rownumbers: true,
        emptyMsg : "没有数据", 
        toolbar: "#tbregistZCList",
        pageSize: 15,
        pageList: [15],
        columns: [
                  [{
                      field: 'zcdm',
                      title: '资产编号',
                      align: 'center',
                      width: '10%',
                      resizable: false,
                      formatter:function(value,row){  
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                          return content;  
                      } 
                  }, {
                      field: 'zc',
                      title: '资产名称',
                      align: 'center',
                      width: '15%',
                      resizable: false,
                      formatter:function(value,row){
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";  
                          return content;  
                          } 
                  }, {
                      field: 'zclx',
                      title: '资产类型',
                      align: 'center',
                      width: '22%',
                      resizable: false,
                      formatter:function(value,row){ 
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                          return content;  
                      } 
                  }, {
                      field: 'cost',
                      title: '单价',
                      align: 'center',
                      width: '6%',
                      resizable: false,
                      formatter:function(value,row){  
                    	  if (value == null) {
                    		  value = "";
                    	  }
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";  
                          return content;  
                          } 

                  }, {
                      field: 'num',
                      title: '资产数量',
                      align: 'center',
                      width: '7%',
                      resizable: false,
                      formatter:function(value,row){  
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";  
                          return content;  
                      } 

                  }, {
                      field: 'deptName',
                      title: '所属部门',
                      align: 'center',
                      width: '14%',
                      resizable: false,
                      formatter:function(value,row){  
                    	  if (value == null) {
                    		  value="";
                    	  }
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                          return content;  
                      } 
                      

                  }, {
                      field: 'glrmc',
                      title: '部门资产管理员',
                      align: 'center',
                      width: '13%',
                      resizable: false,
                      formatter:function(value,row){  
                    	  if (value == null) {
                    		  value = "";
                    	  }
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";  
                          return content;  
                      } 

                  }, {
                      field: 'opt',
                      title: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作',
                      align: 'left',
                      width: '15%',
                      resizable: false,
                      formatter:function (value,row,index) {
                    	  if (row.zczt==9) {
	                    	  return "&nbsp;&nbsp;<a class='registZC_editZcBtn' onclick='registZCList.edit("+index+")' href='#'></a>&nbsp;&nbsp;<a class='registZC_delZcBtn' onclick='registZCList.del("+index+")' href='#'></a>&nbsp;&nbsp;<a class='registZC_allocateZcBtn' onclick='registZCList.allocate("+index+")' href='#'></a>";
                    	  }
                    	  if (row.zczt == 0) {
                    		  return "&nbsp;&nbsp;<a class='registZC_reallocateZcBtn' onclick='registZCList.reallocate("+index+")' href='#'></a>";
                    	  }
                    	  if (row.zczt == 13) {
                    		  return "&nbsp;&nbsp;<a class='registZC_checkZcBtn' onclick='registZCList.check("+index+")' href='#'></a>&nbsp;&nbsp;<a class='registZC_delZcBtn' onclick='registZCList.del("+index+")' href='#'></a>";
                    	  }
                      }
                  }]
              ],
              onLoadSuccess: function () {
            	  
            	  $(".registZC_editZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-edit",
            		  plain : true,
            	  });
            	  
            	  $(".registZC_editZcBtn").tooltip({
            		  position: 'bottom',    
            		  content: '编辑',
            	  });
            	  
            	  $(".registZC_delZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-no",
            		  plain : true,
            	  });
            	  
            	  $(".registZC_delZcBtn").tooltip({
            		  position: 'bottom',    
            		  content: '删除',
            	  });
            	  
            	  $(".registZC_allocateZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-allocate",
            		  plain : true,
            	  });
            	  
            	  $(".registZC_allocateZcBtn").tooltip({
            		  position: 'bottom',    
            		  content: '调拨',
            	  });
            	  
            	  $(".registZC_reallocateZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-reallocate",
            		  plain : true,
            	  });
            	  
            	  $(".registZC_reallocateZcBtn").tooltip({
            		  position: 'bottom',    
            		  content: '重新调拨',
            	  });
            	  $(".registZC_checkZcBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-edit",
            		  plain : true,
            	  });
            	  
            	  $(".registZC_checkZcBtn").tooltip({
            		  position: 'bottom',    
            		  content: '审核',
            	  });
            	  
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
	});
	
</script>
</body>
</html>
