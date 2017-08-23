<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<script
	src="${pageContext.servletContext.contextPath }/js/jquery.min.js"></script>
</head>
<body ontouchstart="">

	<div class="page">
		<div class="page__hd">
			<h1 class="page__title">维修结果上报</h1>
		</div>
		<div class="weui-cells">
			<div class="weui-cell">
				<div class="weui-cell__bd weui-cell__primary">
					<p>维修结果:</p>
				</div>
				<div id="wxjg_sbqk" class="weui-cell__ft"></div>
			</div>
			<div class="weui-cell">
				<div class="weui-cell__bd weui-cell__primary">
					<p>备注:</p>
				</div>
				<div class="weui-cell__ft"></div>
			</div>
			<div class="weui-cells_form">
				<div class="weui-cell">
					<div class="weui-cell__bd">
						<textarea id="wxjg_remarkDiv" oninput="OnInput (event)" class="weui-textarea" placeholder="请输入文本" rows="3"></textarea>
						<div class="weui-textarea-counter">
							<span id="inputCountSpan" >0</span>/200
						</div>
					</div>
				</div>
			</div>
		</div>


		<div class="weui-grids">
			<a class="weui-grid" id="chooseImage">
				<div class="weui-grid__icon">
					<img
						src="${pageContext.servletContext.contextPath }/images/icon_nav_icons.png"
						alt="">
				</div>
				<p class="weui-grid__label">选择图片</p>
			</a> <a class="weui-grid" id="previewImage">
				<div class="weui-grid__icon">
					<img
						src="${pageContext.servletContext.contextPath }/images/icon_nav_actionSheet.png"
						alt="">
				</div>
				<p class="weui-grid__label">预览图片</p>
			</a> <a class="weui-grid" id="uploadImage">
				<div class="weui-grid__icon">
					<img
						src="${pageContext.servletContext.contextPath }/images/icons/upload.png"
						alt="">
				</div>
				<p class="weui-grid__label">上传图片</p>
			</a> <a class="weui-grid" id="startRecord" style="display: block;">
				<div class="weui-grid__icon">
					<img
						src="${pageContext.servletContext.contextPath }/images/icons/start_record.png"
						alt="">
				</div>
				<p class="weui-grid__label">开始录音</p>
			</a> <a class="weui-grid" id="stopRecord" style="display: none;">
				<div class="weui-grid__icon">
					<img
						src="${pageContext.servletContext.contextPath }/images/icons/stop_record.png"
						alt="">
				</div>
				<p class="weui-grid__label">停止录音</p>
			</a> <a class="weui-grid" id="playVoice">
				<div class="weui-grid__icon">
					<img
						src="${pageContext.servletContext.contextPath }/images/icons/play_record.png"
						alt="">
				</div>
				<p class="weui-grid__label">播放录音</p>
			</a> <a class="weui-grid" id="uploadVoice">
				<div class="weui-grid__icon">
					<img
						src="${pageContext.servletContext.contextPath }/images/icons/upload.png"
						alt="">
				</div>
				<p class="weui-grid__label">上传录音</p>
			</a>
		</div>
	</div>

	<a href="${pageContext.servletContext.contextPath }/wx/home.do" class="weui-btn weui-btn_primary weui-footer_fixed-bottom">确定</a>

</body>
<script>
	wx.config({
		debug : false,
		appId : '${appId}',
		timestamp : '${timestamp}',
		nonceStr : '${nonceStr}',
		signature : '${signature}',
		jsApiList :  [ 
			          'startRecord',
	                  'stopRecord',
	                  'onVoiceRecordEnd',
	                  'playVoice',
	                  'onVoicePlayEnd',
	                  'pauseVoice',
	                  'stopVoice',
	                  'uploadVoice',
	                  'downloadVoice',
	                  'chooseImage',
	                  'previewImage',
	                  'uploadImage',
	                  'downloadImage',
	                  'getLocation',
	                  'closeWindow',
	                  'scanQRCode'
	                  ]
	});
</script>

<script type="text/javascript">
	//返回到当前页的URL
    var toPrePageUrl = "javascript:;";
    
	var images = {
		localId : [],
		serverId : []
	};

	//设置内容
	var wxjg_sbqkDiv = document.querySelector("#wxjg_sbqk"),//DOM对象
	wxjg_remarkDiv = document.querySelector("#wxjg_remarkDiv");
	wxjg_sbqkDiv.innerHTML = "${requestScope.sbzt}";
	function OnInput (event) {
		inputCountSpan.innerHTML = wxjg_remarkDiv.value.length;
	}

	wx.ready(function() {
		document.querySelector('#chooseImage').onclick = function() {
			images = {
				localId : [],
				serverId : []
			};
			wx.chooseImage({
				success : function(res) {
					images.localId = res.localIds;
					alert('已选择 ' + res.localIds.length + ' 张图片');
				}
			});
		};

		// 5.2 图片预览
		document.querySelector('#previewImage').onclick = function() {
			wx.previewImage({
				current : images.localId[0],
				urls : images.localId
			});
		};
		
		// 5.3 上传图片
		document.querySelector('#uploadImage').onclick = function() {
			if (images.localId.length == 0) {
				alert('请先使用 chooseImage 接口选择图片');
				return flase;
			}
			var i = 0, length = images.localId.length;
			images.serverId = [];
			$(".page").append("<div id='wxjg_uploadMask' class='weui-mask'><img alt='' style='margin:62% 45%;text-align:center;' src='${pageContext.servletContext.contextPath }/images/loading.gif'></div>");
			function upload() {
				wx.uploadImage({
					localId : images.localId[i],
					isShowProgressTips: 0,
					success : function(res) {
						i++;
						images.serverId.push(res.serverId);
						downloadMedia("image", res);
// 						alert("res.serverId="+res.serverId);
						saveImageWXJL(res.serverId);
						if (i < length) {
							upload();
						} else {
							$("#wxjg_uploadMask").remove();
							$(".page").append(wx_alert("提示信息", "提交图片文件成功", toPrePageUrl, "确定"));
							 //给wx_alert() 绑定一个click事件
							 document.querySelector("#wx_alert_btn").onclick= function () {
								 $("#wx_alert_dialog").remove();
							 };
						}
					},
					fail : function(res) {
						alert(JSON.stringify(res));
					}
				});

			}
			upload();
			
		};
		
		
		
		// 3 智能接口
		var voice = {
			localId : '',
			serverId : ''
		};
		function showStartRecordBtn() {
			$("#stopRecord").hide();
			$("#startRecord").show();
		}
		;
		function showStopRecordBtn() {
			$("#startRecord").hide();
			$("#stopRecord").show();
		}
		;
		// 4 音频接口
		// 4.2 开始录音
		document.querySelector('#startRecord').onclick = function() {
			showStopRecordBtn();
			voice = {
				localId : '',
				serverId : ''
			};
			wx.startRecord({
				cancel : function() {
					alert('用户拒绝授权录音');
					showStartRecordBtn();
				}
			});
		};

		// 4.3 停止录音
		document.querySelector('#stopRecord').onclick = function() {
			showStartRecordBtn();
			wx.stopRecord({
				success : function(res) {
					voice.localId = res.localId;
				},
				fail : function(res) {
					alert(JSON.stringify(res));
				}
			});
		};

		// 4.4 监听录音自动停止
		wx.onVoiceRecordEnd({
			complete : function(res) {
				showStartRecordBtn();
				voice.localId = res.localId;
				alert('录音时间已超过一分钟');
			}
		});

		// 4.5 播放音频
		document.querySelector('#playVoice').onclick = function() {
			if (voice.localId == '') {
				alert('请先使用 startRecord 接口录制一段声音');
				return;
			}
			wx.playVoice({
				localId : voice.localId
			});
		};

		// 4.8 监听录音播放停止
		wx.onVoicePlayEnd({
			complete : function(res) {
				alert('录音播放结束');
				showStartRecordBtn();
			}
		});

		// 4.8 上传语音
		document.querySelector('#uploadVoice').onclick = function() {
			if (voice.localId == '') {
				alert('请先使用 startRecord 接口录制一段声音');
				return;
			}
			wx.uploadVoice({
				localId : voice.localId,
				success : function(res) {
// 					alert('上传语音成功，serverId 为' + res.serverId);
					voice.serverId = res.serverId;
					downloadMedia("voice", res);
					saveVoiceWXJL(res.serverId);
				}
			});
		};

		//将获取到的Image数据保存到数据库
		function saveImageWXJL(serverId){
			//获取地理位置
			wx.getLocation({
				type : 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
				success : function(res) {
					$.ajax({
						url: getContextPath () + "/wx/InsertWXJG_Wxjl.do",
						cache: false,
						async: true,
						data:{"rwid":"${requestScope.rwid}","jlwz":JSON.stringify(res),"sbzt_id":"${requestScope.sbzt_id}","media_type":"image","wx_media_id":serverId,"uuid":"${requestScope.uuid}","remark":wxjg_remarkDiv.value},
						dataType: "json",						
						success: function (result) {
							if (result == true) {
								
							}
						}, 
						error: function () {
							alert("Ajax error!");
						}
					});
				}
			});
		};
		
		//将获取到的Voice数据保存到数据库
		function saveVoiceWXJL(serverId){
			//获取地理位置
			wx.getLocation({
				type : 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
				success : function(res) {
					$.ajax({
						url: getContextPath () + "/wx/InsertWXJG_Wxjl.do",
						cache: false,
						async: false,
						data:{"rwid":"${requestScope.rwid}","jlwz":JSON.stringify(res),"sbzt_id":"${requestScope.sbzt_id}","media_type":"voice","wx_media_id":serverId,"uuid":"${requestScope.uuid}","remark":wxjg_remarkDiv.value},
						dataType: "json",						
						success: function (result) {
							if (result == true) {
								$(".page").append(wx_alert("提示信息", "提交音频文件成功", toPrePageUrl, "确定"));
								 //给wx_alert() 绑定一个click事件
								 document.querySelector("#wx_alert_btn").onclick= function () {
									 $("#wx_alert_dialog").remove();
								 };
							}
						}, 
						error: function () {
							alert("Ajax error!");
						}
					});
				}
			});
		};
		
		
	});

	wx.error(function(res) {
		alert("微信JSSDK加载错误，请重新刷新页面");
	});
</script>
</html>