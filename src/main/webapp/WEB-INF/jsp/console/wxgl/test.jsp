<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>

    <title>测试页面</title>
    
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath }/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/swh_admin.js"></script>
	<style type="text/css">
	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
	</style>

  </head>
  
  <body>
	<div style="margin-top: 25%;">
	<h1 align="center">测试页面</h1>
	<table align="center">
		<tr>
			<td align="right">选择微信部门：</td>
			<td><input disabled="disabled" value="123" name="wxdept" /></td>
			<td><a id="search1" href="#"></a></td>
		</tr>
		<tr>
			<td align="right">选择微信用户：</td>
			<td><input disabled="disabled" value="123" name="wxuser" /></td>
			<td><a id="search2" href="#"></a></td>
		</tr>
	</table>
	</div>
	<script type="text/javascript">
		$('#search1').linkbutton({    
    		iconCls: 'icon-search'   
		});
		
		$('#search1').bind('click', function(){    
        	testOpt.newDialog("queryWXDeptUIDialog", "/console/wxgl/wxdept/queryWXDeptUI.do", "查找微信部门", {});    
    	});    
		  
		
		$('#search2').linkbutton({    
    		iconCls: 'icon-search'   
		});
		
		$('#search2').bind('click', function(){    
        	testOpt.newDialog("queryWXUserUIDialog", "/console/wxgl/wxuser/queryWXUserUI.do", "查找微信用户", {});    
    	}); 
    	
    	//var userRes,deptRes;
    	
    	var testOpt = {
            	newDialog: function(dialogId,url,title,param){
            		var dialogObj = $('<div id="' + dialogId + '"></div>');
					dialogObj.appendTo("body");
					$("#" + dialogId).dialog({
	                    href: getContextPath() + url,
	                    title: title,
	                    queryParams: param,
	                    width: 512,
	                    height: 300,
	                    collapsible: true,
	                    modal: true,
	                    draggable: true,
	                    inline: true,
	                    onClose: function() {
	                        dialogObj.remove();// 关闭时remove对话框
	                    }
	                });
            	}
          };
	</script>
  </body>
</html>
