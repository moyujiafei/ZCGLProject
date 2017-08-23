<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<title>设备巡检微助手</title>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath }/style/weui.css" />

<script src="${pageContext.servletContext.contextPath }/js/jquery.min.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/wx.js"></script>
<script src="${pageContext.servletContext.contextPath}/js/weui.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/jweixin.js"></script>
</head>
<body >

<div class="page">
	<div class="weui-grids">
			<div class="page__hd">
		        <img alt="" style="width:100%" src="${pageContext.servletContext.contextPath }/images/home.jpg">
		    </div>
	       <a class="weui-grid" id="scanRCXJ">
	           <div class="weui-grid__icon">
	               <img src="${pageContext.servletContext.contextPath }/images/icon_nav_icons.png" alt="">
	           </div>
	           <p class="weui-grid__label">日常巡检</p>
	       </a>
	       <a class="weui-grid" id="scanGZWX">
	           <div class="weui-grid__icon">
	               <img src="${pageContext.servletContext.contextPath }/images/icon_nav_actionSheet.png" alt="">
	           </div>
	           <p class="weui-grid__label">故障维修</p>
	       </a>
	       <a class="weui-grid" id="scanWXLS">
	           <div class="weui-grid__icon">
	               <img src="${pageContext.servletContext.contextPath }/images/icon_nav_article.png" alt="">
	           </div>
	           <p class="weui-grid__label">维修历史</p>
	       </a>
	       <a href="${pageContext.servletContext.contextPath }/wx/today.do" class="weui-grid">
	           <div class="weui-grid__icon">
	               <img src="${pageContext.servletContext.contextPath }/images/icon_nav_cell.png" alt="">
	           </div>
	           <p class="weui-grid__label">今日提醒</p>
	       </a>
	       <a href="${pageContext.servletContext.contextPath }/wx/wcqk.do" class="weui-grid">
	           <div class="weui-grid__icon">
	               <img src="${pageContext.servletContext.contextPath }/images/icon_nav_toast.png" alt="">
	           </div>
	           <p class="weui-grid__label">完成情况</p>
	       </a>
	       <a href="javascript:;" class="weui-grid" onclick="resetOpretion()">
	           <div class="weui-grid__icon">
	               <img src="${pageContext.servletContext.contextPath }/images/icon_tabbar.png" alt="">
	           </div>
	           <p class="weui-grid__label">操作重置</p>
	       </a>        
	   </div>

 	<jsp:include page="footer.jsp"/>
</div>
</body>

<script>
  wx.config({
      debug: false,
      appId: '${requestScope.appId}',
      timestamp: '${requestScope.timestamp}',
      nonceStr: '${requestScope.nonceStr}',
      signature: '${requestScope.signature}',
      jsApiList: [
                  'scanQRCode',
      ]
  });
</script>	

<script type="text/javascript">

function resetOpretion(){
	$(".page").append("<div id='home_reset' class='weui-mask'><img alt='' style='margin:62% 45%;text-align:center;' src='${pageContext.servletContext.contextPath }/images/loading.gif'></div>");
	$.ajax({
		url: getContextPath() + "/wx/reset.do",
		dataType:"json",
		cache: false,
		async: true,
		success: function (result) {
			$("#home_reset").remove();
			if (result == true) {
				$(".page").append(wx_alert("提示信息", "操作已执行成功!", "javascript:;","确定"));
				$("#wx_alert_btn").bind("click",function () {
					$("#wx_alert_dialog").remove();
				});
			} else {
				$(".page").append(wx_alert("提示信息", "操作执行失败", "javascript:;","确定"));
				$("#wx_alert_btn").bind("click",function () {
					$("#wx_alert_dialog").remove();
				});
			}
		},
		error: function () {
			
		}
	});
}

wx.ready(function() {
	
	  // 日常巡检， 扫描二维码并返回结果
	  document.querySelector('#scanRCXJ').onclick = function () {
	    wx.scanQRCode({
	      needResult: 1,
	      desc: 'scanQRCode desc',
	      success: function (res) {
	    	  location.href = "${pageContext.servletContext.contextPath }/wx/rcxj.do?url=" + res.resultStr;
	      }
	    });
	  };
	  
	  // 故障维修， 扫描二维码并返回结果
	  document.querySelector('#scanGZWX').onclick = function () {
	    wx.scanQRCode({
	      needResult: 1,
	      desc: 'scanQRCode desc',
	      success: function (res) {
	        location.href = "${pageContext.servletContext.contextPath }/wx/gzwx.do?url=" + res.resultStr;
	      }
	    });
	  };
	  
	  // 维修情况， 扫描二维码并返回结果
	  document.querySelector('#scanWXLS').onclick = function () {
	    wx.scanQRCode({
	      needResult: 1,
	      desc: 'scanQRCode desc',
	      success: function (res) {
	        location.href = "${pageContext.servletContext.contextPath }/wx/wxls.do?url=" + res.resultStr;
	      }
	    });
	  };
	  
	});


wx.error(function(res) {
	alert("微信jssdk加载错误。");
});
</script>   
</html>