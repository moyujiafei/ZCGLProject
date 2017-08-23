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
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/jweixin.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/weui.min.js"></script>
</head>
<body ontouchstart="">
<div class="page">
    <div class="page__hd" align="center">
        <h1 class="page__title">日常巡检</h1>
    </div>
   	<div id="rcxj_sbxx" class="page__bd">
   	<div class="weui-cells__title">设备基本信息</div>
	<div class="weui-cells">
	    <div class="weui-cell">
	        <div class="weui-cell__bd weui-cell__primary">
	            <p>设备:</p>
	        </div>
	        <div id="rcxj_sbmc" class="weui-cell__ft"></div>
	    </div>
	    <div class="weui-cell">
	        <div class="weui-cell__bd weui-cell__primary">
	            <p>类型:</p>
	        </div>
	        <div id="rcxj_sblx" class="weui-cell__ft"></div>
	    </div>
	    <div class="weui-cell">
	        <div class="weui-cell__bd weui-cell__primary">
	            <p>位置:</p>
	        </div>
	        <div id="rcxj_sbwz" class="weui-cell__ft"></div>
	    </div>
	    <div class="weui-cell">
	        <div class="weui-cell__bd weui-cell__primary">
	            <p>供应商:</p>
	        </div>
	        <div id="rcxj_sbgys" class="weui-cell__ft"></div>
	    </div>
	    <div class="weui-cell">
	        <div class="weui-cell__bd weui-cell__primary">
	            <p>设备状态:</p>
	        </div>
	        <div id="rcxj_sbzt" class="weui-cell__ft"></div>
	    </div>
	</div>
   	</div>
   	<div id="rcxj_xjqk" class="page__bd">
   	  <div class="weui-cells__title">巡检情况:</div>
   	  <div id="rcxj_xjqkItem" class="weui-cells weui-cells_radio"> 
	    
      </div>
    </div>
</div>
<a href="javascript:;" class="weui-btn weui-btn_primary weui-footer_fixed-bottom" onclick="rcxj_toNext()">确认</a>
<div id="rcxj_ErrorMsg"></div>
	
<script type="text/javascript">
//查数据库获得设备信息(设备,类型,位置,供应商,设备状态)
//发一个ajax
	var returnUrl = getContextPath()+"/wx/home.do",
	rcxj_sbxx = document.querySelector("#rcxj_sbxx"),
	rcxj_sbmc = document.querySelector("#rcxj_sbmc"), 
	rcxj_sblx = document.querySelector("#rcxj_sblx"), 
	rcxj_sbwz = document.querySelector("#rcxj_sbwz"), 
	rcxj_sbgys = document.querySelector("#rcxj_sbgys"), 
	rcxj_sbzt = document.querySelector("#rcxj_sbzt"),
	rcxj_ErrorMsg = document.querySelector("#rcxj_ErrorMsg"),
	rcxj_xjqkItem = document.querySelector("#rcxj_xjqkItem");
	
	
	var sbmcStr = "",
	sblxStr = "",
	sblxidStr = "",
	sbwzStr = "",
	sbgysStr = "",
	sbztStr = "",
	xjqkStr = "",
	sbuuidStr= "",
	rwidStr = "",
	rcxj_xjrStr = "",
	preUser_XJName = "${sessionScope.wx_login_user.name}",
	preUser_XJPosition = "${sessionScope.wx_login_user.position}";
	preUser_XJDate = "${requestScope.curDate}";
	
	var returnUrl = getContextPath() +"/wx/home.do";
	
	//先判断设备是不是能找到的设备
	$.ajax({
	  	url: getContextPath() + "/wx/getSbByUrl.do", 
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
					url: getContextPath() + "/wx/getRwByUserNameAndUuid.do", 
					async:false,
					cache:false,
					data:{"wx_username":"${sessionScope.wx_login_user.userid}","uuid":sbuuidStr}, 
					dataType:"json", 
					success:function(rwResult){
						rwResult = rwResult.result;
					  if (rwResult == null) {
						  rcxj_sbxx.innerHTML=wx_alert ("提示信息","您无权限对该设备进行巡检",returnUrl, "确定");
					  } else if (rwResult.finish!= 0) {
						  rcxj_sbxx.innerHTML=wx_alert ("提示信息","您无权限对已巡检过的设备进行巡检",returnUrl, "确定");
					  }else {
						  rwidStr = rwResult.rwid;
						  rcxj_xjrStr = rwResult.fzr;
					  }
					}, 
					error:function(){ 
					  alert("Ajax error"); 
					} 
				});
				  rcxj_sbmc.innerHTML = sbmcStr;
				  rcxj_sblx.innerHTML = sblxStr;
				  rcxj_sbwz.innerHTML = sbwzStr;
				  rcxj_sbgys.innerHTML = sbgyStr;
				  rcxj_sbzt.innerHTML = sbztStr;
			} 
			  
		},
		error: function () {
			alert("Ajax error");
		},
  	});
	
	
//设置子标题(查询数据库的到name,position,得到当前系统时间)
	
//设置填写的表单(查询数据库得到巡检情况)
//发一个ajax
	var xjqkResultStr = "";
	$.ajax ({
		url: getContextPath() + "/wx/getSbztByUuid.do", 
		async:false,
		cache:false,
		data:{"uuid":sbuuidStr}, 
		dataType:"json", 
		success:function(result){
		  for (var i = 0;i<result.length;i++) {
			  if(i == 0) {
				  xjqkResultStr = xjqkResultStr + "<label class='weui-cell weui-check__label' for='r"+i+"'>"+ 
													"<div class='weui-cell__bd'>"+result[i].mc+"</div>"+
													 "<div class='weui-cell__ft'>"+
													  "<input required='' type='radio' class='weui-check' name='sex' value='"+result[i].mc+"' id='r"+i+"' checked='checked' tips='请选择性别'>" + 
													      "<span class='weui-icon-checked'></span>"+
												      "</div>" + 
											   	  "</label>";
			  } else {
				  xjqkResultStr = xjqkResultStr + "<label class='weui-cell weui-check__label' for='r"+i+"'>"+ 
				     								"<div class='weui-cell__bd'>"+result[i].mc+"</div>"+
				    								 "<div class='weui-cell__ft'>"+
				    								  "<input required='' type='radio' class='weui-check' name='sex' value='"+result[i].mc+"' id='r"+i+"' tips='请选择性别'>" + 
				       							      "<span class='weui-icon-checked'></span>"+
				        						      "</div>" + 
											   	  "</label>";
			  }
		  };
		 rcxj_xjqkItem.innerHTML = xjqkResultStr;
		}, 
		error:function(){ 
		  alert("Ajax error"); 
		} 
		
	});

//确认按钮onclick事件
	function rcxj_toNext() {
		var xj_sbztStr = $("input:radio:checked").val(); 
		$.ajax ({
			url: getContextPath() + "/wx/updateRwxzAndSbzt.do", 
			async:false,
			cache:false,
			data:{"uuid":sbuuidStr,"rwid":rwidStr,"ztmc":xj_sbztStr, "lxid":sblxidStr}, 
			dataType:"json", 
			success:function(result){
				if (result == true) {
					if (xj_sbztStr == "完好") {
						window.location.href=getContextPath() + "/wx/home.do";
					} else {
						//要将jlr(wx_username),设备(uuid),设备状态(sbzt_id),任务(rwid)传入到wxjg.jsp
						window.location.href= getContextPath () +"/wx/wxsb.do?uuid="+sbuuidStr+"&sbzt="+xj_sbztStr+"&lxid="+sblxidStr+"&rwid="+rwidStr;	
					}
				} else {
					var curUrl = getContextPath () + "/wx/rcxj.do";
					rcxj_sbxx.innerHTML=wx_alert ("提示信息","提交任务失败", curUrl , "确定");
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