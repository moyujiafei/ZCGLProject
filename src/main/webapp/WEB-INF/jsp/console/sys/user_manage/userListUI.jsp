<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
      <meta charset="utf-8">
      <title>当前用户信息</title>
  </head>

  <body>
      <div id="userListDialog">
          <div id="userListToolbar" style="padding:5px;">
              <a id="insertUserBtn" href="javascript:void(0);">新增</a>
          </div>
          <table id="userListDatagrid"> </table>
      </div>
      <script type="text/javascript">
          var userListOpt = {
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
              dele: function(index) {
                  $.messager.confirm("提示", "您确定要删除该条记录吗?", function(sure) {
                      var row = $("#userListDatagrid").datagrid("getRows")[index];
                      if (sure) {
                          //发送删除的请求
                          $.messager.progress(); // 显示进度条
                          $.ajax({
                              url: getContextPath() + "/console/user/deleteUserInfo.do",
                              cache: false,
                              async: true,
                              data: {
                                  "uid": row.uid
                              },
                              type: "post",
                              dataType: "text",
                              success: function(result) {
                                  $.messager.progress('close');
                                  if (result == "success") {
                                      $.messager.alert('成功', '删除成功');
                                      $("#userListDatagrid").datagrid("reload");
                                  } else {
                                      $.messager.alert('失败', '删除失败');
                                  }
                              },
                              error: function() {
                                  $.messager.progress('close');
                                  $.messager.alert('失败', '删除失败');
                              }
                          });
                      }
                  });
              },
              update: function(index) {
                  var row = $("#userListDatagrid").datagrid("getRows")[index];
                  userListOpt.newDialog("updateUserDialog","/console/user/updateUserUI.do","编辑用户", {"uid": row.uid});
              },
              insert: function() {
              	  userListOpt.newDialog("insertUserDialog","/console/user/insertUserUI.do","新增用户");
              },
              reset: function(index) {
                  $.messager.confirm("提示", "您确定将密码重置为123456吗?", function(sure) {
                      var row = $("#userListDatagrid").datagrid("getRows")[index];
                      if (sure) {
                          //执行重置的操作
                          $.messager.progress(); // 显示进度条
                          $.ajax({
                              url: getContextPath() + "/console/user/resetPassword.do",
                              cache: false,
                              async: true,
                              data: {
                                  "uid": row.uid
                              },
                              type: "post",
                              dataType: "text",
                              success: function(result) {
                                  $.messager.progress('close');
                                  if (result == "success") {
                                      $.messager.alert('成功', '重置成功');
                                  } else {
                                      $.messager.alert('失败', result);
                                  }
                              },
                              error: function() {
                                  $.messager.progress('close');
                                  $.messager.alert('失败', '重置失败');
                              }
                          });
                      }
                  });
              },
          };
          $(function() {
              //定义userList要显示的位子的dialog的属性
              $("#userListDialog").dialog({
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
              $("#insertUserBtn").linkbutton({
	            	plain: true,
	            	iconCls: 'icon-add',
	            	onClick: function(){
	            		userListOpt.insert()
	            	}
              });
              //定义dialog中的表的属性
              $("#userListDatagrid").datagrid({
                  url: getContextPath() + "/console/user/userList.do",
                  fit: true,
                  striped: true, //斑马线效果
                  loadMsg: "数据正在加载当中，请稍等...", //加载数据时的提示消息
                  pagination: true, //分页工具栏
                  rownumbers: true, //显示行列号
                  singleSelect: true, //只允许选中一行
                  pageSize: 15, //设置分页时的初始化的分页大小
                  pageList: [15], //选择分页显示行数的列表里面的值
                  toolbar: "#userListToolbar",
                  nowrap: true, //是否在一行显示所有，自动换行
                  columns: [
                      [{
                          field: "name",
                          title: "用户姓名",
                          width: "16%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      }, {
                          field: "uname",
                          title: "账号",
                          width: "17%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      },{
                          field: "corpName",
                          title: "企业名称",
                          width: "20%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      }, {
                          field: "roleName",
                          title: "现有角色",
                          width: "20%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      }, {
                          field: "option",
                          title: "操作",
                          width: "25%",
                          align: "center",
                          resizable: false,
                          formatter: function(value, row, index) {
                              return "<a href='#' class='editBtn' onclick='userListOpt.update(" + index + ")'>编辑</a>&nbsp;&nbsp;<a href='#' class='deleBtn' onclick='userListOpt.dele(" + index + ")'>删除</a>&nbsp;&nbsp;<a href='#' class='resetBtn' onclick='userListOpt.reset(" + index + ")'>重置</a>";
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
                      $(".resetBtn").linkbutton({
                          iconCls: "icon-reload",
                          height: 24,
                          plain: true
                      });
                  }
              });
          });
      </script>
  </body>
</html>

