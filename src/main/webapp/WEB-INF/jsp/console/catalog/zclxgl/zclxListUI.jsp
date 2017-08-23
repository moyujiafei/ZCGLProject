<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>资产类型管理</title>
</head>
<body>
<div id="zclxglDialog">
	<div id="zclxglToolbar" style="padding:5px">
		<a id="zclxInsertBnt"></a>
		<a id="zclxListInsertBnt"></a>
		<a id="downZCLXExcelTempBnt"></a>
	</div>
	<div id="zclxgnDatagrid"></div>	
	<form id="downLoadForm" method="post">
		<input type="hidden" name="randomInfo" value="<%=Math.random()%>"/>
	 </form>
</div>
<script type="text/javascript">
var zclxManageOpt = {
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
		zclxManageOpt.newDialog("insertZclxDialog","/console/catalog/zclxgl/insertZCLXUI.do","新建资产分类");
	},
	update: function(index){
		var rowInfo = $("#zclxgnDatagrid").datagrid("getRows")[index];
		var param = {"id": rowInfo.id};
		zclxManageOpt.newDialog("updateZclxDialog","/console/catalog/zclxgl/updateZCLXUI.do","编辑资产分类",param);
	},
	downZCLXExcelTemp: function(){
		$.messager.confirm("导出","确定下载资产类型模板表格吗？",function(result){
			if(result){
				$("#downLoadForm").form('submit', {
 				url : getContextPath() + "/console/catalog/zclxgl/exportZCLXTempExcel.do",
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
$(function(){
	//主对话框界面
	$("#zclxglDialog").dialog({
		title: '资产类型列表',    
		width: 1024,
		height: 600,
		closed: false,
		cache: false,
		shadow: true,
		modal: true,
		inline: true,
	});
	//新增按钮
	$("#zclxInsertBnt").linkbutton({
		iconCls:'icon-add',
		plain:true,
		text:'新建',
		onClick:function(){
			zclxManageOpt.insert();
		}
	});
	
	//批量导入按钮
	$("#zclxListInsertBnt").linkbutton({
		iconCls:'icon-import',
		plain:true,
		text:'导入',
		onClick:function(){
			var insertList = $("<div id='insertZclxListDialog'></div>");
			insertList.appendTo("body");
			$("#insertZclxListDialog").dialog({
				href: getContextPath() + "/console/catalog/zclxgl/insertZCLXListUI.do",
				title: "批量导入资产分类",
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
	$("#downZCLXExcelTempBnt").linkbutton({
     	plain: true,
     	iconCls: 'icon-download',
     	text:'模板',
     	onClick: function(){
     		zclxManageOpt.downZCLXExcelTemp();
     	}
     });
	
	
	
	//数据列表
	$("#zclxgnDatagrid").datagrid({
		columns:[[
		{
			field:'lx',
			title:'分类名称',
			width:'36%',
			resizable:false,
			align:'center',
			formatter:function(value,row,index){
			 return '<div class="zclxToolTip'+index+'">'+value+'</div>';
			}
			
		},{
			field:'remark',
			title:'备注',
			width:'51%',
			resizable:false,
			align:'center',
		},{
			field:'option',
			title:'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作',
			width:'15%',
			resizable:false,
			align:'left',
			formatter:function(value,row,index){
				 return '&nbsp;&nbsp;<a  href="#" class="editBtn" title="编辑" onclick="zclxManageOpt.update('+ index + ')"></a>';
			}
		}
		]],
		toolbar:'#zclxglToolbar',
		pagination:true,
		url:getContextPath()+'/console/catalog/zclxgl/zclxList.do',
		method:'post',
		pageSize:15,
		pageList:[15],
		singleSelect: true, //只允许选中一行
		pagePosition:'bottom',
		loadMsg:"努力加载中，请稍后。。。。",
		width:'100%',
		height:'100%',
		nowrap: true, //是否在一行显示所有，自动换行
		onLoadSuccess:function(data){
			 $(".editBtn").linkbutton({
			    iconCls: "icon-edit",
			    plain: true,
			    height: 24 //这里一定要设置行高，必须是24，要不然和分页左边的不对齐
			});
			for(var i=0;i<data.total;i++){
				var id = data.rows[i].id;//获得当前行的id
				var imgRequest = getContextPath()+'/console/catalog/zclxgl/getPicUrl.do?id='+id;//通过id查询此资产的资源路径的action
				var imgURL;//图片资源的路径
				
				$.ajax({url:imgRequest,
				async:false,
				dataType:"text",
				success:function(url){
					imgURL = getContextPath()+url+"_m.jpg";//获得缩略图
				}
				});
				
				$('.zclxToolTip'+i).tooltip({
				showDelay:'1000',//延迟2s显示    
				position: 'right',    
				content: '<img src="'+imgURL+'" ></img>',    
				onShow: function(){        
					$(this).tooltip('tip').css({            
						backgroundColor: '#666',            
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
