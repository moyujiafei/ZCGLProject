<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资产使用管理</title>
</head>
<body>
	<div id="leadingZCDialog">
		<div id="leadingZCToolbar" style="padding:5px">
			<table>
				<tr>
					<td>${user.name}的资产列表</td>
				</tr>
			</table>
		</div>
		<table id="leadingZCDatagrid"></table>
	</div>
	<script type="text/javascript">

		var leadingZCManageOpt = {

			newDialog : function(dialogId, url, title, param) {
				// 弹出新的对话框
				var dialogObj = $('<div id="' + dialogId + '"></div>');
				dialogObj.appendTo("body");
				$("#" + dialogId).dialog({
					href : getContextPath() + url,
					title : title,
					queryParams : param,
					width : 512,
					height : 300,
					modal : true,
					draggable : true,
					inline : true,
					onClose : function() {
						dialogObj.remove();// 关闭时remove对话框
					}
				});
			},
			agreeLeading: function(index) {
				var rowInfo = $("#leadingZCDatagrid").datagrid("getRows")[index];
                $.messager.confirm("提示", "您确定要领用该资产吗?", function(sure) {
                    if (sure) {
                        //发送删除的请求
                        $.messager.progress(); // 显示进度条
                        $.ajax({
                            url: getContextPath() + "/console/zcgl/leading/leadingZC.do",
                            cache: false,
                            async: true,
                            data: {
                                "zcid": rowInfo.zcid
                            },
                            type: "post",
                            dataType: "text",
                            success: function(result) {
                                $.messager.progress('close');
                                if (result == "success") {
                                    $.messager.alert('成功', '领用成功');
                                    $("#leadingZCDatagrid").datagrid("reload");
                                } else {
                                    $.messager.alert('失败', '领用失败');
                                }
                            },
                            error: function() {
                                $.messager.progress('close');
                                $.messager.alert('失败', '领用失败');
                            }
                        });
                    }
                });
            },

			refuseLeading: function(index){
				var rowInfo = $("#leadingZCDatagrid").datagrid("getRows")[index];
				var param = {"zcid": rowInfo.zcid};
				leadingZCManageOpt.newDialog("refuseLeadingZCDialog", "/console/zcgl/leading/refuseLeadingZCUI.do", "拒绝领用资产",param);
			},
			
			sendbackZC: function(index){
				var rowInfo = $("#leadingZCDatagrid").datagrid("getRows")[index];
				var param = {"zcid": rowInfo.zcid};
				leadingZCManageOpt.newDialog("sendbackZCDialog", "/console/zcgl/leading/sendbackZCUI.do", "申请上交资产",param);
			},
			
			zcwxSQ: function(index){
				var rowInfo = $("#leadingZCDatagrid").datagrid("getRows")[index];
				var param = {"zcid": rowInfo.zcid};
				leadingZCManageOpt.newDialog("zcwxSQDialog", "/console/zcgl/leading/zcwxSQUI.do", "申请维修资产",param);
			},
			
			zcxzSQ: function(index){
				var rowInfo = $("#leadingZCDatagrid").datagrid("getRows")[index];
				var param = {"zcid": rowInfo.zcid};
				leadingZCManageOpt.newDialog("zcxzSQDialog", "/console/zcgl/leading/zcxzSQUI.do", "申请闲置资产",param);
			},
			
			zcbfSQ: function(index){
				var rowInfo = $("#leadingZCDatagrid").datagrid("getRows")[index];
				var param = {"zcid": rowInfo.zcid};
				leadingZCManageOpt.newDialog("zcbfSQDialog", "/console/zcgl/leading/zcbfSQUI.do", "申请报废资产",param);
			},
			
			 toopTip: function (idOrClass,showText){
			    $(idOrClass).tooltip({
			        position: 'bottom',
			        content: '<span style="color:#6A6A6A">' + showText + '</span>',
			        onShow: function(){
			            $(this).tooltip('tip').css({
			                backgroundColor: '#ffffff',
			                borderColor: '#ff8c40'
			            });
			        }
			    });
			}
			
		}	
				
		$(function() {
			//主对话框界面
			$("#leadingZCDialog").dialog({
				title : '资产领用管理列表',
				width : 1024,
				height : 600,
				closed : false,
				cache : false,
				shadow : true, //显示阴影
				resizable : false, //不允许改变大小
				modal : true, //是否窗口化
				inline : true,
				
			//是否在父容器中，若不在会出现很多BUG
			});
			
			// 定义dialog中的表的属性
			$("#leadingZCDatagrid").datagrid({
                url: getContextPath() + "/console/zcgl/leading/leadingZCList.do",
                fit: true,
                fitColumns: true,
                striped: true, //斑马线效果
                loadMsg: "数据正在加载当中，请稍等...", //加载数据时的提示消息
                pagination: true, //分页工具栏
                rownumbers: true, //显示行列号
                singleSelect: true, //只允许选中一行
                pageSize: 15, //设置分页时的初始化的分页大小
                pageList: [15], //选择分页显示行数的列表里面的值
                toolbar: "#leadingZCToolbar",
                emptyMsg : "没有数据", 
                nowrap: true, //是否在一行显示所有，自动换行
                columns: [ [
										{
											field: "zcid",
					                        title: "资产编号",
					                        width: "10%",
					                        align: "center",
					                        ahlign: "center",
					                        resizable: false,
					                        formatter:function(value,row){ 
					                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
					                           return content;  
					                       	}
										},
										{
											field : "zc",
											title: "资产名称",
					                        width: "15%",
					                        align: "center",
					                        ahlign: "center",
					                        resizable: false,
					                        formatter:function(value,row){ 
					                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
					                           return content;  
					                       	}
										},
										{
											field : "zclx",
											title: "资产类型",
					                        width: "15%",
					                        align: "center",
					                        ahlign: "center",
					                        resizable: false,
					                        formatter:function(value,row){ 
					                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
					                           return content;  
					                       	}
										},
										{
											field : "zjnx",
											title: "折旧年限",
					                        width: "10%",
					                        align: "center",
					                        ahlign: "center",
					                        resizable: false,
					                        formatter:function(value,row){ 
					                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
					                           return content;  
					                       	}
										},
										{
											field : "num",
											title: "资产数量",
					                        width: "10%",
					                        align: "center",
					                        ahlign: "center",
					                        resizable: false,
					                        formatter:function(value,row){ 
					                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
					                           return content;  
					                       	}
										},
										{
											field : "deptName",
											title: "所属部门",
					                        width: "15%",
					                        align: "center",
					                        ahlign: "center",
					                        resizable: false,
					                        formatter:function(value,row){ 
					                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
					                           return content;  
					                       	}
										},
										{
											field : "glrmc",
											title: "部门资产管理人",
					                        width: "11.8%",
					                        align: "center",
					                        ahlign: "center",
					                        resizable: false,
					                        formatter:function(value,row){ 
					                           var content = "<span title='<div><img src="+getContextPath()+row.picUrl+"_m.jpg /></div>' class='tip'>"+value+"</span>";   
					                           return content;  
					                       	}
										},
										{
											field : "option",
											title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作",
					                        width: "15%",
					                        align: "left",
					                        ahlign: "center",
					                        resizable: false,
					                        formatter: function(value, row, index) {
					                        	if(row.zczt==10){
					                        		return '&nbsp;&nbsp;<a href="#" class="agreeLeadingZCBtn" onclick="leadingZCManageOpt.agreeLeading(' 
					                              	+ index + ')"></a>&nbsp;&nbsp;<a href="#" class="refuseLeadingZCBtn" onclick="leadingZCManageOpt.refuseLeading(' 
					                              	+ index + ')"></a>';
					                        	}else if(row.zczt==1){
					                        		return '&nbsp;&nbsp;<a href="#" class="sendbackZCBtn"  onclick="leadingZCManageOpt.sendbackZC(' 
					                              	+ index + ')"></a>&nbsp;&nbsp;<a href="#" class="zcwxSQBtn" onclick="leadingZCManageOpt.zcwxSQ(' 
					                              	+ index + ')"></a>&nbsp;&nbsp;<a href="#" class="zcxzSQBtn" onclick="leadingZCManageOpt.zcxzSQ(' 
					                              	+ index + ')"></a>&nbsp;&nbsp;<a href="#" class="zcbfSQCBtn" onclick="leadingZCManageOpt.zcbfSQ(' 
					                              	+ index + ')"></a>';
					                        	}
					                            
					                        }
										} ] ],
										onLoadSuccess: function(data) {
											  
						                      $(".agreeLeadingZCBtn").linkbutton({
						                          iconCls: "icon-ok",
						                          plain: true,
						                          height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
						                      });
							            	  $(".agreeLeadingZCBtn").tooltip({
							            		  position: 'bottom',    
							            		  content: '同意领用',
							            	  });
						                      $(".refuseLeadingZCBtn").linkbutton({
						                          iconCls: "icon-no",
						                          plain: true,
						                          height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
						                      });
							            	  $(".refuseLeadingZCBtn").tooltip({
							            		  position: 'bottom',    
							            		  content: '拒绝领用',
							            	  });
						                      
						                      $(".sendbackZCBtn").linkbutton({
						                          iconCls: "icon-submitzc",
						                          plain: true,
						                          height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
						                      });
						                      $(".sendbackZCBtn").tooltip({
							            		  position: 'bottom',    
							            		  content: '资产上交',
							            	  });
						                      $(".zcwxSQBtn").linkbutton({
						                          iconCls: "icon-repair",
						                          plain: true,
						                          height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
						                      });
						                      $(".zcwxSQBtn").tooltip({
							            		  position: 'bottom',    
							            		  content: '申请维修',
							            	  });
						                      $(".zcxzSQBtn").linkbutton({
						                          iconCls: "icon-free",
						                          plain: true,
						                          height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
						                      });
						                      $(".zcxzSQBtn").tooltip({
							            		  position: 'bottom',    
							            		  content: '申请闲置',
							            	  });
						                      $(".zcbfSQCBtn").linkbutton({
						                          iconCls: "icon-rubbish",
						                          plain: true,
						                          height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
						                      });
						                      $(".zcbfSQCBtn").tooltip({
							            		  position: 'bottom',    
							            		  content: '申请报废',
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
							});
		
	</script>
</body>
</html>