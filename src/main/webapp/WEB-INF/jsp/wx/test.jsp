<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<title>设备巡检微助手</title>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath }/style/weui.css" />

<script src="${pageContext.servletContext.contextPath }/js/wx.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath }/js/jweixin.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/weui.min.js"></script>
<script src="${pageContext.servletContext.contextPath }/js/jquery.min.js"></script>
</head>
<body ontouchstart="">

<div class="page">
    <div class="page__hd">
        <h1 class="page__title">测试页面</h1>
        <p class="page__desc">许庆炜（巡检员，计算机学院）</p>
    </div>
     <div class="weui-grids">
        <a class="weui-grid" id="chooseImage">
            <div class="weui-grid__icon">
                <img src="${pageContext.servletContext.contextPath }/images/icon_nav_icons.png" alt="">
            </div>
            <p class="weui-grid__label">选择图片</p>
        </a>
        <a class="weui-grid" id="previewImage">
            <div class="weui-grid__icon">
                <img src="${pageContext.servletContext.contextPath }/images/icon_nav_actionSheet.png" alt="">
            </div>
            <p class="weui-grid__label">预览图片</p>
        </a>
        <a class="weui-grid" id="uploadImage">
            <div class="weui-grid__icon">
                <img src="${pageContext.servletContext.contextPath }/images/icons/upload.png" alt="">
            </div>
            <p class="weui-grid__label">上传图片</p>
        </a>
        <a class="weui-grid" id="startRecord" style="display: block;">
            <div class="weui-grid__icon">
                <img src="${pageContext.servletContext.contextPath }/images/icons/start_record.png" alt="">
            </div>
            <p class="weui-grid__label">开始录音</p>
        </a>
        <a class="weui-grid" id="stopRecord"  style="display: none;">
            <div class="weui-grid__icon">
                <img src="${pageContext.servletContext.contextPath }/images/icons/stop_record.png" alt="">
            </div>
            <p class="weui-grid__label">停止录音</p>
        </a>
        <a class="weui-grid" id="playVoice">
            <div class="weui-grid__icon">
                <img src="${pageContext.servletContext.contextPath }/images/icons/play_record.png" alt="">
            </div>
            <p class="weui-grid__label">播放录音</p>
        </a>
        <a class="weui-grid" id="uploadVoice">
            <div class="weui-grid__icon">
                <img src="${pageContext.servletContext.contextPath }/images/icons/upload.png" alt="">
            </div>
            <p class="weui-grid__label">上传录音</p>
        </a>         
    </div>


	<a href="javascript:ok();" class="weui-btn weui-btn_primary">确定</a>
	<jsp:include page="footer.jsp"></jsp:include>

	</body>
<script>
  wx.config({
      debug: false,
      appId: '${appId}',
      timestamp: '${timestamp}',
      nonceStr: '${nonceStr}',
      signature: '${signature}',
      jsApiList: [
                  'checkJsApi',
                  'onMenuShareTimeline',
                  'onMenuShareAppMessage',
                  'onMenuShareQQ',
                  'onMenuShareWeibo',
                  'onMenuShareQZone',
                  'hideMenuItems',
                  'showMenuItems',
                  'hideAllNonBaseMenuItem',
                  'showAllNonBaseMenuItem',
                  'translateVoice',
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
                  'getNetworkType',
                  'openLocation',
                  'getLocation',
                  'hideOptionMenu',
                  'showOptionMenu',
                  'closeWindow',
                  'scanQRCode',
                  'chooseWXPay',
                  'openProductSpecificView',
                  'addCard',
                  'chooseCard',
                  'openCard'
      ]
  });
</script>	

<script type="text/javascript">
	  var images = {
	    localId: [],
	    serverId: []
	  };
	  
      wx.ready(function() {
		  document.querySelector('#chooseImage').onclick = function () {
			  images = {
			    localId: [],
			    serverId: []
			  };
		    wx.chooseImage({
		      success: function (res) {
		        images.localId = res.localIds;
		        alert('已选择 ' + res.localIds.length + ' 张图片');
		      }
		    });
		  };

		  // 5.2 图片预览
		  document.querySelector('#previewImage').onclick = function () {
		    wx.previewImage({
		      current: images.localId[0],
		      urls: images.localId
		    });
		  };

		  // 5.3 上传图片
		  document.querySelector('#uploadImage').onclick = function () {
			  if (images.localId.length == 0) {
			      alert('请先使用 chooseImage 接口选择图片');
			      return flase;
			    }
			    var i = 0, length = images.localId.length;
			    images.serverId = [];
			    function upload() {
			      wx.uploadImage({
			        localId: images.localId[i],
			        success: function (res) {
			          i++;
			          images.serverId.push(res.serverId);
				      downloadMedia("image", res);
			          if (i < length) {
			            upload();
			          }		       		
			        },
			        fail: function (res) {
			          alert(JSON.stringify(res));
			        }
			      });
			      
			    }
			    upload();
		  };

	
   // 3 智能接口
      var voice = {
        localId: '',
        serverId: ''
      };
	  function showStartRecordBtn(){
	   	$("#stopRecord").hide();
		$("#startRecord").show();
	  };
	  function showStopRecordBtn(){
		$("#startRecord").hide();
	   	$("#stopRecord").show();
	  };
      // 4 音频接口
      // 4.2 开始录音
      document.querySelector('#startRecord').onclick = function () {
    	showStopRecordBtn();
    	voice = {
    	        localId: '',
    	        serverId: ''
    	      };
        wx.startRecord({
          cancel: function () {
            alert('用户拒绝授权录音');
            showStartRecordBtn();
          }
        });
      };

      // 4.3 停止录音
      document.querySelector('#stopRecord').onclick = function () {
    	showStartRecordBtn();
        wx.stopRecord({
          success: function (res) {
            voice.localId = res.localId;
          },
          fail: function (res) {
            alert(JSON.stringify(res));
          }
        });
      };

      // 4.4 监听录音自动停止
      wx.onVoiceRecordEnd({
        complete: function (res) {
          showStartRecordBtn();
          voice.localId = res.localId;
          alert('录音时间已超过一分钟');
        }
      });

      // 4.5 播放音频
      document.querySelector('#playVoice').onclick = function () {
        if (voice.localId == '') {
          alert('请先使用 startRecord 接口录制一段声音');
          return;
        }
        wx.playVoice({
          localId: voice.localId
        });
      };

      // 4.8 监听录音播放停止
      wx.onVoicePlayEnd({
        complete: function (res) {
          alert('录音播放结束');
          showStartRecordBtn();
        }
      });

      // 4.8 上传语音
      document.querySelector('#uploadVoice').onclick = function () {
        if (voice.localId == '') {
          alert('请先使用 startRecord 接口录制一段声音');
          return;
        }
        wx.uploadVoice({
          localId: voice.localId,
          success: function (res) {
            alert('上传语音成功，serverId 为' + res.serverId);
            voice.serverId = res.serverId;
            downloadMedia("voice", res);
          }
        });
      };
	  
    });
      
	wx.error(function(res) {
		alert("微信JSSDK加载错误，请重新刷新页面");
	});
	
	
	
</script>
</html>