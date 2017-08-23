<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>企业易耗品登记入库</title>
    
  </head>
  <body>
	<div id="registYHPListDialog">
		<div id="YHPListToolbar" style="width: 100%;height:30px;padding-top: 10px;">
			<div style="float:left;"><a class="easyui-linkbutton" href="#" data-options="iconCls: 'icon-add'" onclick="registYHPList.regist()" plain="true">登记</a></div>
			<div style="float:right;">
				<div style="float:left;line-height: 25px;">易耗品类型：</div><div style="float:left;padding-right: 10px;"><input style="width:100px" id="searchbYhpLx" name="yhplx"/></div>
				<div style="float:left;line-height: 25px;padding-right: 10px;"><a class="easyui-linkbutton" data-options="iconCls: 'icon-search'" href="#" onclick="registYHPList.query()" plain="true">筛选</a></div>
			</div>
		</div>
		<div id="registYHPList"></div>		
	</div>
	<script type="text/javascript">
		registZCList = {
			
		regist: function () {
			var registYHPDialog = $("<div id='registYHPDialog'></div>");
			registYHPDialog.appendTo("body");
			$("#registYHPDialog").dialog({
				
				href: getContextPath() + "/console/yhpgl/yhpdj/insertQYYHPUI.do",
				title: "低值易耗品登记",
				width: 512,
				height: 300,
				modal:true,
				inline: true,
				onClose: function(){
					registYHPDialog.remove();
				}
			});
			
		},
		query: function () {
			
			$("#registYHPList").datagrid("load",{
				yhplx:$("#searchbYhpLx").attr("lxid"),
			});
		},
		allocate: function (index) {
			allocateRow = $("#dgregistZCList").datagrid("getData").rows[index];
			var allocateYHPDialog = $("<div id='allocateYHPDialog'></div>");
			allocateYHPDialog.appendTo("body");
			$("#allocateYHPDialog").dialog({
				
				href: getContextPath() + "/console/yhpgl/yhpdj/allocateQYYHPUI.do",
				title: "低值易耗品调拨",
				width: 512,
				height: 300,
				modal:true,
				inlne: true,
				onClose: function(){
					allocateYHPDialog.remove();
				}
			});
		},
		queryYhplx: function(){
			dialogObj=$("<div id='YhpLxDialogDiv'></div>");
			dialogObj.appendTo("body");
			cyhplx=null;
			$("#YhpLxDialogDiv").dialog({
				title: "查找易耗品类型",
				href: getContextPath()+"/console/catalog/yhplxgl/queryYhplxUI.do",
				width: 512,
                height: 300,
                cache: false,
				queryParams: {isEdit: true},
				modal: true,
				inline: true,
				onClose: function(){
					if(cyhplx!=null){
						$("#yhplxSearchBox").searchbox("setValue",cyhplx.mc);
						$("#yhplxSearchBox").attr("lxid",cyhplx.lxid);
					}
					dialogObj.remove();// 关闭时remove对话框
				}
			});
		},
	};

	$("#registYHPListDialog").dialog({
		title: "企业易耗品登记入库",
		width: 1024,
		height: 600,
        cache: false,
        shadow: true, // 显示阴影
        resizable: false, // 不允许改变大小
        modal: true, // 模式化窗口，锁定父窗口
        inline: true, // 是否在父容器中，若不在会出现很多BUG
	});
	
	
	$("#searchbYhpLx").searchbox({
		editable: false,
		searcher: function(value,name){
			registZCList.queryYhplx();
		}
	});
	
	$("#registYHPList").datagrid({
		url: getContextPath() + "/console/zcgl/regist/zcList.do",
		fit: true,
        singleSelect: true,
        pagination: true,
        striped: true,
        rownumbers: true,
        emptyMsg : "没有数据", 
        toolbar: "#YHPListToolbar",
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
