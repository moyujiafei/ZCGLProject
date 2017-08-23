<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<title>设备巡检微助手</title>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath }/style/weui.css" />
<script src="${pageContext.servletContext.contextPath }/js/jquery.min.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/wx.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/jweixin.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/weui.min.js"></script>
</head>
<body ontouchstart="">
<div class="page">
    <div class="page__hd" align="center">
        <h1 class="page__title">维修历史</h1>
        <p id="wxls_subTitle" class="weui-cells__title"></p>
    </div>
</div>

<div class="weui-cells__title">设备基本信息</div>
<div class="weui-cells">
  <div class="weui-cell">
    <div class="weui-cell__bd">
      <p>设备：</p>
    </div>
    <div id="wxls_sb" class="weui-cell__ft"></div>
  </div>
    <div class="weui-cell">
    <div class="weui-cell__bd">
      <p>类型：</p>
    </div>
    <div id="wxls_lx" class="weui-cell__ft"></div>
  </div>
    <div class="weui-cell">
    <div class="weui-cell__bd">
      <p>位置：</p>
    </div>
    <div id="wxls_wz" class="weui-cell__ft"></div>
  </div>
    <div class="weui-cell">
    <div class="weui-cell__bd">
      <p>供应商：</p>
    </div>
    <div id="wxls_gys" class="weui-cell__ft"></div>
  </div>
    <div class="weui-cell">
    <div class="weui-cell__bd">
      <p>当前状态：</p>
    </div>
    <div id="wxls_sbzt" class="weui-cell__ft"></div>
  </div>
</div>
     
<div class="weui-cells__title">维修历史</div>
<div id="wxls_wxls" class="weui-cells page__category-content"></div> 
<a href="javascript:;" class="weui-btn weui-btn_primary weui-footer_fixed-bottom" onclick="wxls_returnHome()">确认</a>
<script type="text/javascript">
	var wxls_subTitle = document.querySelector("#wxls_subTitle"),
	wxls_sb = document.querySelector("#wxls_sb"),
	wxls_lx = document.querySelector("#wxls_lx"),
	wxls_wz = document.querySelector("#wxls_wz"),
	wxls_gys = document.querySelector("#wxls_gys"),
	wxls_sbzt = document.querySelector("#wxls_sbzt"),
	wxls_wxls = document.querySelector("#wxls_wxls");
	
	var returnUrl  = getContextPath() + "/wx/home.do";
	
	$.ajax({
		url:getContextPath() + "/wx/getSbInfo.do",
		cache: false,
		async:false,
		data:{"url":"${requestScope.url}"},
		dataType:"json",
		success: function (result){
			if (result != null) {
				wxls_sb.innerHTML = result.sb;
				wxls_lx.innerHTML = result.lx;
				wxls_wz.innerHTML = result.wz;  
				wxls_gys.innerHTML = result.gys;  
				wxls_sbzt.innerHTML = result.sbzt;
			} else {
				wxls_subTitle.innerHTML = wx_alert("提示","该设备无法识别",returnUrl, "确定");
			}
		},
		error: function () {
			alert("Ajax error");
		}
	});
	
	var wxls_resultStr = "";
	$.ajax({
		url:getContextPath() + "/wx/getWxlsInfo.do",
		cache: false,
		async:false,
		data:{"url":"${requestScope.url}"},
		dataType:"json",
		success: function (result){
			for (var i = 0;i<result.length;i++) {
				wxls_resultStr +=("<a class='weui-cell weui-cell_access' href='javascript:;'>"+
								          "<div class='weui-cell__bd'><p>"+result[i].s_jlsj+"</p></div>"+
								          "<div class='weui-cell__bd'><p>"+result[i].sbzt+"</p></div>"+
								          "<div class='weui-cell__bd weui-cell__ft'><p>"+result[i].jlr+"</p></div>"+
								      "</a>"); 
			}
			wxls_wxls.innerHTML = wxls_resultStr; 
		},
		error: function () {
			alert("Ajax error");
		}
	});
	
	function wxls_returnHome() {
		window.location.href= getContextPath() + "/wx/home.do";		
	}
	
</script>
</body>
</html>