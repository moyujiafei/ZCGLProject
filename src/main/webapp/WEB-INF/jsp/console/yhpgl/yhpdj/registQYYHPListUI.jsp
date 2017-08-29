<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>企业易耗品登记入库</title>
    
  </head>
  <body>
	<div id="registYHPListDialog">
		<div id="YHPListToolbar" style="width: 100%;height:30px;padding-top: 10px;">
			<div style="float:left;"><a class="easyui-linkbutton" href="#" data-options="iconCls: 'icon-add'" onclick="registQYYHPList.registYHP()" plain="true">登记</a></div>
			<div style="float:right;">
				<div style="float:left;line-height: 25px;">易耗品类型：</div><div style="float:left;padding-right: 10px;"><input style="width:100px" id="QY_YhpLx" name="yhplx"/></div>
				<div style="float:left;line-height: 25px;padding-right: 10px;"><a class="easyui-linkbutton" data-options="iconCls: 'icon-search'" href="#" onclick="registQYYHPList.query()" plain="true">筛选</a></div>
			</div>
		</div>
		<div id="registYHPList"></div>		
	</div>
	<script type="text/javascript">
	cyhplx=null;
	var registQYYHPList = {
			
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
		newDialog: function(dialogId,url,title,param){
			// 弹出新的对话框
			var dialogObj = $('<div id="' + dialogId + '"></div>');
			dialogObj.appendTo("body");
			$("#" + dialogId).dialog({
                href: getContextPath() + url,
                title: title,
                queryParams: param,
                width: 512,
                height: 300,
                shadow : true, //显示阴影
        		resizable : false, //不允许改变大小
                modal: true,
                inline: true,
                onClose: function() {
                    dialogObj.remove();// 关闭时remove对话框
                }
            });
		},
		query: function () {
			if(cyhplx!=null){
					var lxId=cyhplx.lxId;
					$("#registYHPList").datagrid("load",{
						lxId: lxId	
					});
			}
		},
		editYHP: function(index){
			editYHP = $("#registYHPList").datagrid('getData').rows[index];
			registQYYHPList.newDialog("editYHPDialog", "/console/yhpgl/yhpdj/updateYHPUI.do", "编辑低值易耗品",{});
		},
		addYHP : function(index){
			bhYHP = $("#registYHPList").datagrid('getData').rows[index];
			registQYYHPList.newDialog("bhYHPDialog", "/console/yhpgl/yhpdj/addYHPUI.do", "补货低值易耗品",{});
			
		},
		allocateYHP: function () {
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
		delYHP: function(index){
			var row=$("#registYHPList").datagrid("getSelected");
			$.ajax({
			    url:"http://www.microsoft.com",    //请求的url地址
			    dataType:"text",   //返回格式为json
			    async:true,//请求是否异步，默认为异步，这也是ajax重要特性
			    data:{"yhpid":row.yhpId},    //参数值
			    type:"get",   //请求方式
			    success:function(req){
			        //请求成功时处理
			    },
			    error:function(){
			        $.messager.alert("错误","Ajax Error！","error");
			        //请求出错处理
			    }
			});
		},
		queryYhplx: function(dialogId,url,title,param,searchboxId){
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
                       	$("#"+searchboxId).attr("lx_id",cyhplx.lxId);
                       }
                       dialogObj.remove();// 关闭时remove对话框
                   }
               });
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
	
	
	$("#QY_YhpLx").searchbox({
		editable: false,
		searcher: function(value,name){
			registQYYHPList.queryYhplx("queryYHPLxDialog","/console/catalog/yhplxgl/queryYhplxUI.do","易耗品类型查找",{isEdit: false},"QY_YhpLx");
		}
	});
	
	$("#registYHPList").datagrid({
		url: getContextPath() + "/console/yhpgl/yhpdj/getQYYHPList.do",
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
                      field: 'deptName',
                      title: '操作部门',
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
                      align: 'left',
                      width: '15%',
                      resizable: false,
                      formatter:function (value,row,index) {
                    	  if(row.deptNo!=null){
                    	  	return "<a class='editYHPBtn' onclick='registQYYHPList.editYHP("+index+")' href='#'></a>&nbsp;<a class='AddYHPBtn' onclick='registQYYHPList.addYHP("+index+")' href='#'></a>&nbsp;";
                    	  }
                    	  return "<a class='editYHPBtn' onclick='registQYYHPList.editYHP("+index+")' href='#'></a>&nbsp;<a class='AddYHPBtn' onclick='registQYYHPList.addYHP("+index+")' href='#'></a>&nbsp;"
                    	  +"<a class='allocateYHPBtn' onclick='registQYYHPList.allocateYHP()' href='#'></a>&nbsp;"
                    	  +"<a class='delYHPBtn' onclick='registQYYHPList.delYHP("+index+")' href='#'></a>";
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
            		  iconCls:"icon-buhuo",
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
            	  
            	  $(".delYHPBtn").linkbutton ({
            		  height : 22,
            		  iconCls:"icon-del",
            		  plain : true,
            	  });
            	  
            	  $(".delYHPBtn").tooltip({
            		  position: 'bottom',    
            		  content: '删除',
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
            	  
              }
	});
		
	</script>
  </body>
</html>
