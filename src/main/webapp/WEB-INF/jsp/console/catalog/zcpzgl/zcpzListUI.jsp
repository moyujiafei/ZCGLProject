<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>资产品种管理</title>
</head>

	<body>
	<%--
		1、最外层dialog容器，id必须根据当前业务取名(xxxDialog)，以防id出现重复
		2、工具栏id格式(xxxToolbar)
		3、按钮id格式（xxxBtn）
		4、  数据列表id格式(xxxDatagrid)
	 --%>
		<div id="zcpzListDialog">
			<div id="zcpzListToolbar" style="padding:5px;">
				<a id="insertZCPZBtn" href="javascript:void(0);">新建</a>
				<a id="zcpzListInsertBnt"></a>
				<a id="downZCPZExcelTempBnt"></a>
				<table align="right">
					<tr>
						<td>分类名称：</td>
						<td><input id="flmcCombox"> </td>
						<td>品种名称：</td>
						<td><input id="pzmcTextbox" type="text"></td>
						<td><a id="queryZCPZBtn" href="javascript:void(0);">筛选</a></td>
					</tr>
				</table>
			</div>
			<table id="zcpzListDatagrid"></table>
			<form id="downLoadForm" method="post">
				<input type="hidden" name="randomInfo" value="<%=Math.random()%>"/>
	 		</form>
		</div>
		
		<script type="text/javascript">
			var zcpzListOpt = {
					newDialog: function(dialogId,url,title,param){
						var dialogObj = $('<div id="' +dialogId + '"></div>');
						dialogObj.appendTo("body");
						$("#" + dialogId).dialog({
							method: "post",
							href: getContextPath() + url,
							title: title,
							queryParams: param,
							width: 512,
							height: 300,
							modal: true,
							draggable: true,
							inline: true,
							onClose: function(){
								dialogObj.remove();
							}
						});
					},
					update: function(index){
						var rowInfo = $("#zcpzListDatagrid").datagrid("getRows")[index];
						var param = {"id": rowInfo.id};
						console.log(param);
						zcpzListOpt.newDialog("updateZCPZDialog", "/console/catalog/zcpzgl/updateZCPZUI.do", "编辑资产品种", param);
					},
					insert: function(){
						zcpzListOpt.newDialog("insertZCPZDialog", "/console/catalog/zcpzgl/insertZCPZUI.do", "新建资产品种");
					},
					query: function(){
						var flmc = $("#flmcCombox").combobox("getValue");
						var pzmc = $.trim($("#pzmcTextbox").textbox("getValue"));
						var param;
						if(flmc==""){
							param = {"pzmc":pzmc};
						}else{
							param = {"flmc":flmc,"pzmc":pzmc};
						}
						console.log(param);
						$("#zcpzListDatagrid").datagrid("load",param);
					},
					downZCPZExcelTemp: function(){
						$.messager.confirm("导出","确定下载资产品种模板表格吗？",function(result){
							if(result){
								$("#downLoadForm").form('submit', {
				 				url : getContextPath() + "/console/catalog/zcpzgl/exportZCPZTempExcel.do",
				 				success: function(data){
				 					if(data != "success"){
				 						$.messager.alert('失败',data,'warning');
				 					}
				 				}
				 				});
							}
						});
					}
			}
			
			$(function() {
				// 定义zcpzList要显示的位子的dialog的属性
				$("#zcpzListDialog").dialog({
					title: '资产品种列表',
					width: 1024,
					height: 600,
					closed: false,
					cache: false,
					shadow: true,
					modal: true,
					inline: true,
				});
				
				// 新建按钮
				$("#insertZCPZBtn").linkbutton({
	            	plain: true,
	            	iconCls: 'icon-add',
	            	onClick: function(){
	            		zcpzListOpt.insert();
	            	}
	            });
				
				// 下拉列表框
				$("#flmcCombox").combobox({    
					width:"200px",
				    url: getContextPath() +"/console/catalog/zcpzgl/getZCLXComboWithAll.do",    
				    valueField: 'id',
				    textField: 'text',
				    editable:false,
				    onSelect:function(record){
	                	$("#flmcCombox").val(record.id);
	                },
	                onLoadSuccess:function(){
	                	var val = $(this).combobox("getData");
	                	for(var item in val[0]){
	                		if(item =="id"){
	                			$(this).combobox("select",val[0][item]);
	                		}
	                	}
	                }
				}); 
				
				// 文本框
				$("#pzmcTextbox").textbox({
					prompt: "请输入品种名称",
					width:200,
					value:null
				});
				
				// 查询按钮
				$("#queryZCPZBtn").linkbutton({
	            	plain: true,
	            	iconCls: 'icon-search',
	            	onClick: function(){
	            		zcpzListOpt.query();
	            	}
	            });
				
				//批量导入按钮
				$("#zcpzListInsertBnt").linkbutton({
					iconCls:'icon-import',
					plain:true,
					text:'导入',
					onClick:function(){
						var insertList = $("<div id='insertZcpzListDialog'></div>");
						insertList.appendTo("body");
						$("#insertZcpzListDialog").dialog({
							href: getContextPath() + "/console/catalog/zcpzgl/insertZCPZListUI.do",
							title: "批量导入资产品种",
							width: 512,
							height: 200,
							modal:true,
							inlne: true,
							onClose: function () {
								insertList.remove();
							}
						});
					}
				});
				
				// 模板下载按钮
				$("#downZCPZExcelTempBnt").linkbutton({
			     	plain: true,
			     	iconCls: 'icon-download',
			     	text:'模板',
			     	onClick: function(){
			     		zcpzListOpt.downZCPZExcelTemp();
			     	}
			     });
				
				
				// 定义dialog中的表的属性
				$("#zcpzListDatagrid").datagrid({
	                url: getContextPath() + "/console/catalog/zcpzgl/zcpzList.do",
	                fit: true,
	                fitColumns: true,
	                striped: true, //斑马线效果
	                loadMsg: "数据正在加载当中，请稍等...", //加载数据时的提示消息
	                pagination: true, //分页工具栏
	                rownumbers: true, //显示行列号
	                singleSelect: true, //只允许选中一行
	                pageSize: 15, //设置分页时的初始化的分页大小
	                pageList: [15], //选择分页显示行数的列表里面的值
	                toolbar: "#zcpzListToolbar",
	                nowrap: true, //是否在一行显示所有，自动换行
	                emptyMsg : "没有找到符合条件的记录",
	                columns: [
	                    [{
	                        field: "plx",
	                        title: "分类名称",
	                        width: "20%",
	                        align: "center",
	                        ahlign: "center",
	                        resizable: false,
	                    }, {
	                        field: "lx",
	                        title: "品种名称",
	                        width: "20%",
	                        align: "center",
	                        halign: "center",
	                        resizable: false,
	                        formatter:function(value,row,index){
								 return '<div class="zcpzToolTip'+index+'">'+value+'</div>';
							}
	                    },{
	                        field: "zjnx",
	                        title: "折旧年限",
	                        width: "20%",
	                        align: "center",
	                        halign: "center",
	                        resizable: false,
	                        formatter:function(value,row,index){
								 return '<div class="zcpzToolTip'+index+'">'+value+'</div>';
							}
	                    }, {
	                    	field: "remark",
	                        title: "备注",
	                        width: "27%",
	                        align: "center",
	                        halign: "center",
	                        resizable: false,
	                        formatter:function(value,row,index){
								 return '<div class="zcpzToolTip'+index+'">'+value+'</div>';
							}
	                    } ,{
	                        field: "option",
	                        title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
	                        width: "15%",
	                        align: "left",
	                        resizable: false,
	                        formatter: function(value, row, index) {
	                            return '&nbsp;&nbsp;<a href="#" class="updateZCPZBtn"  title="编辑" onclick="zcpzListOpt.update(' 
	                             + index + ')"></a>';
	                        }
	                    } ]
	                ],
	                onLoadSuccess: function(data) {
	                      $(".updateZCPZBtn").linkbutton({
	                          iconCls: "icon-edit",
	                          plain: true,
	                          height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
	                      });
	                      for(var i=0;i<data.rows.length;i++){
							var id = data.rows[i].id;//获得当前行的id
							var imgRequest = getContextPath()+'/console/catalog/zclxgl/getPicUrl.do?id='+id;//通过id查询此资产的资源路径的action
							var imgURL;//图片资源的路径
							
							$.ajax({url:imgRequest,
							async:false,
							dataType:"text",
							success:function(url){
								imgURL = getContextPath()+url+"_m.jpg";
							}
							});
							
							$('.zcpzToolTip'+i).tooltip({   
							showDelay:'1000',//延迟2s显示 
							position: 'right',
							content: '<img src="'+imgURL+'" ></img>',    
							onShow: function(){        
								$(this).tooltip('tip').css({            
									backgroundColor: '#666',            
									borderColor: '#666',
								});
							}
							});
						}
	                  }
			});
		});
		</script>
	</body>
</html>