<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
      <meta charset="utf-8">
      <title>微信应用列表</title>
  </head>

  <body>
      <div id="wxAppListDialog">
          <div id="wxAppListToolbar" style="padding:5px;">
              <a id="insertWxAppBtn" href="javascript:void(0);">新增</a>
          </div>
          <table id="wxAppListDatagrid"> </table>
      </div>
      <script type="text/javascript">
          var wxAppListOpt = {
          	  newDialog: function(dialogId,url,title,param){
					var dialogObj = $('<div id="' + dialogId + '"></div>');
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                    href: getContextPath() + url,
	                    title: title,
	                    queryParams: param,
	                    width: 512,
	                    height: 300,
	                    modal: true,
	                    draggable: true,
	                    inline: true,
	                    onClose: function() {
	                        dialogObj.remove();// 关闭时remove对话框
	                    }
	                });
			  },
              update: function(index) {
                  var row = $("#wxAppListDatagrid").datagrid("getRows")[index];
                  wxAppListOpt.newDialog("updateWxAppDialog","/console/sys/wxapp/updateWXAPPUI.do","编辑用户", {"appId": row.appId});
              },
              insert: function() {
              	  wxAppListOpt.newDialog("insertWxAppDialog","/console/sys/wxapp/insertWXAPPUI.do","新增用户");
              },
              dele: function(index){
                  var row = $("#wxAppListDatagrid").datagrid("getRows")[index];
            	  $.messager.progress();
				  $.ajax({
	            	  url: getContextPath() + "/console/sys/wxapp/delWXAPP.do",
					  cache : false,
					  async : true, //异步操作
					  data : {"appId": row.appId},
					  type : "post",
					  dataType : "text",
		              success: function(data) {
		                    $.messager.progress('close');
		                    if (data != 'success') {
		                        $.messager.alert('失败', data, 'info');
		                        return false;
		                    } else {
		                        $.messager.alert('成功', '删除成功', 'info');
								$("#wxAppListDatagrid").datagrid('reload');
		                    }
		              },
		              error: function() {
                          $.messager.progress('close');
                          $.messager.alert('失败', '删除失败', 'info');
                      }
			      });
              		
              },
              sync: function(index) {
                  var row = $("#wxAppListDatagrid").datagrid("getRows")[index];
                  $.messager.progress();
				  $.ajax({
				      url : getContextPath() + "/console/sys/wxapp/sync.do",
					  cache : false,
					  async : true, //异步操作
					  data : {"appId": row.appId},
					  type : "post",
					  dataType : "text",
		              success: function(data) {
		                    $.messager.progress('close');
		                    if (data != 'success') {
		                        $.messager.alert('失败', data, 'info');
		                        return false;
		                    } else {
		                        $.messager.alert('成功', '同步成功', 'info');
		                    }
		              },
					  error: function(){
						    alert(arguments[1]);
						}
			      });
              }       
          };
          $(function() {
              //定义wxAppList要显示的位子的dialog的属性
              $("#wxAppListDialog").dialog({
                  title: "用户列表",
                  width: 1024,
                  height: 600,
                  inline: true,
                  cache: false,
                  shadow: true,	
                  closed: false,
                  modal: true,
              });
              // 新增按钮
              $("#insertWxAppBtn").linkbutton({
	            	plain: true,
	            	iconCls: 'icon-add',
	            	onClick: function(){
	            		wxAppListOpt.insert()
	            	}
              });
              //定义dialog中的表的属性
              $("#wxAppListDatagrid").datagrid({
                  url: getContextPath() + "/console/sys/wxapp/wxAppList.do",
                  fit: true,
                  striped: true, //斑马线效果
                  loadMsg: "数据正在加载当中，请稍等...", //加载数据时的提示消息
                  pagination: true, //分页工具栏
                  rownumbers: true, //显示行列号
                  singleSelect: true, //只允许选中一行
                  pageSize: 15, //设置分页时的初始化的分页大小
                  pageList: [15], //选择分页显示行数的列表里面的值
                  toolbar: "#wxAppListToolbar",
                  nowrap: true, //是否在一行显示所有，自动换行
                  columns: [
                      [{
                          field: "corpName",
                          title: "企业名",
                          width: "15%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      }, {
                          field: "corpId",
                          title: "企业号",
                          width: "15%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      },{
                          field: "appName",
                          title: "应用名",
                          width: "10%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      }, {
                          field: "appId",
                          title: "应用号",
                          width: "5%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      }, {
                          field: "serverUrl",
                          title: "服务器地址",
                          width: "20%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      },{
                          field: "stop",
                          title: "是否停用",
                          width: "10%",
                          align: "center",
                          formatter: function(value){
                              if (value == null || value.length < 1){
                                  return "";
                              } else if(value == "0"){
                                  return "启用";
                              } else if(value == "1"){
                            	  return "停用";
                              } else{
                            	  return "";
                              }
                          },
                          halign: "center",
                          resizable: false,
                      },{
                          field: "option",
                          title: "操作",
                          width: "25%",
                          align: "center",
                          resizable: false,
                          formatter: function(value, row, index) {
                              return "<a href='#' class='editBtn' onclick='wxAppListOpt.update(" + index + ")'>编辑</a>&nbsp;&nbsp;&nbsp;<a href='#' class='deleBtn' onclick='wxAppListOpt.dele(" + index + ")'>删除</a>&nbsp;&nbsp;&nbsp;<a href='#' class='syncBtn' onclick='wxAppListOpt.sync(" + index + ")'>同步</a>&nbsp;";
                          }
                      } ]
                  ],
                  onLoadSuccess: function(data) {
                      $(".editBtn").linkbutton({
                          iconCls: "icon-edit",
                          plain: true,
                          height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
                      });
                      $(".deleBtn").linkbutton({
                          iconCls: "icon-no",
                          height: 24,
                          plain: true
                      });
                      $(".syncBtn").linkbutton({
                          iconCls: "icon-reload",
                          plain: true,
                          height: 24,
                      });
                  }
              });
          });
      </script>
  </body>
</html>

