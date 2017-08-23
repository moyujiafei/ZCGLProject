<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="weui-footer weui-footer_fixed-bottom">
	<p class="weui-footer__links ">
		<a href="javascript:home();" class="weui-footer__link">设备巡检微助手 v1.0 测试版</a>
	</p>
	<p class="weui-footer__text">湖北潮涌信息科技有限公司 &copy; 2008-2017</p>
</div>

<script>
  function home() {
	  location.href="${pageContext.servletContext.contextPath }/wx/home.do";
  }
</script>