<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>
<!-- <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/easyui/jquery.min.js"></script> -->
<title>浏览器版本错误</title>
<%-- 
<script type="text/javascript">
	$(function(){
		alert('请使用火狐浏览器');
		var userAgent = navigator.userAgent;
		if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Presto") != -1) {
		    window.location.replace("about:blank");
		} else {
		    window.opener = null;
		    window.open("", "_self","");
		    window.close();
		}
	});
</script>
--%>
</head>

<body>
	<span>下载<a href="javascript:void(0)" onclick="errorOpt.downLoadFireFox();" >火狐浏览器</a></span><br>
<%--	<span>下载<a href="javascript:void(0)" onclick="errorOpt.downLoadPlugn();" >火狐插件</a></span><br>--%>
<%--	<span>下载<a href="javascript:void(0)" onclick="errorOpt.downLoadPdf();" >PDF阅读器</a></span><br>--%>
	<script type="text/javascript">
		errorOpt = {
			downLoadFireFox : function(){
				var r=confirm("确定要下载火狐浏览器吗？");
				if(r==true){
// 					var url = '${pageContext.servletContext.contextPath}/etcs/Firefox.zip';
					var url = 'http://www.firefox.com.cn/';
					location.href = url;
				}
			},
			downLoadPlugn : function(){
				var r=confirm("确定要下载火狐插件吗？");
				if(r==true){
					location.href = '${pageContext.servletContext.contextPath}/beyond_australis-1.4.6-fx.zip';
				}
			}
		};
	</script>
</body>
</html>
