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
<script type="text/javascript"
	src="${pageContext.servletContext.contextPath }/js/jweixin.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/weui.min.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/zepto.min.js"></script>
</head>
<body ontouchstart="">
	<div class="page__hd" align="center">
		<h1 class="page__title">工作完成情况</h1>
		<p class="page__desc">${user.name}&nbsp;&nbsp;&nbsp;${user.position }</p>
	</div>
	<div class="weui-tab">
		<div id="tabs" class="weui-navbar">
			<div class="weui-navbar__item weui-bar__item_on" id="unfinished">未完成</div>
			<div class="weui-navbar__item" id="finished">已完成</div>
		</div>
		<div id="tab_panel" class="weui-tab__panel">
			<c:forEach var="jrrws" items="${unFinishedRw}">
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
		</div>
		<a class="weui-btn weui-btn_primary weui-footer_fixed-bottom" href="${pageContext.servletContext.contextPath }/wx/home.do">确认</a>
	</div>
	<script type="text/javascript"> 
		var unfinished = "<c:forEach var='jrrws' items='${unFinishedRw}'>" +
			"<div class='weui-panel__bd' id='Vcells'>" +
			"<div class='weui-cells__title' style='font-size:15px'>${jrrws.key }</div>" +
			"<c:forEach var='jrrw' items='${jrrws.value }'>" +
			"<div class='weui-cell'>" +
			"<div class='weui-cell__bd'>" +
			"<p>${jrrw.sb }</p>" +
			"</div>" +
			"<div class='weui-cell__ft'>${jrrw.rwlx }</div>" +
			"</div>" +
			"</c:forEach>" +
			"</div>" +
			"</c:forEach>";
		var finished ="<c:forEach var='jrrws' items='${finishedRw}'>" +
			"<div class='weui-panel__bd' id='Vcells'>" +
			"<div class='weui-cells__title' style='font-size:15px'>${jrrws.key }</div>" +
			"<c:forEach var='jrrw' items='${jrrws.value }'>" +
			"<div class='weui-cell'>" +
			"<div class='weui-cell__bd'>" +
			"<p>${jrrw.sb }</p>" +
			"</div>" +
			"<div class='weui-cell__ft'>${jrrw.rwlx }</div>" +
			"</div>" +
			"</c:forEach>" +
			"</div>" +
			"</c:forEach>";
		$(function() {
			$('.weui-navbar__item').on('click', function() {
				$(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
				if ($(this).attr("id") == "finished") {
					document.querySelector("#tab_panel").innerHTML = finished;
				}
				else {
					document.querySelector("#tab_panel").innerHTML = unfinished;
				}
	
			});
		});
	</script>
</body>
</html>