<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>测试页面</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/flexslider/flexslider.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/fullscreenslides/css/fullscreenstyle.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/style/assets/css/mystyle.css">
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/swh_admin.js"></script>
  </head>
  
  <body>
	<div align="center" style="margin-top: 25%">
		<p>查找易耗品类型：<input id="yhplxSearchBox"/></p>
	</div>
	<script type="text/javascript">
		$("#yhplxSearchBox").searchbox({
			wdith: 120,
			editable: false,
			searcher: function(value,name){
				queryYhplx();
			}
		});
		
		function queryYhplx(){
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
						$("#yhplxSearchBox").searchbox("setValue",cyhplx.mc);
						$("#yhplxSearchBox").attr("lxid",cyhplx.lxid);
					}
					dialogObj.remove();// 关闭时remove对话框
				}
			});
		    
		}
	</script>
  </body>
</html>
