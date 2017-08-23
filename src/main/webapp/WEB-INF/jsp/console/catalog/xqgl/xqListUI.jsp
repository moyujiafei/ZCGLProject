<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" >
<title>校区管理</title>
</head>
<body>
	<%--
		1、最外层dialog容器，id必须根据当前业务取名(xxxDialog)，以防id出现重复
		2、工具栏id格式(xxxToolbar)
		3、按钮id格式（xxxBtn）
		4、  数据列表id格式(xxxDatagrid)
	 --%>
	<div id="xqManageDialog">
		<div id="xqManageToolbar" style="padding:5px;">
            <a id="xqInsertBtn" href="javascript:void(0);">新增</a>
        </div>
		<table id="xqManageDatagrid"></table>
	</div>
	<script type="text/javascript">
		/**
		            注意：1、所有变量申明前面必须加var；
		    	2、所有json格式最后一个元素后的逗号必须去掉，添加或删除元素是注意逗号增减；
		*/
		// 自定义方法全部以js对象的方式声明，变量名(xxxOpt),注：变量前面的“var”必须写
		var xqManageOpt = {
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
                    modal: true,
                    draggable: true,
                    inline: true,
                    onClose: function() {
                        dialogObj.remove();// 关闭时remove对话框
                    }
                });
			},
			insert: function(){
				xqManageOpt.newDialog("insertXqDialog","/console/catalog/xqgl/insertXQUI.do","新建校区");
			},
			update: function(index){
				var rowInfo = $("#xqManageDatagrid").datagrid("getRows")[index];
				var param = {"xqid": rowInfo.id};
				xqManageOpt.newDialog("updateXqDialog","/console/catalog/xqgl/updateXQUI.do","编辑校区",param);
			},
			dele: function(index) {
                  $.messager.confirm("提示", "您确定要删除该条记录吗?", function(sure) {
                      var rowInfo = $("#xqManageDatagrid").datagrid("getRows")[index];
                      if (sure) {
                          //发送删除的请求
                          $.messager.progress(); // 显示进度条
                          $.ajax({
                              url: getContextPath() + "/console/catalog/xqgl/delXQ.do",
                              cache: false,
                              async: true,
                              data: {
                                  "xqid": rowInfo.id
                              },
                              type: "post",
                              dataType: "text",
                              success: function(data) {
                                  $.messager.progress('close');
                                  if (data == "success") {
                                      $.messager.alert('成功', '删除成功', 'info');
                                      $("#xqManageDatagrid").datagrid("reload");
                                  } else {
                                      $.messager.alert('失败', data, 'info');
                                  }
                              },
                              error: function() {
                                  $.messager.progress('close');
                                  $.messager.alert('失败', '删除失败', 'info');
                              }
                          });
                      }
                  });
              }
		}
		// 所有组件初始化代码全部写在"$(function(){ })"之中
		$(function(){
			// 主对话框
			$("#xqManageDialog").dialog({
                title: "校区列表",
                width: 1024,
                height: 600,
                inline: true,
                cache: false,
                shadow: true,
                closed: false,
                modal: true,
            });
            // 新增按钮
            $("#xqInsertBtn").linkbutton({
            	plain: true,
            	iconCls: 'icon-add',
            	onClick: function(){
            		xqManageOpt.insert();
            	}
            });
            // 数据列表
            $("#xqManageDatagrid").datagrid({
                  url: getContextPath() + "/console/catalog/xqgl/xqList.do",
                  fit: true,
                  fitColumns: true,
                  striped: true, //斑马线效果
                  loadMsg: "数据正在加载当中，请稍等...", //加载数据时的提示消息
                  pagination: true, //分页工具栏
                  rownumbers: true, //显示行列号
                  singleSelect: true, //只允许选中一行
                  pageSize: 15, //设置分页时的初始化的分页大小
                  pageList: [15], //选择分页显示行数的列表里面的值
                  toolbar: "#xqManageToolbar",
                  nowrap: true, //是否在一行显示所有，自动换行
                  columns: [
                      [{
                          field: "xqmc",
                          title: "校区名称",
                          width: "26%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      }, {
                          field: "xqdz",
                          title: "校区地址",
                          width: "41%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      },{
                          field: "yb",
                          title: "邮编",
                          width: "20%",
                          align: "center",
                          halign: "center",
                          resizable: false,
                      },  {
                          field: "option",
                          title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
                          width: "15%",
                          align: "left",
                          resizable: false,
                          formatter: function(value, row, index) {
                              return '&nbsp;&nbsp;<a href="#" class="editBtn" title="编辑" onclick="xqManageOpt.update(' 
                              	+ index + ')"></a>&nbsp;&nbsp;<a href="#" class="deleBtn"  title="删除" onclick="xqManageOpt.dele(' 
                              	+ index + ')"></a>';
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