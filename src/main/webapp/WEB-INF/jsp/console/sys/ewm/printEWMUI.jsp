<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>二维码打印页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>

  <body>
	<div id="PrintEwmDialog">
		<form id="printEwmForm" method="post">
			<table align="center" style="margin-top: 30px;border-collapse:separate; border-spacing:0px 5px;">
				<tr><td align="right">资产类型：</td><td><input id="zclxInput" type="text"/></td></tr>
				<tr><td align="right">所属部门：</td><td><input id="ssbmInput" type="text"/></td></tr>
				<tr><td align="right">存放地点：</td><td><input id="cfddInput" type="text"/></td></tr>
				<tr><td align="right">使用人：</td><td><input id="syrInput" type="text"/></td></tr>
				<tr><td align="right">部门管理员：</td><td><input id="bmglrInput" type="text"/></td></tr>
			</table>
			<table align="center" style="margin-top: 40px;">
				<tr><td align="right"><a id="printEwmConfirm" href="#">确认</a></td><td width="50px;"></td><td><a id="printEwmCancel" href="#" >取消</a></td></tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#PrintEwmDialog").dialog({
				title: '二维码打印',    
			    width: 512,    
			    height: 300,    
			    closed: false,    
			    shadow: true, // 显示阴影
		        resizable: false, // 不允许改变大小
		        modal: true, // 模式化窗口，锁定父窗口
		        inline: true, // 是否在父容器中，若不在会出现很多BUG   
			});
			var cfdd=undefined;
			var printEwmUIObj={
				querySyr: function () {
					wxuser=null;
					dialogObj = $("<div id='dialogObj'></div>");
					dialogObj.appendTo("body");
					$("#dialogObj").dialog({
						href: getContextPath() + "/console/wxgl/wxuser/queryWXUserUI.do",
						title: "资产使用人查询",
						width: 512,
						height: 300,
						modal: true,
						queryParams: {isEdit: true},
						onClose: function () {
							if(wxuser!=null){
								$("#syrInput").searchbox("setValue",wxuser.name); 
								$("#syrInput").attr("userid",wxuser.userid);
							}
							dialogObj.remove();
						}
					});
				},
				queryGlr: function () {
					wxuser=null;
					dialogObj = $("<div id='dialogObj'></div>");
					dialogObj.appendTo("body");
					$("#dialogObj").dialog({
						href: getContextPath() + "/console/wxgl/wxuser/queryWXUserUI.do",
						title: "部门管理人查询",
						width: 512,
						height: 300,
						modal: true,
						queryParams: {isEdit: true},
						onClose: function () {
							if(wxuser!=null){
								$("#bmglrInput").searchbox("setValue",wxuser.name); 
								$("#bmglrInput").attr("userid",wxuser.userid);
							}
							dialogObj.remove();
						}
					});
				},
				query_zclx: function () {
						dialogObj = $("<div id='dialogObj'></div>");
						czclx  = null;
						dialogObj.appendTo("body");
						$("#dialogObj").dialog({
							href: getContextPath() + "/console/catalog/zclxgl/queryZCLXUI.do",
							title: "资产类型查询",
							width: 512,
							height: 300,
							modal: true,
							onClose: function () {
								if(czclx != null) {
									dialogObj.remove();
									$("#zclxInput").searchbox("setValue",czclx.mc);
									$("#zclxInput").attr("lxid",czclx.lxId); 
								}
								dialogObj.remove();
							}
						});
				},
				query_dept: function () {
					dialogObj = $("<div id='dialogObj'></div>");
					wxdept  = null;
					dialogObj.appendTo("body");
					$("#dialogObj").dialog({
						href: getContextPath() + "/console/wxgl/wxdept/queryWXDeptUI.do",
						title: "资产管理部门查询",
						width: 512,
						height: 300,
						modal: true,
						onClose: function () {
							if(wxdept != null) {
								dialogObj.remove();
								$("#ssbmInput").searchbox("setValue",wxdept.deptName);
								$("#ssbmInput").attr("deptno",wxdept.deptNo); 
							}
							dialogObj.remove();
						}
					});
				},
				query_zccfdd : function () {
					var queryCFDDUI = $("<div id='queryCFDDUI'></div>");
					queryCFDDUI.appendTo("body");
					vfj = null;
					$("#queryCFDDUI").dialog({
						title : "查找存放地点",
						href : getContextPath() + "/console/catalog/fjgl/queryCFDDUI.do",
						height : 300,
						width : 512,
						closed : false,
						 onClose: function() {
							 if (vfj != null){
								 queryCFDDUI.remove();
								 $('#cfddInput').searchbox("setText",vfj.xqmc + vfj.jzw + vfj.floor+vfj.room);
								 cfdd = vfj;
							 }
							 queryCFDDUI.remove();
			             }
					});
				}
			};
			var width=200;
			$("#zclxInput").searchbox({
				editable: false,
				searcher: function(value,name){
					printEwmUIObj.query_zclx();
				},
				required: true,
				width: width
			});
			$("#ssbmInput").searchbox({
				editable: false,
				searcher: function(value,name){
					printEwmUIObj.query_dept();
				},
				required: true,
				width: width
			});
			$("#cfddInput").searchbox({
				editable: false,
				searcher: function(value,name){
					printEwmUIObj.query_zccfdd();
				},
				width: width
			});
			$("#syrInput").searchbox({
				editable: false,
				searcher: function(value,name){
					printEwmUIObj.querySyr();
				},
				width: width
			});
			$("#bmglrInput").searchbox({
				editable: false,
				searcher: function(value,name){
					printEwmUIObj.queryGlr();
				},
				width: width
			});
			$("#printEwmConfirm").linkbutton({
				iconCls: 'icon-print'
			});
			$("#printEwmConfirm").on('click',function(){
				$.messager.confirm('提示','确定要下载《资产二维码列表》Pdf文件吗？',function(r){
					if(r){
						$("#printEwmForm").form('submit',{
							url: getContextPath()+'/console/sys/ewm/pdfEWM.do',
							onSubmit: function(param){
								var isValid = $(this).form('validate');
								if (!isValid){
									$.messager.alert('提示',"资产类型和所属部门不能为空！",'info');
								}
								param.zclxId=$("#zclxInput").attr("lxid");
								param.deptNo=$("#ssbmInput").attr("deptno");
								if(cfdd!=undefined || cfdd!=null){
									param.cfdd=cfdd.fjId;
								}
								param.syr=$("#syrInput").attr("userid");
								param.glr=$("#bmglrInput").attr("userid");
								return isValid;	// 返回false终止表单提交
							},
							success: function(data){
								if(data=="success"){
									$.messager.alert('提示',"下载文件成功",'info');
								}else{
									$.messager.alert('提示',data,'info');
								}
							}
						});
					}
				});
			});
			$("#printEwmCancel").linkbutton({
				iconCls: 'icon-cancel'
			});
			$("#printEwmCancel").on('click',function(){
				$("#PrintEwmDialog").dialog('close');
			});
		});
	</script>
  </body>
</html>
