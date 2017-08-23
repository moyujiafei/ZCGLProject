<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=0">
<title>设备巡检微助手</title>
<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath }/style/weui.css" />
<script src="${pageContext.servletContext.contextPath }/js/wx.js"></script>
<script
	src="${pageContext.servletContext.contextPath }/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath }/js/jweixin.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/weui.min.js"></script>
</head>
<body ontouchstart="" style="background-color: #f8f8f8">
	<div class="page__hd" align="center">
		<h1 class="page__title">今日工作提醒</h1>
		<p id="jrtx_subTitle" class="page__desc"></p>
	</div>
	<div id="jrtx_Vcells" class="weui-panel weui-panel_access">
		<c:forEach var="jrrws" items="${jrrwGroup}">
			<div class="weui-panel__bd" id="Vcells">
				<div class="weui-cells__title" style="font-size:15px">${jrrws.key }</div>
				<c:forEach var="jrrw" items="${jrrws.value }">
					<div class="weui-cell">
						<div class="weui-cell__bd">
							<p>${jrrw.sb }</p>
						</div>
						<div class="weui-cell__ft">${jrrw.rwlx }</div>
					</div>
				</c:forEach>
			</div>
		</c:forEach>
		<a class="weui-btn weui-btn_primary weui-footer_fixed-bottom"
			href="${pageContext.servletContext.contextPath }/wx/home.do">确认</a>
	</div>

	
<script type="text/javascript">
 wx.config({
      debug: false,
      appId: '${requestScope.appId}',
      timestamp: '${requestScope.timestamp}',
      nonceStr: '${requestScope.nonceStr}',
      signature: '${requestScope.signature}',
      jsApiList: [
		'closeWindow'
      ]
  });
wx.ready(function() {
	var  jrtx_subTitle = document.querySelector("#jrtx_subTitle")
	jrtx_Vcells = document.querySelector("#jrtx_Vcells");

	var preUser_Name = "${sessionScope.wx_login_user.name}",
	preUser_Position = "${sessionScope.wx_login_user.position}";
	
	if ("${sessionScope.wx_login_user}" == null ||"${sessionScope.wx_login_user}" == "" ) {
		jrtx_Vcells.innerHTML = wx_alert("提示信息","您还没注册，请与管理员联系!","javascript:wx.closeWindow();", "确定");
		return false;
	}
	
	//显示人员信息
	jrtx_subTitle.innerHTML = preUser_Name+"&nbsp;&nbsp;&nbsp;"+preUser_Position;
});
wx.error(function(res){
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
    alert(JSON.stringify(res));
});
</script>
	</body>
</html>