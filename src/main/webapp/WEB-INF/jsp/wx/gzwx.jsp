<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<title>设备巡检微助手</title>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath }/style/weui.css" />
<script src="${pageContext.servletContext.contextPath }/js/zepto.min.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/jquery.min.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/wx.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/jweixin.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/weui.min.js"></script>
</head>
<body ontouchstart="">
<div class="page">
    <div class="page__hd" align="center">
        <h1 class="page__title">故障维修</h1>
    </div>
    <div class="weui-cells__title">设备基本信息</div>
     <div class="weui-cells">
	  <div class="weui-cell">
	    <div class="weui-cell__bd">
	      <p>设备：</p>
	    </div>
	    <div id="gzwx_sb" class="weui-cell__ft"></div>
	  </div>
	  <div class="weui-cell">
	    <div class="weui-cell__bd">
	      <p>类型：</p>
	    </div>
	    <div id="gzwx_sblx" class="weui-cell__ft"></div>
	  </div>
	  <div class="weui-cell">
	    <div class="weui-cell__bd">
	      <p>位置：</p>
	    </div>
	    <div id="gzwx_sbwz" class="weui-cell__ft"></div>
	  </div>
	  <div class="weui-cell">
	    <div class="weui-cell__bd">
	      <p>供应商：</p>
	    </div>
	    <div id="gzwx_sbgys" class="weui-cell__ft"></div>
	  </div>
	  <div class="weui-cell">
	    <div class="weui-cell__bd">
	      <p>设备状态：</p>
	    </div>
	    <div id="gzwx_sbzt" class="weui-cell__ft"></div>
	  </div>
	</div>
	<div class="weui-cells__title">维修结果上报:</div>
   	  <div id="gzwx_wxqkItem" class="weui-cells weui-cells_radio"> 
	    
      </div>
	</div>
	
<a href="javascript:;" class="weui-btn weui-btn_primary weui-footer_fixed-bottom" onclick="gzwx_toNext()">确认</a>
<div id="gzwx_ErrorMsg"></div>

<script type="text/javascript">
	
	var gzwx_subTitle = document.querySelector("#gzwx_subTitle"),
	gzwx_sb = document.querySelector("#gzwx_sb"),
	gzwx_sblx = document.querySelector("#gzwx_sblx"),
	gzwx_sbwz = document.querySelector("#gzwx_sbwz"),
	gzwx_sbgys = document.querySelector("#gzwx_sbgys"),
	gzwx_sbzt = document.querySelector("#gzwx_sbzt"),
	gzwx_ErrorMsg = document.querySelector("#gzwx_ErrorMsg"),
	gzwx_wxqkItem = document.querySelector("#gzwx_wxqkItem");
	
	
	var preUser_Name = "${sessionScope.wx_login_user.name}",
	preUser_Position = "${sessionScope.wx_login_user.position}",
	preUser_WXDate = "${requestScope.curDate}",
	gzwx_showDate = "",
	sbmcStr = "", 
	sblxStr = "", 
	sblxidStr = "",
	sbwzStr = "", 
	sbgyStr = "", 
	sbztStr = "",
	sbuuidStr = "",
	rwidStr = "",
	returnUrl = getContextPath () + "/wx/home.do"; 
	
	//先判断设备是否是能查到的设备
	$.ajax({
	  	url: getContextPath() + "/wx/getVSbByUrl.do", 
		async:false,
		cache:false,
		data:{"url":"${requestScope.url}"}, 
		dataType:"json",
		success:function (sbResult) {
		  sbResult =sbResult.result;
			if (sbResult ==null) {
				rcxj_ErrorMsg.innerHTML = wx_alert("提示","您所扫描的设备无法识别",returnUrl, "确定");
			} else {
				  sbmcStr = sbResult.sb;
				  sblxStr = sbResult.lx;
				  sblxidStr = sbResult.lxid;
				  sbwzStr = sbResult.wz;
				  sbgyStr = sbResult.gys;
				  sbztStr = sbResult.sbzt;
				  sbuuidStr = sbResult.uuid;
				//查看是否有权维修
				$.ajax({ 
					url: getContextPath() + "/wx/getVrwByWxusernameAndUuid.do", 
					async:false,
					cache:false,
					data:{"wx_username":"${sessionScope.wx_login_user.userid}","uuid":sbuuidStr}, 
					dataType:"json", 
					success:function(rwResult){
						rwResult = rwResult.result;
					  if (rwResult == null) {
						  gzwx_ErrorMsg.innerHTML=wx_alert ("提示信息","您无权限对该设备进行维修",returnUrl, "确定");
					  } else if (rwResult.finish!= 0) {
						  gzwx_ErrorMsg.innerHTML=wx_alert ("提示信息","您无权限对已维修过的设备进行维修",returnUrl, "确定");
					  }else {
						  rwidStr = rwResult.rwid;
					  }
					}, 
					error:function(){ 
					  aelrt("Ajax error"); 
					} 
				});
				  gzwx_showDate += ("${requestScope.curDate}"+"-"+"${sessionScope.wx_login_user.name}"+"("+"${sessionScope.wx_login_user.position}"+")"+"<br/>"+ sbmcStr+"--");
				  gzwx_sb.innerHTML = sbmcStr;
				  gzwx_sblx.innerHTML = sblxStr;
				  gzwx_sbwz.innerHTML = sbwzStr;
				  gzwx_sbgys.innerHTML = sbgyStr;
				  gzwx_sbzt.innerHTML = sbztStr;
			} 
			  
		},
		error: function () {
			alert("Ajax error");
		},
  	});
	
	
	//显示维修情况
	var wxqkResultStr = "";
	$.ajax ({
		url: getContextPath() + "/wx/getSbztList.do", 
		async:false,
		cache:false,
		data:{"uuid":sbuuidStr}, 
		dataType:"json", 
		success:function(result){
			result = result.result;
		  for (var i = 0;i<result.length;i++) {
			  if(i == 0) {
				  wxqkResultStr = wxqkResultStr + "<label class='weui-cell weui-check__label' for='r"+i+"'>"+ 
													"<div class='weui-cell__bd'>"+result[i].mc+"</div>"+
													 "<div class='weui-cell__ft'>"+
													  "<input required='' type='radio' class='weui-check' name='sex' value='"+result[i].mc+"' id='r"+i+"' checked='checked' tips='请至少选择一项'>" + 
													      "<span class='weui-icon-checked'></span>"+
												      "</div>" + 
											   	  "</label>";
			  } else {
				  wxqkResultStr = wxqkResultStr + "<label class='weui-cell weui-check__label' for='r"+i+"'>"+ 
				     								"<div class='weui-cell__bd'>"+result[i].mc+"</div>"+
				    								 "<div class='weui-cell__ft'>"+
				    								  "<input required='' type='radio' class='weui-check' name='sex' value='"+result[i].mc+"' id='r"+i+"' tips='请至少选择一项'>" + 
				       							      "<span class='weui-icon-checked'></span>"+
				        						      "</div>" + 
											   	  "</label>";
			  }
		  };
		  gzwx_wxqkItem.innerHTML = wxqkResultStr;
		}, 
		error:function(){ 
		  alert("Ajax error"); 
		} 
		
	});
	
	function gzwx_toNext() {
		var gz_sbztStr = $("input:radio:checked").val(); 
		$.ajax ({
			url: getContextPath() + "/wx/updateRwxzAndSbztMutiple.do", 
			async:false,
			cache:false,
			data:{"uuid":sbuuidStr,"rwid":rwidStr,"ztmc":gz_sbztStr, "lxid":sblxidStr}, 
			dataType:"json", 
			success:function(result){
				if (result == true) {
						//要将jlr(wx_username),设备(uuid),设备状态(sbzt_id),任务(rwid)传入到wxjg.jsp
						window.location.href= getContextPath () +"/wx/wxjg.do?uuid="+sbuuidStr+"&sbzt="+gz_sbztStr+"&lxid="+sblxidStr+"&rwid="+rwidStr;	
				} else {
					var curUrl = getContextPath () + "/wx/rcxj.do";
					gzwx_ErrorMsg.innerHTML=wx_alert ("提示信息","提交任务失败", curUrl , "确定");
				}
			}, 
			error:function(){ 
			  alert("Ajax error"); 
			} 
			
		});
		
	}
</script>
	</body>
</html>