<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>易耗品类型管理</title>
</head>
<body>
<div id="yhplxglDialog">
	<div id="yhplxglToolbar" style="padding:5px">
		<a id="yhplxInsertBnt"></a>
		<a id="YHPLXListInsertBnt"></a>
		<a id="downYHPLXExcelTempBnt"></a>
	</div>
	<div id="yhplxglDatagrid"></div>	
	<form id="downLoadForm" method="post">
		<input type="hidden" name="randomInfo" value="<%=Math.random()%>"/>
	 </form>
</div>
<script type="text/javascript">
var yhplxManageOpt = {
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
		yhplxManageOpt.newDialog("insertYHPLXDialog","/console/catalog/yhplxgl/insertYHPLXUI.do","新建易耗品分类");
	},
	update: function(index){
		var rowInfo = $("#yhplxglDatagrid").datagrid("getRows")[index];
		var param = {"id": rowInfo.id};
		yhplxManageOpt.newDialog("updateYHPLXDialog","/console/catalog/yhplxgl/updateYHPLXUI.do","编辑易耗品分类",param);
	},
	downYHPLXExcelTemp: function(){
		$.messager.confirm("导出","确定下载易耗品类型模板表格吗？",function(result){
			if(result){
				$("#downLoadForm").form('submit', {
 				url : getContextPath() + "/console/catalog/yhplxgl/exportYHPLXTempExcel.do",
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
	$("#yhplxglDialog").dialog({
		title: '易耗品类型列表',    
		width: 1024,
		height: 600,
		closed: false,
		cache: false,
		shadow: true,
		modal: true,
		inline: true,
	});
	//新增按钮
	$("#yhplxInsertBnt").linkbutton({
		iconCls:'icon-add',
		plain:true,
		text:'新建',
		onClick:function(){
			yhplxManageOpt.insert();
		}
	});
	
	//批量导入按钮
	$("#YHPLXListInsertBnt").linkbutton({
		iconCls:'icon-import',
		plain:true,
		text:'导入',
		onClick:function(){
			var insertList = $("<div id='insertYHPLXListDialog'></div>");
			insertList.appendTo("body");
			$("#insertYHPLXListDialog").dialog({
				href: getContextPath() + "/console/catalog/yhplxgl/insertYHPLXListUI.do",
				title: "批量导入易耗品分类",
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
	$("#downYHPLXExcelTempBnt").linkbutton({
     	plain: true,
     	iconCls: 'icon-download',
     	text:'模板',
     	onClick: function(){
     		yhplxManageOpt.downYHPLXExcelTemp();
     	}
     });
	
	
	
	//数据列表
	$("#yhplxglDatagrid").datagrid({
		columns:[[
		{
			field:'lx',
			title:'分类名称',
			width:'36%',
			resizable:false,
			align:'center',
			formatter:function(value,row,index){
			 return '<div class="YHPLXToolTip'+index+'">'+value+'</div>';
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
				 return '&nbsp;&nbsp;<a  href="#" class="editBtn" title="编辑" onclick="yhplxManageOpt.update('+ index + ')"></a>';
			}
		}
		]],
		toolbar:'#yhplxglToolbar',
		pagination:true,
		url:getContextPath()+'/console/catalog/yhplxgl/yhplxList.do',
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
				var imgRequest = getContextPath()+'/console/catalog/yhplxgl/getPicUrl.do?id='+id;//通过id查询此资产的资源路径的action
				var imgURL;//图片资源的路径
				
				$.ajax({url:imgRequest,
				async:false,
				dataType:"text",
				success:function(url){
					imgURL = getContextPath()+url+"_m.jpg";//获得缩略图
				}
				});
				
				$('.YHPLXToolTip'+i).tooltip({
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
