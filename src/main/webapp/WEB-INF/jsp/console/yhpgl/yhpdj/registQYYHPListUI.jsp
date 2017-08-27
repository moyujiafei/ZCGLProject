<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>企业易耗品登记入库</title>
    
  </head>
  <body>
	<div id="registYHPListDialog">
		<div id="YHPListToolbar" style="width: 100%;height:30px;padding-top: 10px;">
			<div style="float:left;"><a class="easyui-linkbutton" href="#" data-options="iconCls: 'icon-add'" onclick="registYHPList.registYHP()" plain="true">登记</a></div>
			<div style="float:right;">
				<div style="float:left;line-height: 25px;">易耗品类型：</div><div style="float:left;padding-right: 10px;"><input style="width:100px" id="searchbYhpLx" name="yhplx"/></div>
				<div style="float:left;line-height: 25px;padding-right: 10px;"><a class="easyui-linkbutton" data-options="iconCls: 'icon-search'" href="#" onclick="registYHPList.query()" plain="true">筛选</a></div>
			</div>
		</div>
		<div id="registYHPList"></div>		
	</div>
	<script type="text/javascript">
	var registYHPList = {
			
		registYHP: function () {
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
					$("#registYHPList").datagrid("reload");
				}
			});
		},
		query: function () {
			$("#registYHPList").datagrid("load",{
				lx:	$("#searchbYhpLx").val()
			});
		},
		allocateYHP: function (index) {
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
						$("#searchbYhpLx").searchbox("setValue",cyhplx.mc);
						$("#searchbYhpLx").attr("id",cyhplx.id);
					}
					dialogObj.remove();// 关闭时remove对话框
				}
			});
		},
		editYHP: function(index){
		}
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
			registYHPList.queryYhplx();
		}
	});
	
	$("#registYHPList").datagrid({
		url: getContextPath() + "/console/yhpgl/yhpdj/getYHPList.do",
		fit: true,
        singleSelect: true,
        pagination: true,
        striped: true,
        rownumbers: true,
        emptyMsg : "没有数据", 
        toolbar: "#YHPListToolbar",
        pageSize: 15,
        pageList: [15],
        fitColumns: true,
        columns: [
                  [{
                      field: 'lx',
                      title: '类型',
                      align: 'center',
                      width: '15%',
                      resizable: true,
                      formatter:function(value,row){  
                          if(value==null){
                        	  value="无";
                          }
                    	  var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                          return content;  
                      } 
                  }, {
                      field: 'xh',
                      title: '规格型号',
                      align: 'center',
                      width: '15%',
                      resizable: true,
                      formatter:function(value,row){
                    	  if(value==null){
                        	  value="无";
                          }
                    	  var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";  
                          return content;  
                          } 
                  }, {
                      field: 'ccbh',
                      title: '出厂编号',
                      align: 'center',
                      width: '15%',
                      resizable: true,
                      formatter:function(value,row){ 
                    	  if(value==null){
                        	  value="无";
                          }
                    	  var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                          return content;  
                      } 
                  }, {
                      field: 'num',
                      title: '持有数量',
                      align: 'center',
                      width: '11%',
                      resizable: true,
                      formatter:function(value,row){  
                    	  if(value==null){
                        	  value="无";
                          }
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";  
                          return content;  
                          } 

                  }, {
                      field: 'cfdd',
                      title: '存放地点',
                      align: 'center',
                      width: '20%',
                      resizable: true,
                      formatter:function(value,row){  
                    	  if(value==null){
                        	  value="无";
                          }
                    	  var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";  
                          return content;  
                      } 

                  }, {
                      field: 'leftLimit',
                      title: '库存下限',
                      align: 'center',
                      width: '10%',
                      resizable: true,
                      formatter:function(value,row){  
                    	  if(value==null){
                        	  value="无";
                          }
                          var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
                          return content;  
                      } 
                      

                  },{
                      field: 'opt',
                      title: '操作',
                      align: 'center',
                      width: '15%',
                      resizable: false,
                      formatter:function (value,row,index) {
	                    	  return "<a class='editYHPBtn' onclick='registYHPList.editYHP("+index+")' href='#'></a>&nbsp;<a class='AddYHPBtn' onclick='registYHPList.AddYHP("+index+")' href='#'></a>&nbsp;<a class='allocateYHPBtn' onclick='registYHPList.allocateYHP("+index+")' href='#'></a>";
                      }
                  }]
              ],
              onLoadSuccess: function () {
            	  
            	  $(".editYHPBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-edit",
            		  plain : true,
            	  });
            	  
            	  $(".editYHPBtn").tooltip({
            		  position: 'bottom',    
            		  content: '编辑',
            	  });
            	  
            	  $(".AddYHPBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-add",
            		  plain : true,
            	  });
            	  
            	  $(".AddYHPBtn").tooltip({
            		  position: 'bottom',    
            		  content: '补货',
            	  });
            	  
            	  $(".allocateYHPBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-allocate",
            		  plain : true,
            	  });
            	  
            	  $(".allocateYHPBtn").tooltip({
            		  position: 'bottom',    
            		  content: '调拨',
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
